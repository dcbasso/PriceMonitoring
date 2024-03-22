package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.repository.DimensionBrandRepository
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DatabaseUpdateService @Autowired constructor(
    private val jobCaptureLogService: JobCaptureLogService,
    private val dimensionProductRepository: DimensionProductRepository,
    private val dimensionProductService: DimensionProductService
): IJob {

    private val logger = LoggerFactory.getLogger(DatabaseUpdateService::class.java)

    companion object {
        const val JOB_NAME = "DatabaseUpdate"
        private const val JOB_NAME_DESCRIPTION = "Database update job. Updates products with brand 'Outra Marca' to the correct brand."
    }

    override fun execute() {
        if (!jobCaptureLogService.jobWasExecutedTodayAndWithSuccess(JOB_NAME)) {
            val products = dimensionProductRepository.findByBrandId(DimensionBrandRepository.ID_OUTRA_MARCA)
            val countProductsUpdated = BigDecimal.ZERO
            products.forEach {
                logger.info("Updating brand of product: ${it.productName}")
                if (dimensionProductService.updateProductBrand(it).brand.id != DimensionBrandRepository.ID_OUTRA_MARCA) {
                    countProductsUpdated.add(BigDecimal.ONE)
                }
            }
            logger.info("Database Update Job executed successfully. Processed ${products.size} and updated $countProductsUpdated products.")
            jobCaptureLogService.save(
                JobCaptureLog(
                    date = LocalDate.now(),
                    dateTime = LocalDateTime.now(),
                    content = null,
                    curl = "",
                    status = JobProcessStatus.JOB_SUCCESS,
                    quantityOfItemsProcessed = products.size,
                    jobName = JOB_NAME,
                    message = "Database Update Job executed successfully. Processed ${products.size} and updated $countProductsUpdated products."
                )
            )
        } else {
            logger.info("Database Update Job already executed today. Skipping execution...")
        }
    }

    override fun getJobName() = JOB_NAME
}