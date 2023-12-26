package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Entity

@Entity
data class DimensionStore(
    val name: String
): BaseModel()

