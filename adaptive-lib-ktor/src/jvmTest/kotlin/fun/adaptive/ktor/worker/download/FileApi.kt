package `fun`.adaptive.ktor.worker.download

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface FileApi {

    suspend fun download() : String

}