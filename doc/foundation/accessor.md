# Accessors

Accessors are used to make binding between a fragment and a state variable easy:

```kotlin
var s = "Hello World!"
stringEditor { s }
```

A bit more sophisticated example:

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun bookEditor(bookUuid : UUID<Book>) {
    val book = fetch { bookStore.get(bookUuid) } ?: Book()
    
    stringEditor { book.title }
    select { book.author } query { authorService.get() }
    dateEditor { book.publishDate }
}
```

## Internals

`IrFunction2ArmClass.transformValueArgument` checks if the parameter is an accessor, if so
  - sets the previous argument to a state variable binding
  - keeps the accessor function as-is

State of the called fragment contains only the binding, not the accessor.