package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.capture.adapters.DimensionProductAdapter
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DimensionProductService @Autowired constructor(
    private val productRepository: DimensionProductRepository,
    private val dimensionProductAdapter: DimensionProductAdapter) {

    fun findProductByName(name: String): DimensionProduct? {
        return productRepository.findByProductNameIgnoreCase(name)
    }

    fun findProductByUniqueProductCodeOrCreate(productCode: String, description: String): DimensionProduct {
        return productRepository.findByUniqueProductCodeIgnoreCase(productCode) ?: adaptProductAndSave(productCode = productCode, description = description)
    }

    fun createProduct(dimensionProduct: DimensionProduct): DimensionProduct {
        dimensionProduct.id = null
        return productRepository.saveAndFlush(dimensionProduct)
    }

    /**
     * create entity and save
     */
    private fun adaptProductAndSave(productCode: String, description: String): DimensionProduct {
        return createProduct(
            dimensionProductAdapter.adapt(
                code = productCode,
                description = description.trimEnd()
            )
        )
    }

}
