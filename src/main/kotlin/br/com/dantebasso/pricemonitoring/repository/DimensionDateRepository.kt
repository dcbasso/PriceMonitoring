package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface DimensionDateRepository : JpaRepository<DimensionDate, UUID> {

    fun findByDate(date: LocalDate): DimensionDate?

}
