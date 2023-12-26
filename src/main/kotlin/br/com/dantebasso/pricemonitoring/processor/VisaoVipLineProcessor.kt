package br.com.dantebasso.pricemonitoring.processor

import br.com.dantebasso.pricemonitoring.capture.adapters.DimensionProductAdapter
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VisaoVipLineProcessor @Autowired constructor(
    private val dimensionProductAdapter: DimensionProductAdapter,
    private val dimensionProductRepository: DimensionProductRepository,
): LineProcessor {

    companion object {
        private val REGEX_LINE_VALIDATOR = Regex("^\\|\\s*\\d+\\s*\\|\\s*[^|]+\\|\\s*US\\$\\s*[\\d,.]+\\s*$")
        private val REGEX_EXTRACT_DATA = Regex("^\\|\\s*(\\d+)\\s*\\|\\s*([^|]+)\\|\\s*(US\\$\\s*[\\d,.]+)\\s*$")
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
                logger.info("Linha: $line")
                logger.info("Código: ${code.trim()}")
                logger.info("Descrição: ${description.trim()}")
                logger.info("Valor: ${price.trim()}")

                val dimensionProduct: DimensionProduct = dimensionProductRepository.findByProductNameIgnoreCase(description)
                    ?: adaptProductAndInsert(code = code, description = description)
            }
        } else {
            logger.error("Line no processed: $line")
        }
    }

    private fun adaptProductAndInsert(code: String, description: String): DimensionProduct {
        logger.info("Adapting Product and Inserting...")
        val dimensionProduct = dimensionProductAdapter.adapt(
            code = code,
            description = description
        )
        return dimensionProductRepository.save(dimensionProduct)
    }
}