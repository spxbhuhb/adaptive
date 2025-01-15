# Drag and Drop

* `draggable` is a fragment that marks whatever is draggable
* `transferData` is used to set the transferred data whenever the user starts a drag
* `dragHover` is a producer, sets the variable value to true when the user is dragging over and the condition is true
* `dropHandler` performs whatever data updates needed on drop

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

    dropHandler { ids += it.id }

    val dragHover = dragHover { it.data is SomeEntity }

    column {
        if (dragHover) backgrounds.red else backgrounds.transparent

        for (id in ids) {
            text(id)
        }
    }
}

@Adaptive
fun someSource() {
    someDraggable(SomeEntity(12, "a"))
    someDraggable(SomeEntity(23, "b"))
}
```