package br.com.dantebasso.pricemonitoring.models.bi

import java.time.LocalDateTime
import java.util.UUID
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "price_history",
    uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "date_id"], name = "uk_price_history_product_date")]
)
data class PriceHistory(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    val productCode: String,

    val brand: String,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val dimensionProduct: DimensionProduct,

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    val dimensionStore: DimensionStore,

    @ManyToOne
    @JoinColumn(name = "date_id", referencedColumnName = "id")
    val dimensionDate: DimensionDate,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val currency: String,

    @Column(name = "effective_date", nullable = false)
    val effectiveDate: LocalDateTime
)


