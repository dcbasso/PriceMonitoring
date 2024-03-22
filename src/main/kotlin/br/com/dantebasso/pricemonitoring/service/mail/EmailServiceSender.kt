package br.com.dantebasso.pricemonitoring.service.mail

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class EmailServiceSender @Autowired constructor(
    @Value("\${notification.email.enabled}")
    private val emailEnabled: Boolean,
    @Value("\${notification.email.destiny}")
    private val emailDestiny: String,
    @Value("\${notification.email.identification}")
    private val emailIdentification: String,
    @Value("\${spring.mail.properties.mail.smtp.from}")
    private val emailFrom: String,
    private val emailSender: JavaMailSender
) {

    companion object {
        private const val LANGUAGE = "pt-BR"
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val DAY_WEEK_FORMAT = "EEEE"
        private const val TIME_FORMAT = "HH:mm:ss"
    }

    private val logger = LoggerFactory.getLogger(EmailServiceSender::class.java)

    fun sendEmail(subject: String, text: String) {
        if (emailEnabled) {
            logger.info("Email notification is enabled, sending email...")
            try {
                val message = SimpleMailMessage()
                message.setTo(emailDestiny)
                message.subject = subject
                message.text = text
                message.from = emailFrom
                message.replyTo = emailFrom
                message.sentDate = Date()
                emailSender.send(message)
            } catch (e: Exception) {
                logger.error(e.message)
            }
        } else {
            logger.info("Email notification is disabled")
        }
    }

    fun sendNotificationOfFinishedTheJobProcessWithSuccess(jobName: String) {
        val localDateTime = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern(TIME_FORMAT)
        val formatterDate = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.forLanguageTag(LANGUAGE))
        val formatterDayWeek = DateTimeFormatter.ofPattern(DAY_WEEK_FORMAT, Locale.forLanguageTag(LANGUAGE))
        val subject = "Successful Processed ($jobName | ${localDateTime.format(formatterDayWeek)})"
        val text = "Job: $jobName" +
                "\nIdentification: $emailIdentification" +
                "\nDate: ${localDateTime.format(formatterDate)}" +
                "\nDay: ${localDateTime.format(formatterDayWeek)}" +
                "\nTime: ${localDateTime.format(formatterTime)}"
        sendEmail(subject, text)
    }

    fun sendNotificationOfFinishedTheJobProcessWithSuccess(jobName: String, message: String) {
        val localDateTime = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern(TIME_FORMAT)
        val formatterDate = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.forLanguageTag(LANGUAGE))
        val formatterDayWeek = DateTimeFormatter.ofPattern(DAY_WEEK_FORMAT, Locale.forLanguageTag(LANGUAGE))
        val subject = "Successful Processed ($jobName | ${localDateTime.format(formatterDayWeek)})"
        val text = "Job: $jobName" +
                "\nIdentification: $emailIdentification" +
                "\nDate: ${localDateTime.format(formatterDate)}" +
                "\nDay: ${localDateTime.format(formatterDayWeek)}" +
                "\nTime: ${localDateTime.format(formatterTime)}" +
                "\nMessage: $message"
        sendEmail(subject, text)
    }

}