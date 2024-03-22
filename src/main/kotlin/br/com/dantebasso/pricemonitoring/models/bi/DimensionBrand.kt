package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Entity
import java.util.*

@Entity
data class DimensionBrand(
    val name: String,

    override var id: UUID? = null
): BaseModel()
