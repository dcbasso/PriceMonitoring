package br.com.dantebasso.pricemonitoring.service.raw

import br.com.dantebasso.pricemonitoring.models.raw.SourceDocument
import br.com.dantebasso.pricemonitoring.repository.raw.SourceDocumentRepository
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class SourceDocumentService(private val repository: SourceDocumentRepository) {

    fun save(sourceDocument: SourceDocument): SourceDocument {
        return repository.save(sourceDocument)
    }

    fun findById(id: UUID): Optional<SourceDocument> {
        return repository.findById(id)
    }

    fun findAll(): List<SourceDocument> {
        return repository.findAll()
    }

    fun deleteById(id: UUID) {
        repository.deleteById(id)
    }
}
