package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class DimensionProduct(
    val productName: String,

    val category: String,

    val masterCategory: String,

    val uniqueProductCode: String,

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: DimensionBrand,
): BaseModel()


