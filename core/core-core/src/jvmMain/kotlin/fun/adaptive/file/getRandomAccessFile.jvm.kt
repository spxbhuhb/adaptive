package `fun`.adaptive.file

import kotlinx.io.EOFException
import kotlinx.io.files.Path

actual fun Path.getRandomAccess(mode: String): RandomAccessFile =
    JvmRandomAccessFileProxy(this, mode)

private class JvmRandomAccessFileProxy(
    path: Path,
    mode: String
) : RandomAccessFile() {

    val impl = java.io.RandomAccessFile(path.toString(), mode)

    override fun read(bytes: ByteArray, offset: Int, length: Int, exception: Boolean): Int {
        var bytesRead = 0
        var currentOffset = offset
        while (bytesRead < length) {
            val count = impl.read(bytes, currentOffset, length - bytesRead)
            if (count == - 1) if (exception) throw EOFException() else return bytesRead
            bytesRead += count
            currentOffset += count
        }
        return bytesRead
    }

    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        impl.write(bytes, offset, length)
    }

    override fun seek(position: Long) {
        impl.seek(position)
    }

    override fun getFilePointer(): Long =
        impl.filePointer

    override fun length(): Long =
        impl.length()

    override fun setLength(length: Long) {
        impl.setLength(length)
    }

    override fun close() {
        impl.close()
    }
    
}