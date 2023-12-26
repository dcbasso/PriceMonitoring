package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.processor.VisaoVipLineProcessor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class VisaoVipCapture @Autowired constructor(
    private val processor: VisaoVipLineProcessor
): ICapture {

    private val logger = LoggerFactory.getLogger(VisaoVipCapture::class.java)

    override fun capture() {
        val url = "https://visaovip.com/"
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.accept = listOf(
            MediaType.TEXT_HTML,
            MediaType.APPLICATION_XHTML_XML,
            MediaType.APPLICATION_XML,
            MediaType.parseMediaType("image/avif"),
            MediaType.parseMediaType("image/webp"),
            MediaType.parseMediaType("image/apng"),
            MediaType.ALL
        )
        headers.set("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
        headers.set("Cache-Control", "max-age=0")
        headers.set("Connection", "keep-alive")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Cookie", "_fbp=fb.1.1676474393978.1698996418; primefaces.download_index.xhtml=true; _ga_2J4J87E94X=GS1.1.1691776093.15.0.1691776103.50.0.0; _gcl_au=1.1.1040101341.1699293899; _ga=GA1.1.1546199250.1676474392; JSESSIONID=7ac7b9b581226590fdbd618e6101; X-Oracle-BMC-LBS-Route=b42d5bf7a0128f11bb7e55ed735c405e9f045e92fadbb75d74d28586d367676c3715a4107f3371b8; _ga_ET2JLJZ642=GS1.1.1703620148.80.0.1703620148.60.0.0")
        headers.set("Origin", "https://visaovip.com")
        headers.set("Referer", "https://visaovip.com/")
        headers.set("Sec-Fetch-Dest", "document")
        headers.set("Sec-Fetch-Mode", "navigate")
        headers.set("Sec-Fetch-Site", "same-origin")
        headers.set("Sec-Fetch-User", "?1")
        headers.set("Upgrade-Insecure-Requests", "1")
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        headers.set("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
        headers.set("sec-ch-ua-mobile", "?0")
        headers.set("sec-ch-ua-platform", "\"macOS\"")

        val body = "form-lista-preco=form-lista-preco&j_idt107=&javax.faces.ViewState=-3293888884515550799%3A2018774966923942912"

        val entity = HttpEntity(body, headers)

        val responseEntity = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String::class.java
        )

        if (responseEntity.statusCode.is2xxSuccessful) {
            val inputStream = responseEntity.body?.byteInputStream()
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    processor.processLine(line)
                }
            }

            reader.close()
        } else {
            logger.error("Falha ao baixar o arquivo. Código de status: ${responseEntity.statusCode}")
        }
    }

}