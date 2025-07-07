# Wrap

[Wrapper example](actualize:///cookbook/example/split-pane-wrapper)

## Details

Wrapping means that the size of a fragment depends on the size of another fragment and should be
laid out accordingly. 

For example, the quotes in Markdown have a decoration which should be as high as the quote content.
We do not know the height of the quote content before actually rendering it as it depends on the
available width. The available width depends on the width of the decoration therefore, we have
a cyclic dependency.

To solve this, we have to decide the width of the wrapping fragment first, then lay out the wrapped
fragment, then lay out the wrapping fragment with the height of the wrapped fragment.

Split pane handles this process when the method is one of `Wrap*`.

There are four shortcuts for wrapping:

* `wrapFromLeft`
* `wrapFromRight`
* `wrapFromTop`
* `wrapFromBottom`

These handle the split pane view backend configuration under the hood:

```kotlin
val wrapperWidth = 8.dp

@Adaptive
fun someFun() {
    wrapFromLeft(wrapperWidth, ::leftWrapper) {
        text("Hello World!")
    }
}

@Adaptive
private fun leftWrapper(): AdaptiveFragment {
    box { maxHeight .. width { wrapperWidth } .. backgrounds.successSurface .. cornerRadius { 2.dp } }
    return fragment()
}
```