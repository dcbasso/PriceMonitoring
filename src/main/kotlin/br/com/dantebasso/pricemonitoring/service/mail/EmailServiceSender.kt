package br.com.dantebasso.pricemonitoring.service.mail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class EmailServiceSender @Autowired constructor(
    @Value("\${notification.email.destiny}")
    private val emailDestiny: String,
    private val emailSender: JavaMailSender
) {

    fun sendEmail() {

    }

    fun sendNotificationEmail() {
        val message = SimpleMailMessage()
        message.setTo(emailDestiny)
        message.setSubject("teste")
        message.setText("Processado com sucesso")
        emailSender.send(message)
    }


}