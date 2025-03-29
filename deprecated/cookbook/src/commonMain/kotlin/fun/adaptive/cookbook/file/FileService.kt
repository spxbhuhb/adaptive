package `fun`.adaptive.cookbook.file

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.api.getDownloadPath
import `fun`.adaptive.utility.write

class FileService : ServiceImpl<FileService>, FileApi {

    override suspend fun download(): String {
        val path = getDownloadPath("a.txt")
        path.write("Hello World!")
        return path.name
    }

}