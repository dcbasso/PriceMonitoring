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

import org.jsoup.Jsoup
import java.net.URL

@Component
class VisaoVipCaptureService @Autowired constructor(
    private val processor: VisaoVipLineProcessor
): ICapture {

    private val logger = LoggerFactory.getLogger(VisaoVipCaptureService::class.java)

    companion object {
        const val URL_VISAOVIP = "https://www.visaovip.com"
        const val XPATH_FIELD_VIEW_STATE_ELEMENT = "//*[@id=\"j_id1:javax.faces.ViewState:2\"]"
        const val XPATH_FIELD_NAME_ELEMENT = "//*[@id=\"form-lista-preco\"]"
        const val XPATH_BUTTON_ELEMENT = "//*[@class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left ui-button-flat botao-lista-preco\"]"
        const val ATTR_FIELD_VIEW = "value"
        const val ATTR_FIELD_NAME = "name"
        const val ATTR_FIELD_ID = "id"
    }

    override fun capture() {
        val restTemplate = RestTemplate()
        val requestData = captureInfo()

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
        headers.set("Origin", "https://visaovip.com")
        headers.set("Referer", "https://visaovip.com/")
        headers.set("Sec-Fetch-Dest", "document")
        headers.set("Sec-Fetch-Mode", "navigate")
        headers.set("Sec-Fetch-Site", "same-origin")
        headers.set("Sec-Fetch-User", "?1")
        headers.set("Upgrade-Insecure-Requests", "1")
        headers.set(
            "User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
        headers.set("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
        headers.set("sec-ch-ua-mobile", "?0")
        headers.set("sec-ch-ua-platform", "\"macOS\"")
        headers.set(
            "Cookie",
            "_fbp=fb.1.1676474393978.1698996418; primefaces.download_index.xhtml=true; _ga_2J4J87E94X=GS1.1.1691776093.15.0.1691776103.50.0.0; _gcl_au=1.1.1040101341.1699293899; _ga=GA1.1.1546199250.1676474392; ${requestData.cookies.get(0)}; ${requestData.cookies.get(1)}; _ga_ET2JLJZ642=GS1.1.1703795403.89.1.1703795404.59.0.0"
        )
        val body = "form-lista-preco=${requestData.fieldNameValue}&${requestData.fieldButtonValue}=&javax.faces.ViewState=${requestData.fieldViewValue}"
        val entity = HttpEntity(body, headers)
        val responseEntity = restTemplate.exchange(
            URL_VISAOVIP,
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
            logger.error("Failure to download file, HTTP STATUS: ${responseEntity.statusCode}")
        }
    }

    private fun captureInfo(): RequestData {
        val connection = URL(URL_VISAOVIP).openConnection()
        val html = connection.getInputStream().bufferedReader().use { it.readText() }
        val document = Jsoup.parse(html)

        val cookies = connection.getHeaderFields()
            .flatMap { entry ->
                if (entry.key != null && entry.key.equals("Set-Cookie", ignoreCase = true)) {
                    entry.value
                } else {
                    emptyList()
                }
            }
            .map { cookie ->
                cookie.split(";\\s*".toRegex())
                    .first() // Pegando apenas o valor do cookie, ignorando os parâmetros adicionais
            }

        val fieldViewStateElement = document.selectXpath(XPATH_FIELD_VIEW_STATE_ELEMENT)
        val fieldNameElement = document.selectXpath(XPATH_FIELD_NAME_ELEMENT)
        val fieldButtonElement = document.selectXpath(XPATH_BUTTON_ELEMENT)

        val fieldViewValue = fieldViewStateElement.attr(ATTR_FIELD_VIEW)
        val fieldNameValue = fieldNameElement.attr(ATTR_FIELD_NAME)
        val fieldButtonValue = fieldButtonElement.attr(ATTR_FIELD_ID)

        return RequestData(
            fieldButtonValue = fieldButtonValue,
            fieldNameValue = fieldNameValue,
            fieldViewValue = fieldViewValue,
            cookies = cookies
        )
    }

    inner class RequestData(
        val fieldViewValue: String,
        val fieldNameValue: String,
        val fieldButtonValue: String,
        val cookies: List<String>
    ) {}

}