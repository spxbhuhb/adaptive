package `fun`.adaptive.file

import `fun`.adaptive.foundation.unsupported
import kotlinx.io.files.Path

actual fun Path.getRandomAccess(mode : String): RandomAccessFile {
    unsupported()
}