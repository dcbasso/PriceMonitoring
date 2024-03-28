package br.com.dantebasso.pricemonitoring.capture.processor

import br.com.dantebasso.pricemonitoring.capture.adapters.PriceHistoryAdapter
import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus
import br.com.dantebasso.pricemonitoring.service.DimensionDateService
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.DimensionStoreService
import br.com.dantebasso.pricemonitoring.service.PriceHistoryService
import org.slf4j.LoggerFactory

abstract class StoresLineProcessor(
    private val priceHistoryAdapter: PriceHistoryAdapter,
    private val priceHistoryService: PriceHistoryService,
    private val dimensionProductService: DimensionProductService,
    private val dimensionStoreService: DimensionStoreService,
    private val dimensionDateService: DimensionDateService,
): LineProcessor {

    private val logger = LoggerFactory.getLogger(StoresLineProcessor::class.java)

    fun process(code: String, description: String, price: String): LineProcessStatus {
        val dimensionProduct = dimensionProductService.findProductByUniqueProductCodeOrCreate(
            productCode = code,
            description = description
        )

        val dimensionDate = getDimensionDate() ?: run {
            logger.error("Was not possible to locate the Dimension Date")
            return LineProcessStatus.LINE_ERROR
        }

        val dimensionStore = getDimensionStore() ?: run {
            logger.error("Was not possible to locate the Dimension Store, nome: ${getStoreName()}")
            return LineProcessStatus.LINE_ERROR
        }

        val priceHistory = priceHistoryAdapter.adapt(
            dimensionProduct = dimensionProduct,
            dimensionStore = dimensionStore,
            dimensionDate = dimensionDate,
            priceAsText = price
        )

        return try {
            priceHistoryService.savePriceHistory(priceHistory)
            logger.info("Saved: ${priceHistory.dimensionDate.id} - ${priceHistory.dimensionProduct.id}")
            LineProcessStatus.LINE_PROCESSED
        } catch (e: Exception) {
            logger.error("Error to process product: ${priceHistory.productCode} - ${priceHistory.brand} - ${priceHistory.dimensionProduct.productName}, error: ${e.message}")
            LineProcessStatus.LINE_ERROR
        }
    }

    private fun getDimensionStore(): DimensionStore? {
        return dimensionStoreService.getStoreByName(name = getStoreName(), ignoreCase = true)
    }

    private fun getDimensionDate(): DimensionDate? {
        return dimensionDateService.findByCurrentDate()
    }

}