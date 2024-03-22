package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.repository.DimensionBrandRepository
import br.com.dantebasso.pricemonitoring.repository.DimensionProductRepository
import br.com.dantebasso.pricemonitoring.service.DimensionProductService
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import br.com.dantebasso.pricemonitoring.service.mail.EmailServiceSender
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
    private val dimensionProductService: DimensionProductService,
    private val emailServiceSender: EmailServiceSender
): IJob {

    private val logger = LoggerFactory.getLogger(DatabaseUpdateService::class.java)

    companion object {
        const val JOB_NAME = "DatabaseUpdate"
        private const val JOB_NAME_DESCRIPTION = "Database update job. Updates products with brand 'Outra Marca' to the correct brand."
    }

    override fun execute() {
        if (!jobCaptureLogService.jobWasExecutedTodayAndWithSuccess(JOB_NAME)) {
            val products = dimensionProductRepository.findByBrandId(DimensionBrandRepository.ID_OUTRA_MARCA)
            val countProductsUpdated = products.count {
                logger.info("Updating brand of product: ${it.productName}")
                dimensionProductService.updateProductBrand(it).brand.id != DimensionBrandRepository.ID_OUTRA_MARCA
            }
            val message = "Database Update Job executed successfully. Processed ${products.size} and updated $countProductsUpdated products."
            logger.info(message)
            jobCaptureLogService.save(
                JobCaptureLog(
                    date = LocalDate.now(),
                    dateTime = LocalDateTime.now(),
                    content = null,
                    curl = "",
                    status = JobProcessStatus.JOB_SUCCESS,
                    quantityOfItemsProcessed = products.size,
                    jobName = JOB_NAME,
                    message = message
                )
            )
            emailServiceSender.sendNotificationOfFinishedTheJobProcessWithSuccess(JOB_NAME, message)
        } else {
            logger.info("Database Update Job already executed today. Skipping execution...")
        }
    }

    override fun getJobName() = JOB_NAME
}