# Producers

Producers provide value(s) for a state variable. They typically add an `AdaptiveProducer` to the fragment to
fetch the value.

## Poll

> [!NOTE]
>
> I'm really unhappy with passing the default to `poll`. It's hideous. It would be better to
> - provide a version of poll which is not suspend
> - provide a `pollOrNull`
> - figure out the data type from the lambda and provide a default value from the compiler plugin
>

The `poll` function lets you refresh a state variable periodically:

```kotlin
fun Adaptive.example() {
    val time = poll(200.ms, Clock.System.now()) { Clock.System.now() }
    text { time }
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

## Future plans

- fetch
- listen

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