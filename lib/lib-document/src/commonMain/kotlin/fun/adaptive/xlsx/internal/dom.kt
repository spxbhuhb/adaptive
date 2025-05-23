/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal

import `fun`.adaptive.xlsx.internal.dom.Node
import `fun`.adaptive.xlsx.internal.dom.toXml
import `fun`.adaptive.xlsx.internal.model.*
import `fun`.adaptive.xlsx.model.XlsxCell
import `fun`.adaptive.xlsx.model.XlsxDocument
import `fun`.adaptive.xlsx.model.XlsxSheet
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/*
 * This file contains internal conversion functions from public model to internal dom model
 */

private fun XlsxCell.toDom(sharedStrings: SharedStrings): Cell? {

    val conf = sheet.doc.conf
    val timeZone = conf.timeZone
    val coord = coordinate.coordinate

    return when (val v = value) {
        null -> null
        is String -> {
            val sharedStringId = sharedStrings.addString(v)
            Cell(coord, sharedStringId, Cell.Type.SHARED_STRING, numberFormat.xfId)
        }

        is Enum<*> -> {
            val str = v.name
            val sharedStringId = sharedStrings.addString(str)
            Cell(coord, sharedStringId, Cell.Type.SHARED_STRING, numberFormat.xfId)
        }

        is Boolean -> {
            Cell(coord, if (v) "1" else "0", Cell.Type.BOOLEAN, numberFormat.xfId)
        }

        is Number -> Cell(coord, v, Cell.Type.NORMAL, numberFormat.xfId)
        is LocalDate -> Cell(coord, v.toInternal(), Cell.Type.NORMAL, numberFormat.xfId)
        is LocalDateTime -> Cell(coord, v.toInternal(), Cell.Type.NORMAL, numberFormat.xfId)
        is Instant -> Cell(coord, v.toInternal(timeZone), Cell.Type.NORMAL, numberFormat.xfId)
        else -> Cell(coord, v, Cell.Type.STRING, numberFormat.xfId)
    }
}

private fun XlsxSheet.toDom(sheetId: Int, sharedStrings: SharedStrings): WorkSheet {
    val ws = WorkSheet(sheetId)

    columns.data.forEach { (cn, column) ->
        column.width?.let { ws.addColumnWidth(cn, it) }
    }

    var row: Row? = null

    cells.forEach { xc ->
        val cell = xc.toDom(sharedStrings) ?: return@forEach
        val rowNumber = xc.coordinate.rowNumber
        if (row?.rowNumber != rowNumber) {
            row = Row(rowNumber).apply(ws::addRow)
        }
        row.addCell(cell)
    }

    try {
        val dim = "${minColumnNumber.toColumnLetter()}$minRowNumber:${maxColumnNumber.toColumnLetter()}$maxRowNumber"
        ws.addDimension(dim)
    } catch (_: NoSuchElementException) {
        // empty data
    }

    ws.clean()

    return ws
}

internal fun XlsxDocument.toXlsxFile(): XlsxFile {
    val f = XlsxFile()

    conf.formats.numberFormats.forEach { format ->
        when (format) {
            is BuiltInNumberFormat -> f.styles.addCellXf(format.numFmtId)
            is CustomNumberFormat -> f.styles.addCustomNumFmt(format.formatCode)
        }
    }

    sheets.forEach { xs ->
        val sheetId = f.workBook.nextSheetId()
        val ws = xs.toDom(sheetId, f.sharedStrings)
        f.addWorkSheet(sheetId, xs.name, ws)
    }

    return f
}

private fun Part.content(append: Appender) {
    when (this) {
        is Node -> toXml(append)
        else -> throw IllegalStateException("Content not supported: ${this::class}")
    }
}

internal fun XlsxFile.toContentMap(): ContentMap {
    val cm = ContentMap()
    content.forEach {
        val path = it.partName.substringAfter('/')
        cm[path] = it::content
    }
    return cm
}

internal fun Node.putCount(): Int {
    this["count"] = size.toString()
    return size
}

