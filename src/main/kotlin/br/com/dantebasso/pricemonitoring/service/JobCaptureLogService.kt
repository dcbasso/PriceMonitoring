package br.com.dantebasso.pricemonitoring.service

import br.com.dantebasso.pricemonitoring.models.control.JobCaptureLog
import br.com.dantebasso.pricemonitoring.models.enums.JobProcessStatus
import br.com.dantebasso.pricemonitoring.repository.JobCaptureLogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class JobCaptureLogService @Autowired constructor(private val jobCaptureLogRepository: JobCaptureLogRepository)  {

    fun save(jobCaptureLog: JobCaptureLog): JobCaptureLog {
        return jobCaptureLogRepository.save(jobCaptureLog)
    }

    fun jobWasExecutedTodayAndWithSuccess(date: LocalDate, jobName: String): Boolean {
        return jobCaptureLogRepository.findByDateAndJobNameAndStatus(date, jobName, JobProcessStatus.JOB_SUCCESS) != null
    }

    fun jobWasExecutedTodayAndWithSuccess(jobName: String): Boolean {
        return jobWasExecutedTodayAndWithSuccess(LocalDate.now(), jobName)
    }

}