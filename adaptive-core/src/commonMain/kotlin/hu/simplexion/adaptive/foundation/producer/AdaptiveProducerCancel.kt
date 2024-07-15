/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.producer

/**
 * Cancels the producer by returning from the coroutine. Throws [AdaptiveProducerCancel].
 */
fun cancelProducer(message: String? = null): Nothing {
    throw AdaptiveProducerCancel(message)
}

class AdaptiveProducerCancel(message : String?) : RuntimeException(message)