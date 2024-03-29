package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.CurrencyQuoteCaptureService
import br.com.dantebasso.pricemonitoring.capture.ICapture
import br.com.dantebasso.pricemonitoring.capture.config.JobConfig
import org.quartz.DisallowConcurrentExecution
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class CurrencyQuoteJob @Autowired constructor(
    private val currencyQuoteCaptureService: CurrencyQuoteCaptureService,
    private val jobConfig: JobConfig
) {

    private val logger = LoggerFactory.getLogger(CurrencyQuoteJob::class.java)

    @Scheduled(cron = "\${capture.cron.scheduled}")
    fun execute() {
        if (!jobConfig.enabled) {
            logger.info("Jobs are disabled. Skipping execution...")
            return
        }

        val map = HashMap<String, ICapture>()
        map[CurrencyQuoteCaptureService.JOB_NAME.lowercase()] = currencyQuoteCaptureService

        jobConfig.jobExtrasList.forEach {
            if (jobConfig.isExtraJobEnabled(it)) {
                val job = map[it.lowercase()]
                if (job != null) {
                    logger.info("Executing $it...")
                    job.capture()
                    logger.info("Execution $it Done...")
                } else {
                    logger.error("Execution $it not mapped. Skipping...")
                }
            } else {
                logger.info("Job $it is disabled. Skipping...")
            }
        }
    }
}
