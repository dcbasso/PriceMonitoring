package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "dimension_date")
data class DimensionDate(
    @Column(nullable = false, unique = true)
    val date: LocalDate,

    @Column(nullable = false)
    val day: Int,

    @Column(nullable = false)
    val month: Int,

    @Column(nullable = false)
    val year: Int,

    @Column(nullable = false, name = "day_of_week")
    val dayOfWeek: String,

    @Column(nullable = false, name = "month_full")
    val monthFull: String,

    @Column(nullable = false, name = "month_year")
    val monthYear: String,

    @Column(nullable = false, name = "full_date")
    val fullDate: String,

    override var id: UUID? = null
): BaseModel()
