# Layouts

| Function | Supported Adapters    | Description                                      |
|----------|-----------------------|--------------------------------------------------|
| `box`    | browser, android, ios | Position each fragment with x and y coordinates. |
| `row`    | browser, android, ios | Stack fragments next to each other.              |                                                
| `column` | browser, android, ios | Stack fragments below each other.                |
| `grid`   | browser, android, ios |                                                  |

## Box

The box layout position the fragments directly with x and y coordinates.

```kotlin
box {
    text("a", Frame(0,0,100,100))
    text("b", Frame(0,0,100,120))
}
```

## Row

Row simply positions fragments next to each other.

```kotlin
row {
    text("a")
    text("b")
}
```

### Instructions

```kotlin
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.JustifyContent

row(AlignItems.Start)
row(AlignItems.Center)
row(AlignItems.End)

row(JustifyContent.Start)
row(JustifyContent.Center)
row(JustifyContent.End)
```

## Column

Column simply positions fragments below to each other.

```kotlin
column {
    text("a")
    text("b")
}
```


### Instructions

```kotlin
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.JustifyContent

column(AlignItems.Start)
column(AlignItems.Center)
column(AlignItems.End)

column(JustifyContent.Start)
column(JustifyContent.Center)
column(JustifyContent.End)
```

## Grid

Grid provides a partial implementation of the [CSS grid layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout).

```kotlin
grid(
    ColTemplate(10.dp, 1.dp, repeat(5, 10.dp)),
    RowTemplate(20.dp)
) {
    /* ... */
}
```

### Instructions

To put a fragment at a given grid position and/or make it span columns/rows use one or a combination of
these:

- `Number.gridCol`
- `Number.gridRow`
- `Number.colSpan`
- `Number.rowSpan`
- `GridCol(col : Int, span : Int = 1)`
- `GridRow(row : Int, span : Int = 1)`
- `RowSpan(span : Int)`
- `ColSpan(span : Int)`
- `GridPos(row : Int, col : Int, rowSpan : Int = 1, colSpan: Int = 1)`

```kotlin
 grid(
    RowTemplate(50.dp),
    ColTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)
) {

    row(2.gridCol) {
        text("Sign Up", white)
    }

    row(4.gridCol) {
        text("Sign In", white)
    }

}
```