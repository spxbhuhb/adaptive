package `fun`.adaptive.file

import kotlinx.io.files.Path

expect fun Path.getRandomAccess(
    mode : String
) : RandomAccessFile