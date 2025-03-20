package `fun`.adaptive.iot.value

fun condition(valueId: AioValueId) = listOf(AioSubscribeCondition(valueId))
fun condition(marker: String) = listOf(AioSubscribeCondition(marker = marker))
