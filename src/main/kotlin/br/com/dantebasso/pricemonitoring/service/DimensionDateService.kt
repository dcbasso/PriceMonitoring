package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.bi.DimensionDate
import br.com.dantebasso.pricemonitoring.repository.DimensionDateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DimensionDateService @Autowired constructor(private val dimensionDateRepository: DimensionDateRepository) {

    fun findByCurrentDate(): DimensionDate {
        val currentDate = LocalDate.now()
        return dimensionDateRepository.findByDate(currentDate)
    }
}
