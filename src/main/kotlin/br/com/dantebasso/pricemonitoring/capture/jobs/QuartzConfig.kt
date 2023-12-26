package br.com.dantebasso.pricemonitoring.capture.jobs

import org.quartz.JobDetail
import org.quartz.SimpleTrigger
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
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
        factoryBean.setName("visaoVipTrigger")
        return factoryBean
    }
}

