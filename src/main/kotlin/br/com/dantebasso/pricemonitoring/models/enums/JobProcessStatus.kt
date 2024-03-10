package br.com.dantebasso.pricemonitoring.models.enums

enum class JobProcessStatus {
    JOB_SUCCESS,
    JOB_ERROR;

    companion object {
        fun fromString(value: String): JobProcessStatus? {
            return when (value) {
                "JOB_SUCCESS" -> JOB_SUCCESS
                "JOB_ERROR" -> JOB_ERROR
                else -> null
            }
        }
    }
}

fun JobProcessStatus.toStringValue(): String {
    return when (this) {
        JobProcessStatus.JOB_SUCCESS -> "LINE_PROCESSED"
        JobProcessStatus.JOB_ERROR -> "JOB_ERROR"
    }
}