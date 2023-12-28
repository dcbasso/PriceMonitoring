package br.com.dantebasso.pricemonitoring.processor

import br.com.dantebasso.pricemonitoring.capture.adapters.DimensionProductAdapter
import br.com.dantebasso.pricemonitoring.capture.adapters.PriceHistoryAdapter
import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import br.com.dantebasso.pricemonitoring.service.DimensionDateService
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.DimensionStoreService
import br.com.dantebasso.pricemonitoring.service.PriceHistoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VisaoVipLineProcessor @Autowired constructor(
    private val dimensionProductAdapter: DimensionProductAdapter,
    private val priceHistoryAdapter: PriceHistoryAdapter,
    private val dimensionProductService: DimensionProductService,
    private val dimensionDateService: DimensionDateService,
    private val dimensionStoreService: DimensionStoreService,
    private val priceHistoryService: PriceHistoryService
): LineProcessor {

    companion object {
        private val REGEX_LINE_VALIDATOR = Regex("^\\|\\s*\\d+\\s*\\|\\s*[^|]+\\|\\s*US\\$\\s*[\\d,.]+\\s*$")
        private val REGEX_EXTRACT_DATA = Regex("^\\|\\s*(\\d+)\\s*\\|\\s*([^|]+)\\|\\s*(US\\$\\s*[\\d,.]+)\\s*$")
        private val STORE_NAME = "Visão Vip"
    }

    private val logger = LoggerFactory.getLogger(VisaoVipLineProcessor::class.java)

    /**
     *
     */
    override fun processLine(line: String) {
        if (line.matches(REGEX_LINE_VALIDATOR)) {
            val matchResult = REGEX_EXTRACT_DATA.find(line)
            matchResult?.let {
                val (code, description, price) = it.destructured
                logger.info("Valor: ${price.trim()}")

                val dimensionProduct: DimensionProduct = dimensionProductService.findProductByName(name = description)
                    ?: adaptProductAndSave(code = code, description = description)
                val dimensionDate = getDimensionDate()
                val dimensionStore = getDimensionStore()

                dimensionDate?.let { dimensionDateNotNull ->
                    dimensionStore?.let { dimensionStore ->
                        val priceHistory = priceHistoryAdapter.adapt(
                            dimensionProduct = dimensionProduct,
                            dimensionStore = dimensionStore,
                            dimensionDate = dimensionDateNotNull,
                            priceAsText = price
                        )
                        priceHistoryService.savePriceHistory(priceHistory)
                    } ?: run {
                        // todo: exception
                        logger.error("Was not possible to locate the Dimension Store, nome: $STORE_NAME")
                    }
                } ?: run {
                    //todo: exception
                    logger.error("Was not possible to locate the Dimension Date")
                }
            }
        } else {
            logger.error("Line no processed: $line")
        }
    }

    private fun adaptProductAndSave(code: String, description: String): DimensionProduct {
        return dimensionProductService.createProduct(
            dimensionProductAdapter.adapt(
                code = code,
                description = description
            )
        )
    }

    private fun getDimensionStore(): DimensionStore? {
        return dimensionStoreService.getStoreByName(name = STORE_NAME, ignoreCase = true)
    }

    private fun getDimensionDate(): DimensionDate? {
        return dimensionDateService.findByCurrentDate()
    }
}