# Layouts

> [!NOTE]
>
> As of now all Adaptive layouts are **strict**. This means that the position of all fragments are calculated
> by the layout and the fragments are placed by absolute positioning relative to the container they are in.
>
> This works for now, but introduces issues I haven't solved yet, most notably optimization of big tables
> in browser. I'm not sure if I want to use strict layout for tables for performance reasons. I'll have to
> investigate this a bit in the future
>

| Function  | Supported Adapters    | Description                                                                                 |
|-----------|-----------------------|---------------------------------------------------------------------------------------------|
| `box`     | browser, android, ios | Position each fragment with x and y coordinates.                                            |
| `boxFlow` | browser, android, ios | Stack fragments in a row next to each other until space is available, then start a new row. |
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

## Column

Column simply positions fragments below to each other.

```kotlin
column {
    text("a")
    text("b")
}
```

## Grid

Grid provides a partial implementation of the [CSS grid layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout).

```kotlin
grid {
    colTemplate(10.dp, 1.dp, 10.dp repeat 5) .. rowTemplate(20.dp)
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
 grid {
    rowTemplate(50.dp) .. colTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp)
    
    row(2.gridCol) {
        text("Sign Up", white)
    }

    row(4.gridCol) {
        text("Sign In", white)
    }

}
```

## Layout Instructions

>
> [!NOTE]
>
> Scroll is not implemented for Android and iOS yet.
>


```kotlin
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
fun a() {
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

This **DOES NOT WORK**. The reason is that the `box` is already big. It does not need
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