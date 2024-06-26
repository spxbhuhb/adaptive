# Rendering

Rendering of actual UI relies heavily on instructions and on the `CommonRenderData` class. 

## Considerations

Typically, there are only a few instructions per fragment, ten is really pushing it in practice.
Because of the low level of instructions, it is better to let the instructions check the
render data class than the other way.

We cannot use Kotlin expect/actual as it would bind us to one implementation per platform.
Render data classes provide the intermediate format between instructions and the actual UI
render data.

The data for the actual rendering forms groups depending on the fragment to be rendered.
Some of these groups such as `layout` are the same for all fragments. Some other groups
like `text` are specific for a given fragment type. 

It is unnecessary to initialize all the fields for all fragments when they are not used.
Therefore `CommonRenderData` data uses subclasses for these groups: `ContainerRenderData`,
`DecorationRenderData`, `LayoutRenderData` and so on.

## Layout

Layout processing starts when a fragment calls `addActualRoot` of the adapter. This method
sets the frame of the fragment, calls `measure` and `layout`. Example from the browser adapter:

```kotlin
override fun addActualRoot(fragment: AdaptiveFragment) {
    traceAddActual(fragment)

    fragment.alsoIfInstance<AbstractContainerFragment<HTMLElement, HTMLDivElement>> {
        rootContainer.getBoundingClientRect().let { r ->
            val frame = RawFrame(0.0, 0.0, r.width, r.height)

            it.layoutFrame = frame
            it.measure()
            it.layout(frame)

            rootContainer.appendChild(it.receiver)
        }
    }
}
```

It is important that:

- `addActualRoot` is called at the end of `mount` of a top-level layout fragment a
- all adapters add the actual UI element to the actual UI **after** the layout process finishes

Adapters may have their own actual UI specific layout implementation that positions
the fragments by absolute coordinates calculated by the `layout` call in the above code:

- `ContainerViewGroup` and `StructuralViewGroup` for Android
- nothing for browser
- `ContainerView` for iOS


