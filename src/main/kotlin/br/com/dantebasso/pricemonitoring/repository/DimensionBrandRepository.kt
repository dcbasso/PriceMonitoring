package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionBrand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface DimensionBrandRepository : JpaRepository<DimensionBrand, UUID> {

    companion object {
        val ID_OUTRA_MARCA: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    }

    @Query("SELECT db FROM DimensionBrand db WHERE LOWER(db.name) = LOWER(:brandName)")
    fun findByBrandNameIgnoreCase(brandName: String): DimensionBrand?

}
