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
| `flowBox` | browser, android, ios | Stack fragments in a row next to each other until space is available, then start a new row. |
| `rootBox` | browser               | A box that is added directly to the root container.                                         |
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

## Root Box

`rootBox` is the same as `box` with one key difference: it is added directly to the
root container instead of the parent fragment.

This is used mostly for modals when we want to place something over everything else.

```kotlin
rootBox {
    text("a", Frame(0,0,100,100))
    text("b", Frame(0,0,100,120))
    text("c", AlignSelf.center)
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

* Basic use: instruct column and row sizes with `colTemplate` and `rowTemplate`.
* Both defaults to `1.fr` if not specified.
* Rows also extend by `1.fr` if not instructed otherwise, see [extension](#automatic-grid-extension).

```kotlin
grid {
    colTemplate(10.dp, 1.dp, 10.dp repeat 5)
    rowTemplate(20.dp)
    
    // ...
}
```

### Automatic grid extension

* When not switched off, grids add rows to contain fragments if needed.
* Use the `extend` parameter of `rowTemplate` and `colTemplate` to set extension.
* If `extend` is set to `null` automatic extension is off.
* Default for `extend` is `1.fr` for rows, `null` for columns.
* If automatic extension is off, the grid throws exception on overflow.

This example adds `20.dp` rows as needed (in this case all rows are added automatically).

```kotlin
grid {
    colTemplate(40.dp, 40.dp)
    rowTemplate(extend = 20.dp)
}
```

### Grid sizing

* When a grid has an instructed width or height, that value is used.
* When the grid has only fix size rows or columns, the sum of the row/column sizes is used.
* Otherwise, the size proposed by the parent container is used.

### Placing fragments directly

Direct fragment placing cannot be used by fragments placed by automatic grid extension.

To put a fragment at a given grid position and/or make it span columns/rows use one or a combination of
these:

- `Number.gridCol`
- `Number.gridRow`
- `Number.colSpan`
- `Number.rowSpan`
- `gridCol(col : Int, span : Int = 1)`
- `gridRow(row : Int, span : Int = 1)`
- `rowSpan(span : Int)`
- `colSpan(span : Int)`
- `gridPos(row : Int, col : Int, rowSpan : Int = 1, colSpan: Int = 1)`

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
column(alignItems.start)
column(alignItems.center)
column(alignItems.end)

column(alignItems.startCenter)
column(alignItems.topCenter)
column(alignItems.endCenter)
column(alignItems.bottomCenter)

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
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.scroll

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