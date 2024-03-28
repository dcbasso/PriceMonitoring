package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.ICapture
import br.com.dantebasso.pricemonitoring.capture.ICompyCaptureService
import br.com.dantebasso.pricemonitoring.capture.TopdekCaptureService
import br.com.dantebasso.pricemonitoring.capture.VisaoVipCaptureService
import br.com.dantebasso.pricemonitoring.capture.config.JobConfig
import org.quartz.DisallowConcurrentExecution
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class CaptureStoreJobs @Autowired constructor(
    private val visaoVipCaptureService: VisaoVipCaptureService,
    private val topdekCaptureService: TopdekCaptureService,
    private val icompyCaptureService: ICompyCaptureService,
    private val jobConfig: JobConfig
) {
    private val logger = LoggerFactory.getLogger(CaptureStoreJobs::class.java)

    @Scheduled(cron = "\${capture.cron.scheduled}")
    fun execute() {
        if (!jobConfig.enabled) {
            logger.info("Jobs are disabled. Skipping execution...")
            return
        }
        val map = HashMap<String, ICapture>()
        map[VisaoVipCaptureService.JOB_NAME.lowercase()] = visaoVipCaptureService
        map[TopdekCaptureService.JOB_NAME.lowercase()] = topdekCaptureService
        map[ICompyCaptureService.JOB_NAME.lowercase()] = icompyCaptureService

        jobConfig.jobStoresList.forEach {
            if (jobConfig.isStoreJobEnabled(it)) {
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
