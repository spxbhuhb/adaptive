/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.conf

import `fun`.adaptive.xlsx.model.XlsxCellFormat
import kotlinx.datetime.TimeZone

/**
 * Xlsx customizable settings holder
 */
data class XlsxConfiguration(

    /**
     * Numeric formats holder
     *
     * creating new format:
     *
     *  val customFormat1 = formats.newCustomNumberFormat(formatCode)
     *  val customFormat2 = formats.newBuiltInNumberFormat(numFmtId)
     *
     */
    val formats: XlsxFormats = XlsxFormats(),

    /**
     *  Timezone for Instant object
     *
     *  default: TimeZone.currentSystemDefault()
     */
    var timeZone: TimeZone = TimeZone.currentSystemDefault(),

    /**
     *  true if Enums localized by strings
     *  default: false
     */
    var localizedEnums: Boolean = false,

    /**
     *  true if Booleans localized by strings
     *
     *  default: false
     */
    var localizedBooleans: Boolean = false,

    /**
     *  date format for LocalDate
     *
     *  default: formats.BUILT_IN_DATE
     */
    var dateFormat: XlsxCellFormat = formats.BUILT_IN_DATE,

    /**
     *  date format for LocalDateTime
     *
     *  default: formats.BUILT_IN_DATETIME
     */
    var dateTimeFormat: XlsxCellFormat = formats.BUILT_IN_DATETIME,

    /**
     *  date format for Instant
     *
     *  default: formats.ISO_DATETIME_MILLISEC
     */
    var instantFormat: XlsxCellFormat = formats.ISO_DATETIME_MILLISEC

)