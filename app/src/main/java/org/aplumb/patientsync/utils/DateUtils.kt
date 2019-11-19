package org.aplumb.patientsync.utils

import java.text.SimpleDateFormat
import java.util.*

val isoDateTimeFormat by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US) }

private val _heartRateTimeDisplayFormat by lazy { LocaleMemoizedFormat("d/M/yyyy 'at' h:mm aaa") }
val heartRateTimeDisplayFormat
    get() = _heartRateTimeDisplayFormat.format

fun String.fromIsoToDate(): Date? {
    return isoDateTimeFormat.parse(this)
}

private class LocaleMemoizedFormat(val pattern: String) {
    private var _lastLocale: Locale? = null
    private var _lastFormat: SimpleDateFormat? = null

    val format: SimpleDateFormat
        get() {
            val locale = Locale.getDefault()
            if (locale != _lastLocale) {
                _lastLocale = locale
                _lastFormat = null
            }
            return _lastFormat
                ?: SimpleDateFormat(pattern, locale)
                    .apply { _lastFormat = this }
        }
}