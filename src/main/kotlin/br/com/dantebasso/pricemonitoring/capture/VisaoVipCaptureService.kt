package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus
import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.capture.processor.VisaoVipLineProcessor
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import br.com.dantebasso.pricemonitoring.service.mail.EmailServiceSender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.InputStreamReader
import org.jsoup.Jsoup
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class VisaoVipCaptureService @Autowired constructor(
    private val processor: VisaoVipLineProcessor,
    private val jobCaptureLogService: JobCaptureLogService,
    private val emailServiceSender: EmailServiceSender
): ICapture {

    private val logger = LoggerFactory.getLogger(VisaoVipCaptureService::class.java)

    companion object {
        const val JOB_NAME = "VisaoVip"
        private const val JOB_NAME_DESCRIPTION = "Visão Vip Capture Service"
        private const val JOB_URL = "https://www.visaovip.com"

        private const val XPATH_FIELD_VIEW_STATE_ELEMENT = "//*[@id=\"j_id1:javax.faces.ViewState:2\"]"
        private const val XPATH_FIELD_NAME_ELEMENT = "//*[@id=\"form-lista-preco\"]"
        private const val XPATH_BUTTON_ELEMENT = "//*[@class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left ui-button-flat botao-lista-preco\"]"
        private const val ATTR_FIELD_VIEW = "value"
        private const val ATTR_FIELD_NAME = "name"
        private const val ATTR_FIELD_ID = "id"
    }

    override fun capture() {
        if (!jobCaptureLogService.jobWasExecutedTodayAndWithSuccess(JOB_NAME)) {
            val restTemplate = RestTemplate()
            val requestData = captureInfo()
            val headers = createHeaders(requestData)
            val body = createBody(requestData)
            val httpEntity = HttpEntity(body, headers)
            val method = HttpMethod.POST
            val responseEntity = restTemplate.exchange(
                JOB_URL,
                method,
                httpEntity,
                String::class.java
            )
            val curlCommand = getCurlCommandLine(headers, method, body)

            if (responseEntity.statusCode.is2xxSuccessful) {
                val inputStream = responseEntity.body?.byteInputStream()
                val reader = BufferedReader(InputStreamReader(inputStream))
                val listOfResults = mutableListOf<LineProcessStatus>()

                reader.useLines { lines ->
                    lines.forEach { line ->
                        listOfResults.add(processor.processLine(line))
                    }
                }
                createLogAndSave(
                    jobProcessStatus = JobProcessStatus.JOB_SUCCESS,
                    content = responseEntity.body,
                    message ="Success",
                    curlCommand = curlCommand,
                    listOfResults = listOfResults
                )
                emailServiceSender.sendNotificationEmailWhenSuccessfullyFinishedTheJobProcess(JOB_NAME_DESCRIPTION)
                reader.close()
            } else {
                logger.error("Failure to download file, HTTP STATUS: ${responseEntity.statusCode}")
                createLogAndSave(
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

    fun createLogAndSave(jobProcessStatus: JobProcessStatus, content: String?, message: String, curlCommand: String, listOfResults: List<LineProcessStatus>?) {

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
    }

    private fun getCurlCommandLine(headers: HttpHeaders, method: HttpMethod, body: String): String {
        var curlCommand = "curl -X ${method.name()} $JOB_URL"
        for ((key, value) in headers) {
            val temp = value.toString().replace("[", "").replace("]", "")
            curlCommand += " -H \'$key: $temp\'"
        }
        if (body.isNotEmpty()) {
            curlCommand += " -d '$body'"
        }
        return curlCommand
    }

    private fun createBody(requestData: RequestData) = "form-lista-preco=${requestData.fieldNameValue}&${requestData.fieldButtonValue}=&javax.faces.ViewState=${requestData.fieldViewValue}"

    private fun createHeaders(requestData: RequestData): HttpHeaders {
        val headers = HttpHeaders()
        headers.accept = listOf(
            MediaType.TEXT_HTML,
            MediaType.APPLICATION_XHTML_XML,
            MediaType.APPLICATION_XML,
            MediaType.parseMediaType("image/avif"),
            MediaType.parseMediaType("image/webp"),
            MediaType.parseMediaType("image/apng"),
            MediaType.ALL
        )
        headers.set("Connection", "keep-alive")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Origin", "https://visaovip.com")
        headers.set("Referer", "https://visaovip.com/")
        headers.set(
            "Cookie",
            "_fbp=fb.1.1676474393978.1698996418; primefaces.download_index.xhtml=true; _ga_2J4J87E94X=GS1.1.1691776093.15.0.1691776103.50.0.0; _gcl_au=1.1.1040101341.1699293899; _ga=GA1.1.1546199250.1676474392; ${requestData.cookies.get(0)}; ${requestData.cookies.get(1)}; _ga_ET2JLJZ642=GS1.1.1703795403.89.1.1703795404.59.0.0"
        )
        return headers
    }

    fun captureInfo(): RequestData {
        val connection = URI(JOB_URL).toURL().openConnection()
        val html = connection.getInputStream().bufferedReader().use { it.readText() }
        val document = Jsoup.parse(html)

        val cookies = connection.getHeaderFields()
            .flatMap { entry ->
                if (entry.key != null && entry.key.equals("Set-Cookie", ignoreCase = true)) {
                    entry.value
                } else {
                    emptyList()
                }
            }
            .map { cookie ->
                cookie.split(";\\s*".toRegex())
                    .first() // Pegando apenas o valor do cookie, ignorando os parâmetros adicionais
            }

        val fieldViewStateElement = document.selectXpath(XPATH_FIELD_VIEW_STATE_ELEMENT)
        val fieldNameElement = document.selectXpath(XPATH_FIELD_NAME_ELEMENT)
        val fieldButtonElement = document.selectXpath(XPATH_BUTTON_ELEMENT)

        val fieldViewValue = fieldViewStateElement.attr(ATTR_FIELD_VIEW)
        val fieldNameValue = fieldNameElement.attr(ATTR_FIELD_NAME)
        val fieldButtonValue = fieldButtonElement.attr(ATTR_FIELD_ID)

        return RequestData(
            fieldButtonValue = fieldButtonValue,
            fieldNameValue = fieldNameValue,
            fieldViewValue = fieldViewValue,
            cookies = cookies
        )
    }

    inner class RequestData(
        val fieldViewValue: String,
        val fieldNameValue: String,
        val fieldButtonValue: String,
        val cookies: List<String>
    )

    override fun getJobName() = JOB_NAME

}