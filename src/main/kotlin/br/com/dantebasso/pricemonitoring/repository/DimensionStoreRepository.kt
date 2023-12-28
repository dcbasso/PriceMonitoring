package br.com.dantebasso.pricemonitoring.repository

import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface DimensionStoreRepository : JpaRepository<DimensionStore, UUID> {

    fun findByName(name: String): DimensionStore?
    fun findByNameIgnoreCase(name: String): DimensionStore?

}
