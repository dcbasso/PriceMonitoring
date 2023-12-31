package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.VisaoVipCaptureService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class VisaoVipJob @Autowired constructor(
    private val visaoVipCaptureService: VisaoVipCaptureService
) : Job {

    private val logger = LoggerFactory.getLogger(VisaoVipJob::class.java)

    override fun execute(context: JobExecutionContext) {
        logger.info("Executing VisaoVipJob...")
        visaoVipCaptureService.capture()
        logger.info("Execution VisaoVipJob Done...")
    }
}
