package br.com.dantebasso.pricemonitoring.capture

import br.com.dantebasso.pricemonitoring.processor.LineProcessor

open class Capture(private val lineProcessor: LineProcessor) {
    fun processLine(line: String) {
        lineProcessor.processLine(line)
    }
}