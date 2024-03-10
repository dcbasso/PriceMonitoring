package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.*

interface JobCaptureLogRepository : JpaRepository<JobCaptureLog, UUID> {

    fun findByDateAndJobName(date: LocalDate, jobName: String): JobCaptureLog?

}