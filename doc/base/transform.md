# Transform

Transforms change the state of a fragment **after** their external state variables are set but
**before** their internal state variables are calculated.

```kotlin
fun Adaptive.example() {
    val authorFilter = ""
    editor { authorFilter }
    select() query { authorService.query(authorFilter) }
}

interface SelectTransformApi : AdaptiveTransformApi {
    infix fun query(value: suspend () -> List<String>) {
        setStateVariable(0, value)
    }
}

fun Adaptive.select(query: (suspend () -> List<String>)? = null): SelectTransformApi {
    val authors = fetch(emptyList()) { query?.invoke() }

    /* ... */

    return thisState()
}
```