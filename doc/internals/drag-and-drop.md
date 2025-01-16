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
@Adat
class SomeEntity(
    val id: Int,
    val name: String
)

@Adaptive
fun draggableEntity(entity: SomeEntity) {

    draggable {
        transferData { entity }
        text(entity.name)
    }
    
}

@Adaptive
fun someTarget() {
    val ids = listOf<String>()

    dropTarget<SomeEntity> { transfer : SomeEntity? ->
    
        onDrop(transfer) { ids += it.id }
    
        column {
            if (transfer != null) {
                backgrounds.red
            } else {
                backgrounds.transparent
            }

            for (id in ids) {
                text(id)
            }
        }
    }
}

@Adaptive
fun someSource() {
    someDraggable(SomeEntity(12, "a"))
    someDraggable(SomeEntity(23, "b"))
}
```