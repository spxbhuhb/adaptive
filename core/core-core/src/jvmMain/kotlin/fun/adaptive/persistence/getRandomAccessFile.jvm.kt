package `fun`.adaptive.persistence

import kotlinx.io.EOFException
import kotlinx.io.files.Path
import java.io.RandomAccessFile

actual fun Path.getRandomAccess(mode: String): RandomAccessPersistence =
    JvmRandomAccessPersistenceProxy(this, mode)

private class JvmRandomAccessPersistenceProxy(
    val path: Path,
    mode: String
) : RandomAccessPersistence() {

    val impl = RandomAccessFile(path.toString(), mode)

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

    override fun setPosition(position: Long) {
        impl.seek(position)
    }

    override fun getPosition(): Long =
        impl.filePointer

    override fun getSize(): Long =
        impl.length()

    override fun setSize(size: Long) {
        impl.setLength(size)
    }

    override fun close() {
        impl.close()
    }

    override fun delete() {
        impl.close()
        path.delete()
    }

}