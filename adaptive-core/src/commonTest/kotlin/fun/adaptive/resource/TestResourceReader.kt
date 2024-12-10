package `fun`.adaptive.resource

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
}