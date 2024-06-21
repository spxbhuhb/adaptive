# Foundation Fragments

## delegate

A fragment that delegates all the `gen*` calls to the provided functions.

> [!CAUTION]
>
> `delegate` is a very advanced topic, use it only if you really understand what it does.
>
> You can find an actual example between the unit tests in `core`.
>

```kotlin
delegate(
    buildFun = { parent, declarationIndex -> AdaptiveT1(parent.adapter, parent, declarationIndex) },
    patchExternalFun = { fragment -> fragment.state[0] = 12 },
    patchInternalFun = { false }
)
```

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

## slot

Define a slot that allows its children to be replaced.

Useful for:

* [navigation](../tutorials/navigation.md)
* complex displays (tabs for example when you want to remember content of the tab)

```kotlin
slot("mainContent") {
    text("Click on the left to load a demo!")
}
```


