package br.com.dantebasso.pricemonitoring.service.mail

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*


@Service
class EmailServiceSender @Autowired constructor(
    @Value("\${notification.email.destiny}")
    private val emailDestiny: String,
    private val emailSender: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(EmailServiceSender::class.java)

    fun sendEmail() {

    }

    fun sendNotificationEmail() {
        try {
            val message = SimpleMailMessage()
            message.setTo("dcbasso@gmail.com")
            message.subject = "teste"
            message.text = "Processado com sucesso"
            message.from = "notification@dantebasso.com.br"
            message.replyTo = "notification@dantebasso.com.br"
            message.sentDate = Date()
//            message.to = arrayOf("dcbasso@gmail.com")
            emailSender.send(message)
        } catch (e: Exception) {
           logger.error(e.message)
        }
    }


}