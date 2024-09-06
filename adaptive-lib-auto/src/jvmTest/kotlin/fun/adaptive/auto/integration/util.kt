package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

fun read(path: Path) =
    FileFrontend.read(path, Json).second as TestData

fun write(path: Path, itemId: ItemId, t: TestData) =
    FileFrontend.write(path, Json, itemId, t)

fun Path.ensureExistsAndEmpty() {
    if (exists()) {
        SystemFileSystem.list(this).forEach {
            require(it.name.endsWith(".json"))
            SystemFileSystem.delete(it)
        }
    } else {
        SystemFileSystem.createDirectories(this)
    }
}