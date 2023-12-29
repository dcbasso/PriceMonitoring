package br.com.dantebasso.pricemonitoring.models.bi

import br.com.dantebasso.pricemonitoring.models.BaseModel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "dimension_currency_quote")
data class DimensionCurrencyQuote(
    @Column(name = "usd", nullable = false)
    val USD: Double,

    @Column(name = "eur", nullable = false)
    val EUR: Double,

    @Column(name = "brl", nullable = false)
    val BRL: Double,

    @Column(name = "ars", nullable = false)
    val ARS: Double,

    @Column(name = "jpy", nullable = false)
    val JPY: Double,

    @Column(name = "pyg", nullable = false)
    val PYG: Double,

    @Column(name = "btc", nullable = false)
    val BTC: Double,

    @Column(name = "eth", nullable = false)
    val ETH: Double,

    @Column(name = "cny", nullable = false)
    val CNY: Double,

    @ManyToOne
    @JoinColumn(name = "date_id", referencedColumnName = "id")
    val dimensionDate: DimensionDate,

    @Column(name = "effective_date", nullable = false)
    val effectiveDate: LocalDateTime
): BaseModel()

