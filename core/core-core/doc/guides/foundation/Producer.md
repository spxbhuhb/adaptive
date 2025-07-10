---
status: outdated
---

# Producer

[producer](def://?inline)

> [!IMPORTANT]
> Indirect use of producers is **not supported**. Producers must be called directly.

Invalid:

```kotlin
val a = if (odd) fetch { 12 } else fetch { 13 }
```

Valid:

```kotlin
val a = fetch { if (odd) 12 else 13 }
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

## Observe

Use `observe` to get a value from an `Observable`:

[observeExample](example://)

- `observe` adds itself as a listener for the observable (`observableOf` in the example).
- Whenever the observable changes, it notifies the listeners.
- In turn, `observe` updates the state variable which triggers a fragment patching.

## See Also

- [Fragment](guide://)
- [Instruction](guide://)
