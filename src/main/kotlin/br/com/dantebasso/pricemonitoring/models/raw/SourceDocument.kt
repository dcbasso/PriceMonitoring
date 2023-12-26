package br.com.dantebasso.pricemonitoring.models.raw

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.time.LocalDateTime

@Entity
data class SourceDocument(
    @Enumerated(EnumType.STRING)
    private val documentType: DocumentType,
    private val document: String,
    @Temporal(TemporalType.TIMESTAMP)
    private val dateTime: LocalDateTime
): BaseModel()
