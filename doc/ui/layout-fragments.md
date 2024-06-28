# Layouts

| Function  | Supported Adapters    | Description                                                                                 |
|-----------|-----------------------|---------------------------------------------------------------------------------------------|
| `box`     | browser, android, ios | Position each fragment with x and y coordinates.                                            |
| `boxFlow` | browser               | Stack fragments in a row next to each other until space is available, then start a new row. |
| `row`     | browser, android, ios | Stack fragments next to each other.                                                         |                                                
| `column`  | browser, android, ios | Stack fragments below each other.                                                           |
| `grid`    | browser, android, ios |                                                                                             |

## Box

The box layout position the fragments:

- directly with x and y coordinates **or**
- by aligning them with standard alignment instructions

```kotlin
box {
    text("a", Frame(0,0,100,100))
    text("b", Frame(0,0,100,120))
    text("c", AlignSelf.center)
}
```

## Flow Box

The box flow layout positions fragments in a row next to each other until there is available space.
When the is no more space, opens a new row below.

```kotlin
flowBox {
    text("a")
    text("b")
}
```

You can limit the number of fragments in a row:

```kotlin
flowBox {
    flowItemLimit { 3 }
    text("a")
    text("b")
    text("c")
    // this item will start in a new row
    // even if there would be enough space
    text("d") 
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

>
> [!NOTE]
>
> Scroll is not implemented for Android and iOS yet.
>

```kotlin
import hu.simplexion.adaptive.ui.common.instruction.AlignItems

column(AlignItems.start)
column(AlignItems.center)
column(AlignItems.end)

column(AlignItems.startCenter)
column(AlignItems.topCenter)
column(AlignItems.endCenter)
column(AlignItems.bottomCenter)

column(scroll)
column(verticalScroll)
column(horizontalScroll)
```

### Scrolling

When adding scroll, pay attention to where you add the instruction.

This works, as you apply scroll to the column that contains the big stuff.

```kotlin
@Adaptive
fun a{
    column {
        scroll
        bigStuff()
    }
}

@Adaptive
fun bigStuff() {
    box(size(10000.dp, 10000.dp)) {  }
}
```

This **does not** work. The reason is that the `box` is already big. It does not need
to be scrolled to contain everything. The column that contains `bigStuff` is small,
the content of that column has to be scrolled.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.instruction.scroll

@Adaptive
fun a() {
    column {
        bigStuff()
    }
}

@Adaptive 
fun bigStuff() {
    box(size(10000.dp, 10000.dp)) {
        scroll
        throw RuntimeException("THIS DOES NOT WORK")
    }
}
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