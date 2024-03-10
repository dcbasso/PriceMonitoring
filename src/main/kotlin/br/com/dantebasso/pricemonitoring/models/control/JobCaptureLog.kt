package br.com.dantebasso.pricemonitoring.models.control

import br.com.dantebasso.pricemonitoring.models.BaseModel
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "jobcapturelog")
data class JobCaptureLog(
    @Column(nullable = false, unique = true)
    val date: LocalDate,
    @Column(nullable = false, unique = true)
    val dateTime: LocalDateTime,
    val content: String?,
    val curl: String,
    @Enumerated(EnumType.STRING)
    val status: JobProcessStatus,
    val quantityOfItemsProcessed: Int,
    @Column(nullable = false, unique = true)
    val jobName: String,
    val message: String?
): BaseModel()