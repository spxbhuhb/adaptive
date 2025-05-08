package my.project.example.api

import my.project.example.model.ExampleData
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface ExampleApi {

    suspend fun getExampleData() : ExampleData

    suspend fun saveExampleData(data: ExampleData)

}