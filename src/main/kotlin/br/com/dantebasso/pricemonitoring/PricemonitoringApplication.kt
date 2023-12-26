package br.com.dantebasso.pricemonitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PricemonitoringApplication

fun main(args: Array<String>) {
	runApplication<PricemonitoringApplication>(*args)
}
