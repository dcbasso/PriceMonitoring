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
    private val dimensionCurrencyQuoteService: DimensionCurrencyQuoteService
): ICapture {

    private val logger = LoggerFactory.getLogger(CurrencyQuoteCaptureService::class.java)

    override fun capture() {
        webClient.get()
            .uri("https://v6.exchangerate-api.com/v6/$apiKey/latest/USD")
            .retrieve()
            .bodyToMono(String::class.java)
            .map {
                val objectMapper = ObjectMapper()
                objectMapper.readTree(it)
            }
            .map {
                it.path("conversion_rates")
            }
            .map {
                DimensionCurrencyQuote(
                    USD = getQuoteValue(Currency.AMERICAN_DOLLAR, it),
                    EUR = getQuoteValue(Currency.EURO, it),
                    BRL = getQuoteValue(Currency.BRAZILIAN_REAL, it),
                    ARS = getQuoteValue(Currency.ARGENTINE_PESO, it),
                    JPY = getQuoteValue(Currency.JAPANESE_YEN, it),
                    PYG = getQuoteValue(Currency.PARAGUAYAN_GUARANI, it),
                    BTC = getQuoteValue(Currency.BITCOIN, it),
                    ETH = getQuoteValue(Currency.ETHEREUM, it),
                    CNY = getQuoteValue(Currency.CHINESE_YUAN, it),
                    dimensionDate = getDimensionDate(),
                    effectiveDate = LocalDateTime.now()
                )
            }
            .map { dimensionCurrencyQuote ->
                dimensionCurrencyQuoteService.saveCurrencyQuote(dimensionCurrencyQuote)
            }
            .onErrorResume { error ->
                logger.error(error.message)
                Mono.error(error)
            }
            .subscribe {
                logger.error("Currency Quote capture executed with success.")
            }
    }

    private fun getQuoteValue(currency: Currency, ratesNode: JsonNode): Double {
        return ratesNode.path(currency.code)?.asDouble() ?: 0.0
    }

    private fun getDimensionDate(): DimensionDate {
        return dimensionDateService.findByCurrentDate()
    }

}