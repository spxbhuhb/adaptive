/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal.model

internal class XlsxFile {

    val content = mutableListOf<Part>()

    val contentType = + ContentType()
    val rels = + Rels()
    val workBookRels = + Rels("/xl/", "workbook.xml")
    val workBook = + WorkBook()
    val sharedStrings = + SharedStrings()
    val styles = + Styles()

    init {

        contentType.addPart(workBook)
        contentType.addPart(styles)
        contentType.addPart(sharedStrings)

        rels.addRel(workBook)

        workBookRels.addRel(styles)
        workBookRels.addRel(sharedStrings)

    }

    fun addWorkSheet(sheetId: Int, name: String, ws: WorkSheet) {
        val workBookRelId = workBookRels.addRel(ws)
        workBook.addSheet(sheetId, workBookRelId, name)
        contentType.addPart(ws)
        content.add(ws)
    }

    private operator fun <T : Part> T.unaryPlus(): T = also(content::add)

}
