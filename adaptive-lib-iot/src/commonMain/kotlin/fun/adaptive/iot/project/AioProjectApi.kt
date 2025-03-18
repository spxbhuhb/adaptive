package `fun`.adaptive.iot.project

import `fun`.adaptive.iot.project.model.AioProject
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface AioProjectApi {

    suspend fun create(): AioProject

}