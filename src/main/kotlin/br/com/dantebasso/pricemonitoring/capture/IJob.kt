package br.com.dantebasso.pricemonitoring.capture

import org.springframework.stereotype.Service

@Service
interface IJob {

    fun execute()

    fun getJobName(): String

}