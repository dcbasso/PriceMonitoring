package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.bi.DimensionCurrencyQuote
import br.com.dantebasso.pricemonitoring.repository.DimensionCurrencyQuoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DimensionCurrencyQuoteService @Autowired constructor(
    private val dimensionCurrencyQuoteRepository: DimensionCurrencyQuoteRepository
) {

    fun saveCurrencyQuote(
        dimensionCurrencyQuote: DimensionCurrencyQuote
    ): DimensionCurrencyQuote {
        return dimensionCurrencyQuoteRepository.save(dimensionCurrencyQuote)
    }
}
