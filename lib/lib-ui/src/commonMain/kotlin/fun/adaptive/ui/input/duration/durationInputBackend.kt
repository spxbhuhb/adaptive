package `fun`.adaptive.ui.input.duration

import kotlin.time.Duration

fun durationInputBackend(inputValue : Duration? = null, builder: DurationInputViewBackendBuilder.() -> Unit = {  }) =
    DurationInputViewBackendBuilder(inputValue).apply(builder).toBackend()
