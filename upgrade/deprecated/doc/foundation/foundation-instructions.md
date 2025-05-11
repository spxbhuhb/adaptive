# Foundation Instructions

## Name

To give a fragment a name use `Name`. This might be useful for a few reasons:

- trace checks if a fragment has a name and uses it instead of the class name
- for browser, the `id` of the HTML node is set to the name

```kotlin
row(4.gridCol, whiteBorder, cornerRadius, *center, Name("sign-in")) {
    text("Sign In", white)
}
```

Results in (assuming trace is enabled):

```text
[ AdaptiveText                     21 ]  layout                     |  layoutFrame=Frame(point=Point(top=-1.5, left=179.5), size=Size(width=133.0, height=53.0)) measuredSize=Size(width=133.0, height=53.0) instructedPoint=null instructedSize=null
[ sign-in                          22 ]  layout                     |  layoutFrame=Frame(point=Point(top=0.0, left=556.0), size=Size(width=492.0, height=50.0)) measuredSize=Size(width=119.0, height=53.0) instructedPoint=null instructedSize=null
[ AdaptiveText                     24 ]  layout                     |  layoutFrame=Frame(point=Point(top=-1.5, left=186.5), size=Size(width=119.0, height=53.0)) measuredSize=Size(width=119.0, height=53.0) instructedPoint=null instructedSize=null
```

## Trace

The `Trace` instruction lets you print out or collect a trace of Adaptive function calls.

The `Trace` instruction may be used to switch on trace:

- globally for an adapter
- locally for a fragment

> [!IMPORTANT]
>
> These are regular expressions, not strings. `*` does not match everything, use `.*`
>

Parameters of `Trace` are regular expressions to filter the trace by the point of the trace.
For example `Trace("layout.*")` adds all trace lines with points that start with "layout".

```kotlin
val traceLayout = Trace(Regex("layout"), Regex("measure.*"))

android(this, rootView, ExampleExports, trace = traceLayout) {
    login()
}
```

```kotlin
@Adaptive
fun someFun() {

    row(2.gridCol, *someStyles, Trace(Regex(".*"))) {
        text("Hello World!", white)
    }

}
```
