package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.acl.AvAclAgent
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

/**
 * One value in a value store, uniquely identified by its [uuid].
 *
 * The [spec] property contains the actual domain-specific data, other properties
 * provide metadata.
 *
 * References (property [refsOrNull]) and markers (property [markersOrNull]) can be used
 * to build complex, inter-linked structures of values such as lists, trees, etc.
 */
@Adat
class AvValue<T>(

    /**
     * The unique identifier of this value, by default, it is a Version 7 UUID. Which also contains
     * the creation time of the value.
     */
    val uuid: AvValueId = uuid7(),

    /**
     * A friendly ID to show to the users. May be auto-generated to be unique or freely modifiable.
     * Use short, identifier like values such as "TH-0001", When null, use [name] or [uuid] instead.
     */
    val friendlyId: FriendlyItemId? = null,

    /**
     * The number of times this value has been changed.
     */
    val revision: Long = 1,

    /**
     * The last time this value was changed.
     */
    val lastChange: Instant = Clock.System.now(),

    /**
     * The ID of the Access Control List that governs the access to this value. When null,
     * the ACL check mechanism will try to find an ACL depending on the value store configuration.
     */
    val acl: AvValueId? = null,

    /**
     * Name of the value, `Kitchen Thermostat`, for example. Typically changeable by the user.
     * When null, the value is called `anonymous`.
     */
    val name: String? = null,

    /**
     * A set of strings that describe the current status of the value. The actual strings used are
     * domain-dependent, typical examples are: `ok`, `disabled`, `down`.
     */
    val statusOrNull: Set<AvStatus>? = null,

    /**
     * Markers are labels or tags that specify the stereotype of the value.
     *
     * The actual strings used are domain-dependent.
     *
     * For example, a room in a building may have the following markers: `space`, `room`, `storage`.
     *
     * - `space` tells us that this is a physical space somewhere
     * - `room` tells us that this is a room-type space, other examples would be `building`, `site`, `floor`
     * - `storage` tells us that this room is used as a storage
     */
    val markersOrNull: Set<AvMarker>? = null,

    /**
     * References to other values. Value stores do not enforce reference integrity, so it is possible
     * to reference something that in another value store or even a non-value.
     */
    val refsOrNull: Map<AvRefLabel, AvValueId>? = null,

    /**
     * The domain-specific arbitrary data this value stores.
     */
    val spec: T
) {

    constructor(
        uuid: AvValueId = uuid7(),
        lastChange: Instant = now(),
        status: Set<String>? = null,
        spec: T
    ) : this(uuid = uuid, lastChange = lastChange, statusOrNull = status, spec = spec)

    /**
     * Get the set of statuses or an empty set if there are no statuses for this value.
     */
    val status get() = statusOrNull ?: emptySet()

    /**
     * Get the set of statuses as a mutable set. Changing this set **DOES NOT** change the value,
     * use [copy] to create a new version of the value with the new status set.
     */
    fun mutableStatus() = statusOrNull?.toMutableSet() ?: mutableSetOf()

    /**
     * Get the set of markers or an empty set if there are no markers for this value.
     */
    val markers get() = markersOrNull ?: emptySet()

    /**
     * Get the set of markers as a mutable set. Changing this set **DOES NOT** change the value,
     * use [copy] to create a new version of the value with the new marker set.
     */
    fun mutableMarkers() = markersOrNull?.toMutableSet() ?: mutableSetOf()

    /**
     * Get the map of references or an empty map if there are no references for this value.
     */
    val refs get() = refsOrNull ?: emptyMap()

    /**
     * Get the map of references as a mutable map. Changing this map **DOES NOT** change the value
     * use [copy] to create a new version of the value with the new references.
     */
    fun mutableRefs() = refsOrNull?.toMutableMap() ?: mutableMapOf()

    /**
     * Get the ID of a reference by the reference label.
     *
     * @throws  NoSuchElementException  if there is no reference with the given [label]
     */
    fun refId(label: AvRefLabel) : AvValueId =
        refsOrNull?.get(label) ?: throw NoSuchElementException("No reference for label $label in value $uuid")

    /**
     * Get the ID of a reference by the reference label, or null if the reference is not found.
     */
    fun refIdOrNull(label: AvRefLabel) = refsOrNull?.get(label)

    /**
     * When the value is accessed the first time, the value store sets [aclAgent]
     * which then will be used to make access control decisions. The agent is
     * identified by [acl]. If [acl] changes, the value store will re-set [aclAgent].
     */
    internal var aclAgent : AvAclAgent? = null

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

    val nameLike
        get() = name ?: friendlyId ?: uuid.toShort()

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