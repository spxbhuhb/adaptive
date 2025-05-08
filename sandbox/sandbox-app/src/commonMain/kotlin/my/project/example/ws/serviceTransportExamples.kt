package my.project.example.ws

import my.project.example.api.ExampleApi
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.service.api.getService


@Adaptive
fun exampleFetchFun() {
    val data = fetch { getService<ExampleApi>(adapter().transport).getExampleData() }
}

@Adaptive
fun examplePollFun() {
    val data = fetch { getService<ExampleApi>(adapter().transport).getExampleData() }
}