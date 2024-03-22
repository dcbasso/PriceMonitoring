package br.com.dantebasso.pricemonitoring.capture.adapters

import br.com.dantebasso.pricemonitoring.models.bi.DimensionBrand
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionBrandRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
class DimensionProductAdapterUnitTest {

    @RelaxedMockK
    lateinit var dimensionBrandRepositoryMockk: DimensionBrandRepository

    @BeforeEach
    fun before() {
        every { dimensionBrandRepositoryMockk.findById(DimensionBrandRepository.ID_OUTRA_MARCA) } returns Optional.of(
            DimensionBrand(
                id = DimensionBrandRepository.ID_OUTRA_MARCA,
                name = "Outra Marca"
            )
        )
    }

    @Test
    fun `validate adapt New Brand will update Brand when Brand exists in database`() {
        // Given
        val productID = UUID.randomUUID()
        val product = DimensionProduct(
            id = productID,
            category = "Category 1",
            masterCategory = "Master Category 1",
            productName = "Product 1 MARCA X",
            uniqueProductCode = "123456",
            brand = DimensionBrand(
                id = DimensionBrandRepository.ID_OUTRA_MARCA,
                name = "Outra Marca"
            )
        )

        every { dimensionBrandRepositoryMockk.findAll() } returns listOf(
            DimensionBrand(
                id = UUID.randomUUID(),
                name = "MARCA X"
            )
        )

        // When
        val newProduct = DimensionProductAdapter(dimensionBrandRepositoryMockk).adaptNewBrand(
            product
        )

        // Then
        assert(newProduct.id == productID)
        assert(newProduct.brand.name == "MARCA X")
    }

    @Test
    fun `validate adapt New Brand will not update Brand when Brand exists in database`() {
        // Given
        val productID = UUID.randomUUID()
        val product = DimensionProduct(
            id = productID,
            category = "Category 1",
            masterCategory = "Master Category 1",
            productName = "Product 1 MARCA Y",
            uniqueProductCode = "123456",
            brand = DimensionBrand(
                id = DimensionBrandRepository.ID_OUTRA_MARCA,
                name = "Outra Marca"
            )
        )

        every { dimensionBrandRepositoryMockk.findAll() } returns listOf(
            DimensionBrand(
                id = UUID.randomUUID(),
                name = "MARCA X"
            )
        )

        // When
        val newProduct = DimensionProductAdapter(dimensionBrandRepositoryMockk).adaptNewBrand(
            product
        )

        // Then
        assert(newProduct.id == productID)
        assert(newProduct.brand.name == "Outra Marca")
        assert(newProduct.brand.id == DimensionBrandRepository.ID_OUTRA_MARCA)
    }

}