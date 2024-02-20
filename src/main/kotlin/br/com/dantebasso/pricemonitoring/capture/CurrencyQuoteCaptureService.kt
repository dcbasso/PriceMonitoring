package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.bi.DimensionCurrencyQuote
import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.models.bi.enums.Currency
import br.com.dantebasso.pricemonitoring.service.DimensionCurrencyQuoteService
import br.com.dantebasso.pricemonitoring.service.DimensionDateService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class CurrencyQuoteCaptureService @Autowired constructor(
    @Value("\${exchangeratesapi.api-key}")
    private val apiKey: String,
    private val webClient: WebClient,
    private val dimensionDateService: DimensionDateService,
    private val dimensionCurrencyQuoteService: DimensionCurrencyQuoteService,
    private val objectMapper: ObjectMapper
): ICapture {

    private val logger = LoggerFactory.getLogger(CurrencyQuoteCaptureService::class.java)

    override fun capture() {
        webClient.get()
            .uri("https://v6.exchangerate-api.com/v6/$apiKey/latest/USD")
            .retrieve()
            .bodyToMono(String::class.java)
            .map { objectMapper.readTree(it) }
            .map { it.path("conversion_rates") }
            .map { createDimensionCurrencyQuote(it, getDimensionDate()) }
            .doOnError { error ->
                logger.error("Error to process currency quotes: ${error.message}")
            }
            .switchIfEmpty(Mono.error(IllegalArgumentException("DimensionDate not found")))
            .map { dimensionCurrencyQuote ->
                dimensionCurrencyQuoteService.saveCurrencyQuote(dimensionCurrencyQuote)
            }
            .subscribe {
                logger.info("Currency Quote capture executed with success.\"")
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


    private fun getQuoteValue(currency: Currency, ratesNode: JsonNode): Double {
        return ratesNode.path(currency.code)?.asDouble() ?: 0.0
    }

    private fun getDimensionDate(): DimensionDate {
        return dimensionDateService.findByCurrentDate()
    }

}