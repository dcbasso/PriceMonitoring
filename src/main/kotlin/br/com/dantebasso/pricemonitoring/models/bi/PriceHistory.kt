package br.com.dantebasso.pricemonitoring.models.bi

import java.time.LocalDateTime
import java.util.UUID
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "price_history")
data class PriceHistory(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val productCode: String,

    val brand: String,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: DimensionProduct,

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    val store: DimensionStore,

    @ManyToOne
    @JoinColumn(name = "id_datetime", referencedColumnName = "id")
    val dimensionDateTime: DimensionDate,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(name = "effective_date", nullable = false)
    val effectiveDate: LocalDateTime
)


