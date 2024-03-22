package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DimensionProductRepository : JpaRepository<DimensionProduct, UUID> {

    fun findByProductNameIgnoreCase(productName: String): DimensionProduct?

    fun findByUniqueProductCodeIgnoreCase(productCode: String): DimensionProduct?

    fun countByBrandName(name: String): Long

    fun findByBrandName(name: String): List<DimensionProduct>

    fun findByBrandId(id: UUID): List<DimensionProduct>
}
