package `fun`.adaptive.iot.domain.rht.ui.controller

import `fun`.adaptive.iot.generated.resources.active
import `fun`.adaptive.iot.generated.resources.alarm
import `fun`.adaptive.iot.generated.resources.all
import `fun`.adaptive.iot.generated.resources.down
import `fun`.adaptive.iot.generated.resources.ok
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.filter.QuickFilterModel
import `fun`.adaptive.value.AvValue

data class RhtFilter(
    val search : String = "",
    val qf1 : QuickFilterModel<String> = QuickFilterModel(Strings.all, listOf(Strings.all, Strings.active, Strings.down), { it }),
    val qf2 : QuickFilterModel<String> = QuickFilterModel(Strings.all, listOf(Strings.all, Strings.ok, Strings.alarm), { it })
) {

    fun filter(items: List<AvValue<*>>): List<AvValue<*>> =
        items.filter { matches(it) }

    fun matches(item: AvValue<*>): Boolean {

        when (qf1.selected) {
            Strings.active -> if (!item.status.isActive) return false
            Strings.down -> if (!item.status.isDown) return false
        }

        when (qf2.selected) {
            Strings.ok -> if (!item.status.isOk) return false
            Strings.alarm -> if (!item.status.isAlarm) return false
        }

        if (search.isEmpty()) return true
        val lcSearch = search.lowercase()

        if (lcSearch in item.name.lowercase()) return true
        if (lcSearch in item.friendlyId.lowercase()) return true

        return false
    }

}