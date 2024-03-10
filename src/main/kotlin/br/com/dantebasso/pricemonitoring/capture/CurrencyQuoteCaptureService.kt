package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.bi.DimensionCurrencyQuote
import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.models.bi.enums.Currency
import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus
import br.com.dantebasso.pricemonitoring.service.DimensionCurrencyQuoteService
import br.com.dantebasso.pricemonitoring.service.DimensionDateService
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class CurrencyQuoteCaptureService @Autowired constructor(
    @Value("\${exchangeratesapi.api-key}")
    private val apiKey: String,
    private val webClient: WebClient,
    private val dimensionDateService: DimensionDateService,
    private val dimensionCurrencyQuoteService: DimensionCurrencyQuoteService,
    private val objectMapper: ObjectMapper,
    private val jobCaptureLogService: JobCaptureLogService
): ICapture {

    private val logger = LoggerFactory.getLogger(CurrencyQuoteCaptureService::class.java)

    companion object {
        private val JOB_NAME = "CurrencyQuoteCaptureService"
        private val URL = "https://v6.exchangerate-api.com/v6/"
    }

    override fun capture() {
        if (!jobCaptureLogService.jobWasExecutedTodayAndWithSuccess(JOB_NAME)) {
            webClient.get()
                .uri("$URL$apiKey/latest/USD")
                .retrieve()
                .bodyToMono(String::class.java)
                .map { objectMapper.readTree(it) }
                .map { it.path("conversion_rates") }
                .map { createDimensionCurrencyQuote(it, getDimensionDate()) }
                .doOnError { error ->
                    logger.error("Error to process currency quotes: ${error.message}")
                    createLogAndSave(
                        jobProcessStatus = JobProcessStatus.JOB_ERROR,
                        content = null,
                        message = "Failure to download file, HTTP STATUS: ${error.message}",
                        curlCommand = getCurlCommandLine()
                    )
                }
                .switchIfEmpty(Mono.error(IllegalArgumentException("DimensionDate not found")))
                .map { dimensionCurrencyQuote ->
                    dimensionCurrencyQuoteService.saveCurrencyQuote(dimensionCurrencyQuote)
                }
                .subscribe {
                    createLogAndSave(
                        jobProcessStatus = JobProcessStatus.JOB_SUCCESS,
                        content = null,
                        message = "Success",
                        curlCommand = getCurlCommandLine()
                    )
                    logger.info("Currency Quote capture executed with success.\"")
                }
        } else {
            logger.info("Job ${JOB_NAME}, already executed today ${LocalDate.now()} with success.")
        }
    }

    private fun createDimensionCurrencyQuote(
        ratesNode: JsonNode,
        dimensionDate: DimensionDate
    ): DimensionCurrencyQuote {
        return DimensionCurrencyQuote(
            USD = getQuoteValue(Currency.AMERICAN_DOLLAR, ratesNode),
            EUR = getQuoteValue(Currency.EURO, ratesNode),
            BRL = getQuoteValue(Currency.BRAZILIAN_REAL, ratesNode),
            ARS = getQuoteValue(Currency.ARGENTINE_PESO, ratesNode),
            JPY = getQuoteValue(Currency.JAPANESE_YEN, ratesNode),
            PYG = getQuoteValue(Currency.PARAGUAYAN_GUARANI, ratesNode),
            BTC = getQuoteValue(Currency.BITCOIN, ratesNode),
            ETH = getQuoteValue(Currency.ETHEREUM, ratesNode),
            CNY = getQuoteValue(Currency.CHINESE_YUAN, ratesNode),
            dimensionDate = dimensionDate,
            effectiveDate = LocalDateTime.now()
        )
    }

    private fun getCurlCommandLine(): String {
        val currentDateTime = LocalDate.now()
        val curlCommand = "curl -X ${HttpMethod.GET.name()} $URL$apiKey/latest/USD or curl -X ${HttpMethod.GET.name()} $URL$apiKey/history/latest/USD/${currentDateTime.year}/${currentDateTime.month}}/${currentDateTime.dayOfMonth}"
        return curlCommand
    }

    fun createLogAndSave(jobProcessStatus: JobProcessStatus, content: String?, message: String, curlCommand: String) {
        val quantityOfItemsProcessed = if (JobProcessStatus.JOB_SUCCESS == jobProcessStatus) 8 else 0
        val jobCaptureLog = JobCaptureLog(
            date = LocalDate.now(),
            dateTime = LocalDateTime.now(),
            content = content,
            curl = curlCommand,
            status = jobProcessStatus,
            quantityOfItemsProcessed = quantityOfItemsProcessed,
            jobName = JOB_NAME,
            message = message
        )
        jobCaptureLogService.save(jobCaptureLog)
    }

    private fun getQuoteValue(currency: Currency, ratesNode: JsonNode): Double {
        return ratesNode.path(currency.code)?.asDouble() ?: 0.0
    }

    private fun getDimensionDate(): DimensionDate {
        return dimensionDateService.findByCurrentDate()
    }

}