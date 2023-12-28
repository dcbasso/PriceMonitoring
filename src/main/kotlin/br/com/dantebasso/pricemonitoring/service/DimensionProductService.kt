package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DimensionProductService @Autowired constructor(private val productRepository: DimensionProductRepository) {

    fun findProductByName(name: String): DimensionProduct? {
        return productRepository.findByProductNameIgnoreCase(name)
    }

    fun createProduct(dimensionProduct: DimensionProduct): DimensionProduct {
        dimensionProduct.id = null
        return productRepository.saveAndFlush(dimensionProduct)
    }

}
