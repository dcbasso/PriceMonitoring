package br.com.dantebasso.pricemonitoring.capture.adapters

import br.com.dantebasso.pricemonitoring.capture.converter.PriceConverter
import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import br.com.dantebasso.pricemonitoring.models.bi.PriceHistory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class PriceHistoryAdapter {

    companion object {
        const val DEFAULT_CURRENCY: String = "US$"
        val DEFAULT_PRICE: BigDecimal = BigDecimal.ZERO
    }

    fun adapt(dimensionProduct: DimensionProduct, dimensionStore: DimensionStore, dimensionDate: DimensionDate, priceAsText: String): PriceHistory {
        val priceData = PriceConverter.convert(priceAsText)
        return PriceHistory(
            id = null,
            dimensionProduct =  dimensionProduct,
            dimensionStore = dimensionStore,
            dimensionDate = dimensionDate,
            brand = dimensionProduct.brand.name,
            price = priceData.price,
            currency = priceData.currency,
            effectiveDate = LocalDateTime.now(),
            productCode = dimensionProduct.uniqueProductCode
        )
    }

}