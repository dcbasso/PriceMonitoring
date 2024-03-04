package br.com.dantebasso.pricemonitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PricemonitoringApplication

fun main(args: Array<String>) {
	runApplication<PricemonitoringApplication>(*args)
}
