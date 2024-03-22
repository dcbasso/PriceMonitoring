package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.bi.DimensionBrand
import br.com.dantebasso.pricemonitoring.models.bi.DimensionProduct
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import br.com.dantebasso.pricemonitoring.service.mail.EmailServiceSender
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class DatabaseUpdateServiceUnitTest {

    @RelaxedMockK
    lateinit var dimensionProductRepositoryMockk: DimensionProductRepository

    @RelaxedMockK
    lateinit var dimensionProductServiceMockk: DimensionProductService

    @RelaxedMockK
    lateinit var jobCaptureLogServiceMockk: JobCaptureLogService

    @RelaxedMockK
    lateinit var emailServiceSenderMockk: EmailServiceSender

    @Test
    fun requestToWebsiteShouldTryProcessTheDownloadedFile() {

        every { dimensionProductServiceMockk.updateProductBrand(any()) } returns DimensionProduct(
            id = UUID.randomUUID(),
            category = "Category 1",
            masterCategory = "Master Category 1",
            productName = "Product 1",
            uniqueProductCode = "123456",
            brand = DimensionBrand(
                id = UUID.randomUUID(),
                name = "Marca X"
            )
        )

        every { dimensionProductRepositoryMockk.findByBrandId(any()) } returns listOf(
            DimensionProduct(
                id = UUID.randomUUID(),
                category = "Category 1",
                masterCategory = "Master Category 1",
                productName = "Product 1",
                uniqueProductCode = "123456",
                brand = DimensionBrand(
                    id = UUID.randomUUID(),
                    name = "Outra Marca"
                )
            ),
        )

        val toTest = DatabaseUpdateService(
            jobCaptureLogService = jobCaptureLogServiceMockk,
            dimensionProductRepository = dimensionProductRepositoryMockk,
            dimensionProductService = dimensionProductServiceMockk,
            emailServiceSender = emailServiceSenderMockk
        )
        toTest.execute()
        verify(atLeast = 1) { dimensionProductServiceMockk.updateProductBrand(any()) }
        verify(atLeast = 1) { dimensionProductRepositoryMockk.findByBrandId(any()) }
        verify(exactly = 1) { jobCaptureLogServiceMockk.save(any()) }
        verify(exactly = 1) { emailServiceSenderMockk.sendNotificationOfFinishedTheJobProcessWithSuccess(any(), any()) }
    }

}