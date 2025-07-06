package `fun`.adaptive.grove.doc.api

import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface GroveDocApi {

    suspend fun getByPath(path: List<String>): GroveDocValue?

    suspend fun getExampleGroup(group : String) : List<GroveDocExample>

    suspend fun getDefinition(name : String) : GroveDocValue?

}