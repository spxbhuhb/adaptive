package `fun`.adaptive.cookbook.file

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface FileApi {

    suspend fun download() : String

}