package br.com.dantebasso.pricemonitoring.capture.processor

import br.com.dantebasso.pricemonitoring.capture.adapters.PriceHistoryAdapter
import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus
import br.com.dantebasso.pricemonitoring.service.DimensionDateService
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.DimensionStoreService
import br.com.dantebasso.pricemonitoring.service.PriceHistoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TopdekLineProcessor @Autowired constructor(
    priceHistoryAdapter: PriceHistoryAdapter,
    dimensionProductService: DimensionProductService,
    dimensionDateService: DimensionDateService,
    dimensionStoreService: DimensionStoreService,
    priceHistoryService: PriceHistoryService
): StoresLineProcessor(
priceHistoryAdapter,
priceHistoryService,
dimensionProductService,
dimensionStoreService,
dimensionDateService
) {

    companion object {
        private val REGEX_LINE_VALIDATOR = Regex("^\\d+\\s+[A-Z].*?\\d+\\.\\d{2}\\s*\$")
        private val REGEX_EXTRACT_DATA = Regex("^(\\d+)\\s+(.+?)\\s+(\\d+\\.\\d{2})\\s*$")
        const val STORE_NAME = "Topdek"
    }

    private val logger = LoggerFactory.getLogger(TopdekLineProcessor::class.java)

    override fun processLine(line: String): LineProcessStatus {
        if (line.matches(REGEX_LINE_VALIDATOR)) {
            val matchResult = REGEX_EXTRACT_DATA.find(line)
            matchResult?.let {
                logger.info("Line processed: $line")
                val (code, description, price) = it.destructured
                return process(code, description, price)
            } ?: run {
                logger.error("Line not processed, REGEX error: $line")
                return LineProcessStatus.LINE_IGNORED
            }
        } else {
            logger.error("Line not processed: $line")
        }
        return LineProcessStatus.LINE_IGNORED
    }

    override fun getStoreName() = STORE_NAME
}