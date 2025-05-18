package `fun`.adaptive.persistence

import `fun`.adaptive.foundation.unsupported
import kotlinx.io.files.Path

actual fun Path.getRandomAccess(mode : String): RandomAccessPersistence {
    unsupported()
}