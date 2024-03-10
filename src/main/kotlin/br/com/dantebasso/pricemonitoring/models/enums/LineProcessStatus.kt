package br.com.dantebasso.pricemonitoring.models.enums

enum class LineProcessStatus {
    LINE_PROCESSED,
    LINE_ERROR,
    LINE_IGNORED;

    companion object {
        fun fromString(value: String): LineProcessStatus? {
            return when (value) {
                "LINE_PROCESSED" -> LINE_PROCESSED
                "LINE_ERROR" -> LINE_ERROR
                "LINE_IGNORED" -> LINE_IGNORED
                else -> null
            }
        }
    }
}

fun LineProcessStatus.toStringValue(): String {
    return when (this) {
        LineProcessStatus.LINE_PROCESSED -> "LINE_PROCESSED"
        LineProcessStatus.LINE_ERROR -> "LINE_ERROR"
        LineProcessStatus.LINE_IGNORED -> "LINE_IGNORED"
    }
}