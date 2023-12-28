package br.com.dantebasso.pricemonitoring.capture.converter

import br.com.dantebasso.pricemonitoring.capture.adapters.PriceHistoryAdapter
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class PriceConverter {

    companion object {
        private val logger = LoggerFactory.getLogger(PriceConverter::class.java)

        fun convert(priceAsText: String): PriceDate {
            try {
                val splittedValue = priceAsText.split("\\s+".toRegex())
                val price = if (splittedValue.size == 2) BigDecimal(
                    splittedValue[1].trim().replace(",", "")
                ) else PriceHistoryAdapter.DEFAULT_PRICE
                val currency = if (splittedValue.size == 2) splittedValue[0].trim() else PriceHistoryAdapter.DEFAULT_CURRENCY
                return PriceDate(price, currency)
            } catch (e: Exception) {
                logger.error("Was not possible to convert the $priceAsText to Price (BigInteger) and Currency (String)")
            }
            return PriceDate(BigDecimal.ZERO, "")
        }
    }

}

data class PriceDate(
    val price: BigDecimal,
    val currency: String
)