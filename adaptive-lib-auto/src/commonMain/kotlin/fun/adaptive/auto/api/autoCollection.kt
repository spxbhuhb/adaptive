package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatCompanionResolve
import `fun`.adaptive.adat.api.toArray
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.instance.AutoCollection
import `fun`.adaptive.auto.internal.instance.AutoInstanceBuilder
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.internal.producer.AutoCollectionProducer
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.NonAdaptive
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Produces a new value whenever the auto collection handled by [instance]
 * changes.
 *
 * Does not create a new auto instance, adds a listener to [instance].
 *
 * This is a producer intended for use in an Adaptive fragment.
 *
 * @param    instance       The instance to connect with.
 * @param    binding        Set by the compiler plugin, ignore it.
 */
@Producer
fun <A : AdatClass> autoCollection(
    instance: CollectionBase<A>,
    binding: AdaptiveStateVariableBinding<Collection<A>>? = null
): Collection<A>? {
    checkNotNull(binding)

    val producer = AutoCollectionProducer(binding, disposeInstance = false)

    producer.instance = instance

    instance.addListener(producer)
    binding.targetFragment.addProducer(producer)

    // at this point the latest value of producer may be null
    // or not null, as the instance may or may not have a value
    // depending on the state of synchronization

    return producer.latestValue
}

/**
 * Create an **endpoint** and connect it to the peer with [AutoApi] using **service** connection.
 *
 * This is a producer intended for use in an Adaptive fragment.
 *
 * Uses the transport of the fragment adapter.
 *
 * @param    trace             Log trace information.
 * @param    binding           Set by the compiler plugin, ignore it.
 * @param    companion         Set by the compiler plugin, ignore it.
 * @param    infoFunSuspend    A function to get the connection info. Typically, this is created by
 *                             a service call.
 *
 * @return   `null` (it takes time to connect and synchronize)
 */
@Producer
@AdatCompanionResolve
fun <A : AdatClass> autoCollection(
    trace: Boolean = false,
    binding: AdaptiveStateVariableBinding<Collection<A>>? = null,
    companion: AdatCompanion<A>? = null,
    infoFunSuspend: InfoFunSuspend<SetBackend<A>, AutoCollectionPersistence<A>, Collection<A>, A>
): Collection<A>? {
    checkNotNull(binding)

    val producer = AutoCollectionProducer(binding, disposeInstance = true)
    binding.targetFragment.addProducer(producer)

    val instance =
        buildCollection(
            origin = false,
            infoFunSuspend = infoFunSuspend,
            defaultWireFormat = companion !!.adatWireFormat,
            listener = producer,
            trace = trace,
            scope = binding.targetFragment.adapter.scope,
            worker = binding.targetFragment.adapter.backend.firstImpl<AutoWorker>()
        )

    producer.instance = instance

    return producer.latestValue
}

/**
 * Create an **origin controller** auto instance with the initial value set to [initialValue].
 *
 * * The instance **DOES NOT** connect to any other nodes automatically.
 * * The instance **DOES NOT** have persistence.
 *
 * When [worker] **IS NOT** null:
 * * register the instance with [worker] to accept incoming service connections
 * * the instance may have **service or direct** connections
 *
 * When [worker] **IS** null:
 * * the instance may have **only direct** connections
 *
 * @param    initialValue   The value of the auto instance.
 * @param    worker         An optional worker to register this instance with.
 * @param    listener       An optional listener to get auto events.
 * @param    persistence    Persistence provider, optional.
 * @param    trace          Log trace information.
 * @param    companion      The adat companion to fetch the wireformat from. When on default the compiler
 *                          will resolve the type parameter into an actual companion.
 */
@NonAdaptive
@AdatCompanionResolve
fun <A : AdatClass> autoCollectionOrigin(
    initialValue: Collection<A>?,
    worker: AutoWorker? = null,
    listener: AutoCollectionListener<A>? = null,
    persistence: AutoCollectionPersistence<A>? = null,
    trace: Boolean = false,
    companion: AdatCompanion<A>? = null
): CollectionBase<A> =

    @Suppress("UNCHECKED_CAST")
    buildCollection(
        origin = true,
        initialValue = initialValue,
        defaultWireFormat = companion?.adatWireFormat,
        listener = listener,
        trace = trace,
        worker = worker,
        scope = CoroutineScope(Dispatchers.Default),
        persistence = persistence
    )

/**
 * Create a **node** with no initial value and connect it to another auto node
 * through a worker.
 *
 * The created instance:
 *
 * - **initiates a connection** with [infoFunSuspend]
 * - **is registered** with the [worker]
 * - may have **service or direct** connections
 *
 * @param    worker          The worker to register this instance with.
 * @param    listener        An optional listener to get auto events.
 * @param    persistence     Persistence provider, optional.
 * @param    trace           Log trace information.
 * @param    companion       The adat companion to fetch the wireformat from. When on default the compiler
 *                           will resolve the type parameter into an actual companion.
 * @param    infoFunSuspend  Called to get the connection info from the peer.
 */
@NonAdaptive
@AdatCompanionResolve
fun <A : AdatClass> autoCollectionNode(
    worker: AutoWorker,
    listener: AutoCollectionListener<A>? = null,
    persistence: AutoCollectionPersistence<A>? = null,
    trace: Boolean = false,
    companion: AdatCompanion<A>? = null,
    infoFunSuspend: InfoFunSuspend<SetBackend<A>, AutoCollectionPersistence<A>, Collection<A>, A>,
) =

    @Suppress("UNCHECKED_CAST")
    buildCollection(
        origin = false,
        infoFunSuspend = infoFunSuspend,
        defaultWireFormat = companion?.adatWireFormat,
        listener = listener,
        trace = trace,
        worker = worker,
        scope = CoroutineScope(Dispatchers.Default),
        persistence = persistence
    )

/**
 * Create a **node** with no initial value and connect it to another auto node
 * without involving a worker.
 *
 * The created instance:
 *
 * - creates a **direct connection** with [peer]
 * - is **NOT registered** with any workers
 * - may have **only direct** contentions
 *
 * @param    peer            The instance to connect with.
 * @param    listener        An optional listener to get auto events.
 * @param    persistence     Persistence provider, optional.
 * @param    trace           Log trace information.
 * @param    companion       The adat companion to fetch the wireformat from. When on default the compiler
 *                           will resolve the type parameter into an actual companion.
 */
@NonAdaptive
@AdatCompanionResolve
fun <A : AdatClass> autoCollectionNode(
    peer: CollectionBase<A>,
    listener: AutoCollectionListener<A>? = null,
    persistence: AutoCollectionPersistence<A>? = null,
    trace: Boolean = false,
    companion: AdatCompanion<A>? = null
) =
    @Suppress("UNCHECKED_CAST")
    buildCollection(
        origin = false,
        infoFunSuspend = { peer.connectInfo(AutoConnectionType.Direct) },
        directPeer = peer,
        defaultWireFormat = companion?.adatWireFormat,
        listener = listener,
        trace = trace,
        scope = CoroutineScope(Dispatchers.Default),
        persistence = persistence
    )


@Suppress("UNCHECKED_CAST")
private fun <A : AdatClass> buildCollection(
    origin: Boolean,
    defaultWireFormat: AdatClassWireFormat<A>?,
    scope: CoroutineScope,
    initialValue: Collection<A>? = null,
    infoFunSuspend: InfoFunSuspend<SetBackend<A>, AutoCollectionPersistence<A>, Collection<A>, A>? = null,
    directPeer: CollectionBase<A>? = null,
    listener: AutoCollectionListener<A>? = null,
    trace: Boolean,
    worker: AutoWorker? = null,
    persistence: AutoCollectionPersistence<A>? = null,
): AutoCollection<SetBackend<A>, AutoCollectionPersistence<A>, A> =

    AutoInstanceBuilder<SetBackend<A>, AutoCollectionPersistence<A>, Collection<A>, A>(
        origin = origin,
        collection = true,
        infoFunSuspend = infoFunSuspend,
        directPeer = directPeer,
        defaultWireFormat = defaultWireFormat,
        collectionListener = listener,
        worker = worker,
        scope = scope,
        trace = trace,
        backendFun = { builder, export ->
            export as AutoCollectionExport<A>

            val values = mutableMapOf<ItemId, PropertyBackend<A>>()

            export.items.forEach {
                values[it.itemId !!] = PropertyBackend(
                    builder.instance,
                    null,
                    it.item?.toArray(),
                    initialPropertyTimes = it.propertyTimes,
                    itemId = it.itemId
                )
            }

            SetBackend(
                builder.instance,
                values
            )
        },
        persistence = persistence
    ).build(
        initialValue
    ) as AutoCollection<SetBackend<A>, AutoCollectionPersistence<A>, A>