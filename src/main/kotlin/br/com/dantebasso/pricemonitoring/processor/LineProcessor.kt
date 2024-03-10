package br.com.dantebasso.pricemonitoring.processor

import br.com.dantebasso.pricemonitoring.models.enums.LineProcessStatus

interface LineProcessor {
    fun processLine(line: String): LineProcessStatus
}