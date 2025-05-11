# Scrolling

Scrolling and scrollbar styling is a problematic topic, especially in browsers.

You can add scrolling to a container with any of these instructions:

```kotlin
verticalScroll
horizontalScroll
scroll
```

To have scrolling work properly, you have to limit the size of the container in the
scroll direction at least.

## Scrollbar style in browsers

When a scroll instructions are added to the fragment, `AuiAdapter.applyLayoutToActual`
applies these CSS styles:

- `overflow` to `auto` (in the respective direction)
- `scrollbar-gutter` to `stable`

Mechanisms:

- For non-macOs platforms, the `custom-scrollbar` CSS class is added to the root element of the adapter.
- Size of the scrollbar is measured with `getScrollbarWidth` during adapter initialization.
- `box`, `row` and `column` layouts subtract this size from the available space when the container has a scrolling instruction. 

The `custom-scrollbar` CSS class is typically defined in `index.html`:

```css
.custom-scrollbar::-webkit-scrollbar {
    width: 7px;
    height: 7px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 3.5px;
}
```