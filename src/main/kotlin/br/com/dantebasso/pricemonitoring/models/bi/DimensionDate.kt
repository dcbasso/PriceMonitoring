package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "dimension_date")
data class DimensionDate(
    @Column(nullable = false)
    val date: LocalDate,

    @Column(nullable = false)
    val day: Int,

    @Column(nullable = false)
    val month: Int,

    @Column(nullable = false)
    val year: Int,

    @Column(nullable = false, name = "month_full")
    val monthFull: String,

    @Column(nullable = false, name = "month_year")
    val monthYear: String,

    @Column(nullable = false, name = "full_date")
    val fullDate: String
): BaseModel()
