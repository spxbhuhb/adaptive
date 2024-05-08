# Producers

Producers provide value(s) for a state variable. They typically add an `AdaptiveProducer` to the fragment to
fetch the value.

```kotlin
fun Adaptive.example() {
    val books = fetch(emptyList<Book>()) { bookService.getBooks() }
    val time = poll(200.ms, Clock.System.now()) { Clock.System.now() }
    val releases = listen(emptyList<Release>) { bookService.releaseFeed() }
    
    text { time }
    
    for (book in books) {
        text { book.title }
    }
    
    for (release in releases) {
        text { release.title }
    }
}
```

## Internals

`AdaptiveStateVariableBinding` parameters paired with selector functions in the state definition
are transformed by the compiler plugin into a producer function call. For example:

```kotlin
fun <VT> poll(
    interval: Duration,
    default: VT,
    binding: AdaptiveStateVariableBinding<VT>? = null,
    pollFun: (suspend () -> VT)?
): VT {
    checkNotNull(binding)

    binding.owner.addWorker(
        AdaptivePoll(binding, interval)
    )

    return default
}
```

Is transformed into:

```kotlin
setStateVariable(index, poll(interval, default, AdaptiveStateVariableBinding<VT>(...), null))
```