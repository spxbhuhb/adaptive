# Schema

```kotlin
infix fun Int.min(value : Int) : Int = TODO()
infix fun Int.max(value : Int) : Int = TODO()

class Test(
    val i1 : Int = 5,
    val i2 : Int = 5 min 10 max 20 
)
```