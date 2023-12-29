package br.com.dantebasso.pricemonitoring.models.bi.enums

enum class Currency(val code: String) {
    AMERICAN_DOLLAR("USD"),
    BRAZILIAN_REAL("BRL"),
    ARGENTINE_PESO("ARS"),
    JAPANESE_YEN("JPY"),
    PARAGUAYAN_GUARANI("PYG"),
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    CHINESE_YUAN("CNY"),
    EURO("EUR");

    fun Currency.getCode(): String {
        return this.code
    }

    companion object {

        fun fromCode(code: String): Currency? {
            return values().find { it.code == code }
        }

        fun getCode(currency: Currency): String {
            return currency.code
        }
    }
}
