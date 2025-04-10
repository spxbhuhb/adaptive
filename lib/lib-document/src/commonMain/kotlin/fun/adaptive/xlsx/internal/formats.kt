/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal

import `fun`.adaptive.xlsx.model.XlsxCellFormat

internal class BuiltInNumberFormat(xfId: Int, val numFmtId: Int) : XlsxCellFormat(xfId)

internal class CustomNumberFormat(xfId: Int, val formatCode: String) : XlsxCellFormat(xfId)
