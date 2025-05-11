# What is a producer

Producers are functions that initialize or update state variables over time. They are most commonly used for:

- Fetching data from suspend functions.
- Polling values on a timer.
- Providing external or computed values outside normal rendering cycles.

Supported producer types:

| Producer    | Use Case                                                                  |
|-------------|---------------------------------------------------------------------------|
| `fetch`     | Call a suspend function once and update when it returns.                  |
| `poll`      | Call a suspend function on an interval, update on each result.            |
| `periodic`  | Call a non-suspend function on an interval, update on each result.        |
| `valueFrom` | Bind to an external value store.                                          |

> [!IMPORTANT]
> Indirect use of producers is **not supported**. Producers must be called directly.

Invalid:

```kotlin
val a = if (odd) fetch { 12 } else fetch { 13 }  // ❌ does not work
```

Valid:

```kotlin
val a = fetch { if (odd) 12 else 13 }  // ✅ works correctly
```

## Built-in producers

## Fetch

Use `fetch` when retrieving a value from a suspend function just once.

```kotlin
@Adaptive
fun example() {
    val books = fetch { bookService.getBooks() } ?: emptyList()

    for (book in books) {
        text(book.title)
    }
}
```

- Initially returns `null`, then sets value when async call completes.
- Automatically triggers patching upon resolution.

## Poll

Use `poll` when you need to periodically re-fetch data using a suspend function.

```kotlin
@Adaptive
fun example() {
    val books = poll(10.seconds) { bookService.getBooks() } ?: emptyList()

    for (book in books) {
        text(book.title)
    }
}
```

- Returns `null` initially.
- Fetches and sets the value repeatedly on the given interval.

## Periodic

Use `periodic` to invoke a regular update from a **non-suspend** function.

```kotlin
val time = periodic(1.seconds) { now() }
```

- Efficient for local or lightweight time-based updates.

## ValueFrom

Use `valueFrom` to get a value from an `Observable`:

```kotlin
package my.project.example.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text

val valueStore = adaptiveStoreFor(12)

@Adaptive
fun exampleFun() {
    val observed = valueFrom { valueStore }
    text(observed) .. onClick { valueStore.value = valueStore.value + 1 }
}
```

- `valueForm` adds itself as a listener for the observable (`valueStore` in the example).
- Whenever the observable changes, it notifies the listeners.
- In turn, `valueFrom` updates the state variable which triggers a fragment patching.

## See Also

- [What is an instruction](guide://)
- [What is a fragment](guide://)
