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
        val response = webClient.get()
            .uri("https://v6.exchangerate-api.com/v6/$apiKey/latest/USD")
            .retrieve()
            .bodyToMono(String::class.java)
            .block() // bloqueia a execução para obter o resultado síncrono

        val objectMapper = ObjectMapper()
        val rootNode = objectMapper.readTree(response)
        val ratesNode = rootNode.path("conversion_rates")

        getDimensionDate()?.let { dimensionDate ->
            val dimensionCurrencyQuote = DimensionCurrencyQuote(
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
            dimensionCurrencyQuoteService.saveCurrencyQuote(dimensionCurrencyQuote)
        } ?: run {
            //todo: exception
            logger.error("Was not possible to locate the Dimension Date")
        }
    }

    private fun getQuoteValue(currency: Currency, ratesNode: JsonNode): Double {
        return ratesNode.path(currency.code)?.asDouble() ?: 0.0
    }

    private fun getDimensionDate(): DimensionDate? {
        return dimensionDateService.findByCurrentDate()
    }

}