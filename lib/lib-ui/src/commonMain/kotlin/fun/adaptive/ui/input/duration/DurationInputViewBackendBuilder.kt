package `fun`.adaptive.ui.input.duration

import `fun`.adaptive.ui.input.InputViewBackendBuilder
import kotlin.time.Duration

class DurationInputViewBackendBuilder(
    inputValue: Duration?
) : InputViewBackendBuilder<Duration, DurationInputViewBackend>(inputValue) {

    override fun toBackend() =
        DurationInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
        }

}