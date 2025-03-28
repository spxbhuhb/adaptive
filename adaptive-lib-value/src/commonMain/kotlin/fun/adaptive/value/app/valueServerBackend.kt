package `fun`.adaptive.value.app

import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

@Adaptive
fun valueServerBackend(
    persistence: AbstractValuePersistence = NoPersistence()
) {

    worker { AvValueWorker(persistence) }

}