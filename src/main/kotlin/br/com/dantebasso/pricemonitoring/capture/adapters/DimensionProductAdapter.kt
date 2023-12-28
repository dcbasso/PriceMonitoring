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
        val allBrands = dimensionBrandRepository.findAll()
        val defaultBrand = dimensionBrandRepository.findById(DimensionBrandRepository.ID_OUTRA_MARCA).get()

        val brand = locateBrand(allBrands, defaultBrand, description)

        return DimensionProduct(
            productName = description,
            category = "",
            masterCategory = "",
            uniqueProductCode = code,
            brand = brand
        )

    }

    private fun locateBrand(allBrands: List<DimensionBrand>, defaultBrand: DimensionBrand, description: String): DimensionBrand {
        return allBrands.firstOrNull {
            description.contains(it.name, ignoreCase = true)
        } ?: defaultBrand
    }
}