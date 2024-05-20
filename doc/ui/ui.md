# User Interface

> [!NOTE]
> 
> This is basically just a plan for now, I'll implement these in short order (like starting today).
> 

## Layouts

| Function | Supported Adapters | Description                                      |
|----------|--------------------|--------------------------------------------------|
| `stack`  |                    | Horizontal or vertical stack of fragments.       |
| `grid`   |                    | CSS grid wannabe (grid of fragments).            |                                                
| `pixel`  |                    | Position each fragment with x and y coordinates. |

## Stack

Stack simply positions elements below or next to each other. Default positioning is vertical.

```kotlin
stack {
    text("a")
    text("b")
}
```

```kotlin
stack(horizontal) {
    text("a")
    text("b")
}
```

## Grid

Grid provides a partial implementation of the [CSS grid layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout).

```kotlin
grid(
    colTemplate(10.px, 1.fr, repeat(5, 10.px)),
    rowTemplate(20.px)
) {
    /* ... */
}
```

## Pixel

Pixel layouts position the components directly with x and y coordinates.

```kotlin
pixel {
    text("a", coords(100,100))
    text("b", coords(100,120))
}
```