package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Entity
import java.util.*

@Entity
data class DimensionStore(
    val name: String,

    override var id: UUID? = null
): BaseModel()

