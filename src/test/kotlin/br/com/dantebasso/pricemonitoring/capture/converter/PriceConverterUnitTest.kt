package br.com.dantebasso.pricemonitoring.capture.converter

import br.com.dantebasso.pricemonitoring.capture.adapters.PriceHistoryAdapter
import br.com.dantebasso.pricemonitoring.capture.processor.TopdekLineProcessor
import br.com.dantebasso.pricemonitoring.capture.processor.VisaoVipLineProcessor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal

class PriceConverterUnitTest {

    private companion object {
        @JvmStatic
        fun prices() = listOf(
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 0.35", PriceDate(BigDecimal("0.35"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 1.67", PriceDate(BigDecimal("1.67"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 15.25", PriceDate(BigDecimal("15.25"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 123.45", PriceDate(BigDecimal("123.45"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 1234.56", PriceDate(BigDecimal("1234.56"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(VisaoVipLineProcessor.STORE_NAME, "US\$ 12345.67", PriceDate(BigDecimal("12345.67"), PriceHistoryAdapter.DEFAULT_CURRENCY)),

            Arguments.of(TopdekLineProcessor.STORE_NAME, "0.35", PriceDate(BigDecimal("0.35"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(TopdekLineProcessor.STORE_NAME, "1.67", PriceDate(BigDecimal("1.67"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(TopdekLineProcessor.STORE_NAME, "12.34", PriceDate(BigDecimal("12.34"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(TopdekLineProcessor.STORE_NAME, "123.45", PriceDate(BigDecimal("123.45"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(TopdekLineProcessor.STORE_NAME, "1234.56", PriceDate(BigDecimal("1234.56"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
            Arguments.of(TopdekLineProcessor.STORE_NAME, "12345.67", PriceDate(BigDecimal("12345.67"), PriceHistoryAdapter.DEFAULT_CURRENCY)),
        )
    }

    @ParameterizedTest(name = "given price {0}, currency {1}")
    @MethodSource("prices")
    fun validatePriceConverter(store: String, price: String, expected: PriceDate) {
        val result = PriceConverter.convert(price)
        assertEquals(expected, result)
    }
}