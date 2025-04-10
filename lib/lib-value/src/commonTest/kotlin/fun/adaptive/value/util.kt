package `fun`.adaptive.value

fun condition(valueId: AvValueId) = listOf(AvSubscribeCondition(valueId))
fun condition(marker: String) = listOf(AvSubscribeCondition(marker = marker))
