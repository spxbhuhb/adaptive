/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.xlsx

import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.xlsx.model.XlsxDocument
import kotlinx.io.files.Path

actual suspend fun XlsxDocument.save(path: Path) {
    downloadFile(
        pack(),
        path.name,
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    )
}