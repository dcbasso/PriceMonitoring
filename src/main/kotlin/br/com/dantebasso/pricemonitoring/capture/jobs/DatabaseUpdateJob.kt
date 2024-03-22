package br.com.dantebasso.pricemonitoring.capture.jobs

import br.com.dantebasso.pricemonitoring.capture.DatabaseUpdateService
import br.com.dantebasso.pricemonitoring.capture.config.JobConfig
import org.quartz.DisallowConcurrentExecution
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class DatabaseUpdateJob @Autowired constructor(
    private val databaseUpdateService: DatabaseUpdateService,
    private val jobConfig: JobConfig
) {

    private val logger = LoggerFactory.getLogger(DatabaseUpdateJob::class.java)

    @Scheduled(cron = "\${capture.cron.scheduled}")
    fun execute() {
        if (!jobConfig.enabled || !jobConfig.databaseUpdateEnabled) {
            logger.info("Job Database Update is disabled. Skipping execution...")
            return
        }
        logger.info("Executing Database Update Job...")
        databaseUpdateService.execute()
        logger.info("Executing Database Update Job...")
    }
}
