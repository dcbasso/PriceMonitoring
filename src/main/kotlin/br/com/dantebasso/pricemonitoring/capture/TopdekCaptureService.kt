package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.capture.processor.TopdekLineProcessor
import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus
import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import br.com.dantebasso.pricemonitoring.service.mail.EmailServiceSender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class TopdekCaptureService @Autowired constructor(
    private val processor: TopdekLineProcessor,
    private val jobCaptureLogService: JobCaptureLogService,
    private val emailServiceSender: EmailServiceSender
): ICapture {

    private val logger = LoggerFactory.getLogger(TopdekCaptureService::class.java)

    companion object {
        const val JOB_NAME = "Topdek"
        private const val JOB_NAME_DESCRIPTION = "Topdek Capture Service"
        private const val JOB_URL = "https://www.topdek.com.br/lista_preco.php"
    }

    override fun capture() {
        if (!jobCaptureLogService.jobWasExecutedTodayAndWithSuccess(JOB_NAME)) {
            val restTemplate = RestTemplate()
            val httpEntity = HttpEntity(null, null)
            val method = HttpMethod.GET
            val responseEntity = restTemplate.exchange(
                JOB_URL,
                method,
                httpEntity,
                String::class.java
            )
            val curlCommand = getCurlCommandLine(null, method, null)

            if (responseEntity.statusCode.is2xxSuccessful) {
                val inputStream = responseEntity.body?.byteInputStream()
                val reader = BufferedReader(InputStreamReader(inputStream))
                val listOfResults = mutableListOf<LineProcessStatus>()

                reader.useLines { lines ->
                    lines.forEach { line ->
                        listOfResults.add(processor.processLine(line))
                    }
                }
                createLogAndSendEmail(
                    jobProcessStatus = JobProcessStatus.JOB_SUCCESS,
                    content = responseEntity.body,
                    message ="Success",
                    curlCommand = curlCommand,
                    listOfResults = listOfResults
                )
                reader.close()
            } else {
                logger.error("Failure to download file, HTTP STATUS: ${responseEntity.statusCode}")
                createLogAndSendEmail(
                    jobProcessStatus = JobProcessStatus.JOB_ERROR,
                    content = null,
                    message ="Failure to download file, HTTP STATUS: ${responseEntity.statusCode}",
                    curlCommand = curlCommand,
                    listOfResults = null
                )
            }
        } else {
            logger.info("Job $JOB_NAME, already executed today ${LocalDate.now()} with success.")
        }
    }

    fun createLogAndSendEmail(jobProcessStatus: JobProcessStatus, content: String?, message: String, curlCommand: String, listOfResults: List<LineProcessStatus>?) {
        val totalOfLinesSuccess = listOfResults?.filter { it == LineProcessStatus.LINE_PROCESSED }?.size ?: 0
        val finalMessage = if (!listOfResults.isNullOrEmpty()) {
            val totalOfLinesProcessed = listOfResults.size
            val totalOfLinesError = listOfResults.filter { it == LineProcessStatus.LINE_ERROR }.size
            val totalOfLinesIgnored = listOfResults.filter { it == LineProcessStatus.LINE_IGNORED }.size
            "$message. Total lines processed: $totalOfLinesProcessed, Total Lines with success: $totalOfLinesSuccess, Total Lines ignored: $totalOfLinesIgnored, Total Lines with error: $totalOfLinesError"
        } else {
           message
        }

        val jobCaptureLog = JobCaptureLog(
            date = LocalDate.now(),
            dateTime = LocalDateTime.now(),
            content = content,
            curl = curlCommand,
            status = jobProcessStatus,
            quantityOfItemsProcessed = totalOfLinesSuccess,
            jobName = JOB_NAME,
            message = finalMessage
        )
        jobCaptureLogService.save(jobCaptureLog)
        if (jobProcessStatus == JobProcessStatus.JOB_SUCCESS) {
            emailServiceSender.sendNotificationOfFinishedTheJobProcessWithSuccess(JOB_NAME, finalMessage)
        }
    }

    private fun getCurlCommandLine(headers: HttpHeaders?, method: HttpMethod, body: String?): String {
        var curlCommand = "curl -X ${method.name()} $JOB_URL"
        if (headers != null) {
            for ((key, value) in headers) {
                val temp = value.toString().replace("[", "").replace("]", "")
                curlCommand += " -H \'$key: $temp\'"
            }
        }
        if (body != null) {
            if (body.isNotEmpty()) {
                curlCommand += " -d '$body'"
            }
        }
        return curlCommand
    }

    override fun getJobName() = JOB_NAME

}