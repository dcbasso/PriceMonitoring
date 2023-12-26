package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.VisaoVipCapture
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VisaoVipJob @Autowired constructor(
    private val visaoVipCapture: VisaoVipCapture
) : Job {

    private val logger = LoggerFactory.getLogger(VisaoVipJob::class.java)

    override fun execute(context: JobExecutionContext) {
        logger.info("Executing VisaoVipJob...")

        visaoVipCapture.capture()
        logger.info("Execution VisaoVipJob Done...")
    }
}
