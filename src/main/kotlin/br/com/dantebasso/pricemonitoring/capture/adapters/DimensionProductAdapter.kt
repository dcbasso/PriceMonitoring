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

    private fun locateBrand(description: String): DimensionBrand {
        val words = description.split(" ")
        for (word in words) {
            val foundBrand = dimensionBrandRepository.findByBrandNameIgnoreCase(word)
            if (foundBrand != null) {
                return foundBrand
            }
        }

        return dimensionBrandRepository.findById(DimensionBrandRepository.ID_OUTRA_MARCA).get()
    }

}