package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DimensionStoreRepository : JpaRepository<DimensionStore, UUID>
