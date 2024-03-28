package br.com.dantebasso.pricemonitoring.capture.processor

import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus

interface LineProcessor {

    fun processLine(line: String): LineProcessStatus

    fun getStoreName(): String

}