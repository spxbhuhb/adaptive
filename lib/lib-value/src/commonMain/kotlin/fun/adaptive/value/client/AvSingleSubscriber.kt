package `fun`.adaptive.value.client

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscribeFun
import `fun`.adaptive.value.AvValue
import kotlin.reflect.KClass

/**
 * Subscribe a single [AvValue] and notify listeners when the value is updated from
 * the remote.
 */
open class AvSingleSubscriber<SPEC : Any>(
    subscribeFun: AvSubscribeFun,
    backend: BackendAdapter,
    val specClass: KClass<SPEC>,
) : AvValueSubscriber<AvValue<SPEC>?>(subscribeFun, backend) {

    constructor(backend: BackendAdapter, specClass: KClass<SPEC>, condition: AvSubscribeCondition) : this(
        { service, id -> listOf(condition).also { service.subscribe(it) } }, backend, specClass
    )

    override var value: AvValue<SPEC>? = null

    override fun process(value: AvValue<*>) {
        if (! specClass.isInstance(value.spec)) {
            getLogger("AvSingleSubscriber").warning("invalid spec type: ${value.spec::class} != $specClass in ${value.adatToString()}")
            return
        }

        @Suppress("UNCHECKED_CAST")
        this.value = value as AvValue<SPEC>
    }

    override fun onCommit() {
        notifyListeners()
    }

}