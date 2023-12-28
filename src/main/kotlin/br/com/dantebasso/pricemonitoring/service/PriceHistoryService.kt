package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.bi.PriceHistory
import br.com.dantebasso.pricemonitoring.repository.PriceHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PriceHistoryService @Autowired constructor(private val priceHistoryRepository: PriceHistoryRepository) {

    fun savePriceHistory(priceHistory: PriceHistory): PriceHistory {
        return priceHistoryRepository.save(priceHistory)
    }

}
