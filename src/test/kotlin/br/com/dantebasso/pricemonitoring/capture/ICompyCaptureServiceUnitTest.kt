package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.capture.processor.ICompyLineProcessor
import br.com.dantebasso.pricemonitoring.service.JobCaptureLogService
import br.com.dantebasso.pricemonitoring.service.mail.EmailServiceSender
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ICompyCaptureServiceUnitTest {

    @RelaxedMockK
    lateinit var processorMockk: ICompyLineProcessor

    @RelaxedMockK
    lateinit var jobCaptureLogServiceMockk: JobCaptureLogService

    @RelaxedMockK
    lateinit var emailServiceSenderMockk: EmailServiceSender

    @Test
    fun requestToWebsiteShouldTryProcessTheDownloadedFile() {
        val toTest = ICompyCaptureService(
            processor = processorMockk,
            jobCaptureLogService = jobCaptureLogServiceMockk,
            emailServiceSender = emailServiceSenderMockk
        )
        toTest.capture()
        verify(atLeast = 1) { processorMockk.processLine(any()) }
        verify(exactly = 1) { jobCaptureLogServiceMockk.save(any()) }
        verify(exactly = 0) { emailServiceSenderMockk.sendNotificationOfFinishedTheJobProcessWithSuccess(any()) }
        verify(exactly = 1) { emailServiceSenderMockk.sendNotificationOfFinishedTheJobProcessWithSuccess(any(), any()) }
    }

}