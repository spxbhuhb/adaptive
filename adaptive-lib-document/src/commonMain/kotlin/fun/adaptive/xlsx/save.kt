package `fun`.adaptive.xlsx

import `fun`.adaptive.xlsx.internal.pack
import `fun`.adaptive.xlsx.internal.toContentMap
import `fun`.adaptive.xlsx.internal.toXlsxFile
import `fun`.adaptive.xlsx.model.XlsxDocument
import kotlinx.io.files.Path

/**
 * Convert public model to internal dom, then pack it into a byte array.
 */
suspend fun XlsxDocument.pack(): ByteArray {
    return toXlsxFile().toContentMap().pack()
}

expect suspend fun XlsxDocument.save(path: String)