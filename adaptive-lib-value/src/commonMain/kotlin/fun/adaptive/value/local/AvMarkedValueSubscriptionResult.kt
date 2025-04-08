package `fun`.adaptive.value.local

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId

@Adat
class AvMarkedValueSubscriptionResult(
    val conditions: List<AvSubscribeCondition>,
    val mapping : Map<AvValueId, List<AvMarkedValueId>>
)