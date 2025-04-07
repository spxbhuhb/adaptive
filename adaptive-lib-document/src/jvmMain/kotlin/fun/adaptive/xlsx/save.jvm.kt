/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.xlsx

import `fun`.adaptive.utility.write
import `fun`.adaptive.xlsx.model.XlsxDocument
import kotlinx.io.files.Path

actual suspend fun XlsxDocument.save(path: Path) {
    path.write(pack())
}