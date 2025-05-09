package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

@Adat
class AvValue<T>(
    override val name: String,
    override val type: NamedItemType,
    val uuid: AvValueId = uuid7(),
    val timestamp: Instant = Clock.System.now(),
    val statusOrNull: Set<AvStatus>? = null,
    val markersOrNull: Set<AvMarker>? = null,
    val refsOrNull: Map<AvRefLabel, AvValueId>? = null,
    val parentId: AvValueId? = null,
    val friendlyId: FriendlyItemId? = null,
    val spec: T
) : NamedItem() {

    val status get() = statusOrNull ?: emptySet()
    fun toMutableStatus() = statusOrNull?.toMutableSet() ?: mutableSetOf()

    val markers get() = markersOrNull ?: emptySet()
    fun toMutableMarkers() = markersOrNull?.toMutableSet() ?: mutableSetOf()

    val refs get() = refsOrNull ?: emptyMap()
    fun toMutableRefs() = refsOrNull?.toMutableMap() ?: mutableMapOf()


//
//    operator fun contains(markerName: AvMarker): Boolean {
//        return markersOrNull?.containsKey(markerName) == true
//    }
//
//    operator fun get(markerName: AvMarker): AvValueId? {
//        return markersOrNull?.get(markerName)
//    }
//
//    fun copyWith(value: AvMarkerValue): AvValue<T> {
//        return copyWith(value.markerName, value)
//    }
//
//    fun copyWith(key: AvMarker, value: AvMarkerValue?): AvValue<T> {
//        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
//        markers[key] = value?.uuid
//        return copy(markersOrNull = markers)
//    }

    override fun toString(): String {
        return name
    }

    companion object {

        inline fun <reified T> Any.asAvValue(): AvValue<T> {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvValue<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            check(spec is T)

            return this
        }

        inline fun <reified T> Any.asAvValueOrNull(): AvValue<T>? {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvValue<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            if (spec !is T) return null

            return this
        }

        inline fun <reified T : Any> AvValue<*>.withSpec(): AvValue<T> =
            withSpec(T::class)

        fun <T : Any> AvValue<*>.withSpec(kClass: KClass<T>): AvValue<T> {
            check(kClass.isInstance(spec)) { "Spec is not of type $kClass for item $this" }
            @Suppress("UNCHECKED_CAST")
            return this as AvValue<T>
        }
    }

}