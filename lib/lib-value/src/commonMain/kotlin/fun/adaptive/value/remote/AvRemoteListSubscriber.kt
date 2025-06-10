package `fun`.adaptive.value.remote

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscribeFun
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlin.reflect.KClass

/**
 * Subscribe a list of [AvItem<V>], notify the listener when the list or any item
 * in the list changes.
 */
open class AvRemoteListSubscriber<SPEC : Any>(
    subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    val specClass: KClass<SPEC>,
    val transform: ((Map<AvValueId, AvValue<SPEC>>) -> List<AvValue<SPEC>>)? = null
) : AvAbstractRemoteSubscriber<List<AvValue<SPEC>>>(subscribeFun, backend) {

    constructor(
        backend: BackendAdapter,
        specClass: KClass<SPEC>,
        vararg conditions: AvSubscribeCondition
    ) : this(
        { service, id -> conditions.toList().also { service.subscribe(it) } }, backend, specClass
    )

    constructor(
        backend: BackendAdapter,
        specClass: KClass<SPEC>,
        transform: ((Map<AvValueId, AvValue<SPEC>>) -> List<AvValue<SPEC>>),
        vararg conditions: AvSubscribeCondition
    ) : this(
        { service, id -> conditions.toList().also { service.subscribe(it) } }, backend, specClass, transform
    )

    private val itemMap = mutableMapOf<AvValueId, AvValue<SPEC>>()

    override fun process(value: AvValue<*>) {
        if (! specClass.isInstance(value.spec)) return

        @Suppress("UNCHECKED_CAST")
        itemMap[value.uuid] = value as AvValue<SPEC>
        cachedValue = null

        notifyListeners()
    }

    override var value: List<AvValue<SPEC>>
        get() = cachedValue ?: (transform?.invoke(itemMap) ?: itemMap.values.toList()).also { cachedValue = it }
        set(_) = unsupported()

    private var cachedValue: List<AvValue<SPEC>>? = null

}