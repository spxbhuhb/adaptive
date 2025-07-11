package `fun`.adaptive.example

import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport


suspend fun serviceConsumerExample(
    transport : ServiceCallTransport
) {

    val exampleService = getService<ExampleServiceApi>(transport)

    exampleService.getMessage()?.let { println(it) }

    exampleService.sendMessage("Hello World!")

}