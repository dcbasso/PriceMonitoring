package br.com.dantebasso.pricemonitoring.processor

interface LineProcessor {
    fun processLine(line: String)
}