# Drag and Drop

| name           | type        | function                        |
|----------------|-------------|---------------------------------|
| `draggable`    | fragment    | contains whatever is draggable  |
| `transferData` | instruction | sets transferred data           |
| `dropTarget`   | fragment    | fragment that can receive drops |
| `onDrop`       | instruction | handles the actual drop         |                            

The drag image is a bit problematic. Best would be to handle it ourselves but that would be a big task, so I plan to use:

* the default drag and drop image in browser
* capture the view on Android and iOS

```kotlin
var items = emptyList<String>()

column {

    draggable {
        transferData { "hello" }
        text(Strings.snooze)
    }

    dropTarget {

        onDrop {
            items = items + (it.transferData?.data as String)
        }

        column(size(200.dp, 200.dp), borders.outline) {
            verticalScroll

            for (item in items) {
                text(item)
            }
        }

    }

}
```