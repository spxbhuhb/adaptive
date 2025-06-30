package `fun`.adaptive.value.local

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.*
import `fun`.adaptive.value.operation.AvValueOperation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


/**
 * Listens for value operations matching specified conditions and processes them according to the given spec class.
 *
 * @param SPEC The type of spec to process
 * @param values The value worker instance to listen to
 * @param conditions List of conditions that trigger the listener
 * @param specClass The Kotlin class of the spec
 * @param processFun The function to process each matching spec
 * @param scope The coroutine scope used for processing (defaults to a new scope with the Default dispatcher)
 */
class AvSpecListener<SPEC : Any>(
    val values: AvValueWorker,
    val conditions: List<AvSubscribeCondition>,
    val specClass: KClass<SPEC>,
    val processFun: suspend (AvValue<SPEC>) -> Unit,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {

    /**
     * Convenience constructor that creates a listener with a single marker condition.
     *
     * @param values The value worker instance to listen to
     * @param marker The marker to filter values
     * @param specClass The Kotlin class of the spec
     * @param processFun The function to process each matching spec
     */
    constructor(
        values: AvValueWorker,
        marker: AvMarker,
        specClass: KClass<SPEC>,
        processFun: suspend (AvValue<SPEC>) -> Unit
    ) : this(values, listOf(AvSubscribeCondition(marker = marker)), specClass, processFun)

    private val subscriptionId: AvSubscriptionId = uuid4()

    private val channel = Channel<AvValueOperation>(Channel.UNLIMITED)

    /**
     * Starts the listener by subscribing to value operations and launching the processing coroutine.
     */
    fun start() {
        values.subscribe(
            AvChannelSubscription(
                subscriptionId,
                conditions,
                channel
            )
        )

        scope.launch { run() }
    }

    /**
     * Processes value operations from the channel by applying the process function to each matching spec.
     */
    suspend fun run() {
        for (operation in channel) {
            operation.forEachValueSuspend(specClass) {
                processFun(it)
            }
        }
    }

    /**
     * Stops the listener by closing the channel.
     */
    fun stop() {
        channel.close()
    }

    companion object {

        /**
         * Creates a new spec listener with a reified type parameter.
         *
         * @param SPEC The type of spec to process
         * @param values The value worker instance to listen to
         * @param marker The marker to filter values
         * @param processFun The function to process each matching spec
         * @return A new AvSpecListener instance
         */
        inline fun <reified SPEC : Any> avSpecListener(
            values: AvValueWorker,
            marker: AvMarker,
            noinline processFun: suspend (AvValue<SPEC>) -> Unit
        ) = AvSpecListener(values, marker, SPEC::class, processFun)

    }

}