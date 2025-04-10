package `fun`.adaptive.resource

import kotlinx.io.files.Path

class TestResourceReader(
    val readFun: suspend (String) -> ByteArray
) : ResourceReader {

    override suspend fun read(path: String): ByteArray =
        readFun(path)

    override suspend fun readPart(path: String, offset: Long, size: Long): ByteArray {
        TODO("Not yet implemented")
    }

    override fun getUri(path: String): String {
        return path
    }

    override fun sizeAndLastModified(path: Path): ResourceMetadata {
        TODO("Not yet implemented")
    }

    override fun setFileModificationTime(path: Path, timestamp: Long) {
        TODO("Not yet implemented")
    }
}