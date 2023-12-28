package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.bi.DimensionStore
import br.com.dantebasso.pricemonitoring.repository.DimensionStoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DimensionStoreService @Autowired constructor(private val storeRepository: DimensionStoreRepository) {

    fun getStoreByName(name: String, ignoreCase: Boolean): DimensionStore? {
        return if (ignoreCase) {
            storeRepository.findByNameIgnoreCase(name)
        } else {
            storeRepository.findByName(name)
        }
    }

}
