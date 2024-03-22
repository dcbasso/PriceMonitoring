package br.com.dantebasso.pricemonitoring.capture.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jobs")
class JobConfig {

    @Value("\${jobs.enabled}")
    var enabled: Boolean = false

    @Value("\${jobs.stores.list}")
    lateinit var jobStoresList: List<String>

    @Value("\${jobs.extras.list}")
    lateinit var jobExtrasList: List<String>

    @Value("\${jobs.database-update.enabled}")
    var databaseUpdateEnabled: Boolean = false

    fun isStoreJobEnabled(jobName: String): Boolean {
        return jobStoresList.any { it.equals(jobName, ignoreCase = true) }
    }

    fun isExtraJobEnabled(jobName: String): Boolean {
        return jobExtrasList.any { it.equals(jobName, ignoreCase = true) }
    }

}