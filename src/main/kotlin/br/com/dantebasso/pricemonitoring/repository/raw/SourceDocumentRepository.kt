package br.com.dantebasso.pricemonitoring.repository.raw

import br.com.dantebasso.pricemonitoring.models.raw.SourceDocument
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SourceDocumentRepository : JpaRepository<SourceDocument, UUID>
