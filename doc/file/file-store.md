# File Store

* common file handling on all platforms
* automatic synchronization with server/cloud

## API

File stores implement the `AdaptiveFileStore` interface:

```kotlin
interface AdaptiveFileStore {

    val storeId: UUID<AdaptiveFileStore>

    val platformPath: String

    suspend fun createFolder(path: String)

    suspend fun moveFolder(currentPath: String, newPath: String)

    suspend fun deleteFolder(path: String)

    suspend fun listFolders(path: String): List<String>

    suspend fun listFiles(path: String): List<String>

    suspend fun writeFile(path: String, byteArray: ByteArray)

    suspend fun writeFile(path: String, sourcePath: String, mode: FileWriteMode)

    suspend fun readFile(path: String): ByteArray

    suspend fun moveFile(curPath: String, newPath: String)

    suspend fun deleteFile(path: String)

}
```

