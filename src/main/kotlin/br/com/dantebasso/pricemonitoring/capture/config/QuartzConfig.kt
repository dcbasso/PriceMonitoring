package br.com.dantebasso.pricemonitoring.capture.config

import br.com.dantebasso.pricemonitoring.capture.jobs.CurrencyQuoteJob
import br.com.dantebasso.pricemonitoring.capture.jobs.VisaoVipJob
import org.quartz.JobDetail
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.JobDetailFactoryBean
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean

@Configuration
class QuartzConfig {

    @Bean
    fun visaoVipJobDetail(): JobDetailFactoryBean {
        val factoryBean = JobDetailFactoryBean()
        factoryBean.setJobClass(VisaoVipJob::class.java)
        factoryBean.setName("visaoVipJob")
        factoryBean.setDurability(true)
        return factoryBean
    }

    @Bean
    fun visaoVipTrigger(visaoVipJobDetail: JobDetail): SimpleTriggerFactoryBean {
        val factoryBean = SimpleTriggerFactoryBean()
        factoryBean.setJobDetail(visaoVipJobDetail)
        factoryBean.setStartDelay(0)
        factoryBean.setRepeatInterval(20000) // Intervalo de 20 segundos (ajuste conforme necessário)
        factoryBean.setRepeatCount(0) //SimpleTrigger.REPEAT_INDEFINITELY
        factoryBean.setName("visaoVipTrigger")
        return factoryBean
    }

    @Bean
    fun currencyQuoteJobDetail(): JobDetailFactoryBean {
        val factoryBean = JobDetailFactoryBean()
        factoryBean.setJobClass(CurrencyQuoteJob::class.java)
        factoryBean.setName("currencyQuoteJob")
        factoryBean.setDurability(true)
        return factoryBean
    }

    @Bean
    fun currencyQuoteTrigger(currencyQuoteJobDetail: JobDetail): SimpleTriggerFactoryBean {
        val factoryBean = SimpleTriggerFactoryBean()
        factoryBean.setJobDetail(currencyQuoteJobDetail)
        factoryBean.setStartDelay(0)
        factoryBean.setRepeatInterval(20000) // Intervalo de 20 segundos (ajuste conforme necessário)
        factoryBean.setRepeatCount(0) // SimpleTrigger.REPEAT_INDEFINITELY
        factoryBean.setName("currencyQuoteTrigger")
        return factoryBean
    }

}
