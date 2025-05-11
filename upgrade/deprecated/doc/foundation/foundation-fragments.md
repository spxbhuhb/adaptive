# Foundation Fragments

## measureFragmentTime

Use the `measureFragmentTime` fragment to print out the time spent in `create`, `mount`, `unmount` and `dispose`:

```kotlin
@Adaptive
fun chessBoard() {
    measureFragmentTime {
        column(greenGradient) {
            for (r in 0 until size) {
                row {
                    for (c in 1 .. size) {
                        row(*colors(r, c), *center) { text(r * size + c, Size(40.dp, 40.dp)) }
                    }
                }
            }
        }
    }
}
```

On the console you can see the times spent (this is not a trace but a log, you don't need trace for this).

```text
[Log]       175  [ AdaptiveMeasureFragmentTime      22 ]  create duration            |  168ms
[Log]       193  [ AdaptiveMeasureFragmentTime      22 ]  mount duration             |  17ms
[Log]      2129  [ AdaptiveMeasureFragmentTime      22 ]  unmount duration           |  25ms
[Log]      2137  [ AdaptiveMeasureFragmentTime      22 ]  mount dispose              |  8ms
```


