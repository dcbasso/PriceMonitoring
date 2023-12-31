package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.CurrencyQuoteCaptureService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CurrencyQuoteJob @Autowired constructor(
    private val currencyQuoteCaptureService: CurrencyQuoteCaptureService
) : Job {

    private val logger = LoggerFactory.getLogger(CurrencyQuoteJob::class.java)

    override fun execute(context: JobExecutionContext) {
        logger.info("Executing CurrencyQuoteJob...")
        currencyQuoteCaptureService.capture()
        logger.info("Execution CurrencyQuoteJob Done...")
    }
}
