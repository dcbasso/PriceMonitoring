package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionCurrencyQuote
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DimensionCurrencyQuoteRepository : JpaRepository<DimensionCurrencyQuote, UUID> {
}