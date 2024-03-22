package br.com.dantebasso.pricemonitoring.capture.adapters

import br.com.dantebasso.pricemonitoring.models.bi.DimensionBrand
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionBrandRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DimensionProductAdapter @Autowired constructor(
    private val dimensionBrandRepository: DimensionBrandRepository
){

    fun adapt(
        code: String,
        description: String
    ): DimensionProduct {
        val brand = locateBrand(description)
        return DimensionProduct(
            productName = description,
            category = "",
            masterCategory = "",
            uniqueProductCode = code,
            brand = brand
        )
    }

    fun adaptNewBrand(
        product: DimensionProduct
    ): DimensionProduct {
        val brand = locateBrand(product.productName)
        return product.copy(brand = brand, id = product.id)
    }

    private fun locateBrand(description: String): DimensionBrand {
        val defaultBrand = dimensionBrandRepository.findById(DimensionBrandRepository.ID_OUTRA_MARCA).get()
        val allBrands = dimensionBrandRepository.findAll()
        return allBrands.firstOrNull {
            description.contains(it.name, ignoreCase = true)
        } ?: defaultBrand
    }
}