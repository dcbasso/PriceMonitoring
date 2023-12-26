package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.PriceHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PriceHistoryRepository : JpaRepository<PriceHistory, UUID> {
}
