# Poll

The `poll` function lets you refresh a state variable periodically:

```kotlin
fun Adaptive.example() {
    val time = poll(200.ms, Clock.System.now()) { Clock.System.now() }
    text { time }
}
```