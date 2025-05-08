package my.project.example.server

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import my.project.example.api.ExampleApi
import my.project.example.model.ExampleData
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class ExampleService : ServiceImpl<ExampleService>(), ExampleApi {

    companion object {
        var lock = getLock()
        var exampleData = ExampleData("abc", 12, emptyList())
    }

    override suspend fun getExampleData(): ExampleData {
        publicAccess()
        return lock.use { exampleData }
    }

    override suspend fun saveExampleData(data: ExampleData) {
        ensureLoggedIn()
        lock.use { exampleData = data }
    }

}