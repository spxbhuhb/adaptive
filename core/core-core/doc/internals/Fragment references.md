# Fragment references

There are four ways to pass a fragment model to a higher-level one:

- lambda
- Kotlin function reference
- by fragment key
- by fragment resource

Also, we should be able to store fragment models in properties and variables:

- global variables
- class properties

## Use patterns

```kotlin
class Config( 
    @Adaptive  
    val fragmentFun : () -> Any
)

@Adaptive 
fun someFun(@Adaptive : f : () -> Any) {    
    f()
}
   
@Adaptive
fun useRef(config : Config) {
    someFun(config.fragmentFun)
}
```

## Implementation

```kotlin
import `fun`.adaptive.foundation.replacedByPlugin
import `fun`.adaptive.resource.file.FileResourceSet
import `fun`.adaptive.resource.file.Files

fun someFun0(i1 : Int, block : () -> Any) {  }
fun someFun1(i1 : Int, block : (i2 : Int) -> Any) {  }

fun fromKey(key : String) : Function0<Any> = replacedByPlugin("")
fun <P1> fromKey(key : String) : Function1<P1,Any> = replacedByPlugin("")

fun fromResource(resource : FileResourceSet) : Function0<Any> = replacedByPlugin("")
fun <P1> fromResource(resource : FileResourceSet) : Function1<P1,Any> = replacedByPlugin("")

fun anotherFun0() {  }
fun anotherFun1(i2 : Int) {  }

fun otherFun() {
    someFun0(12) {  }
    someFun0(23, ::anotherFun0)
    someFun0(34, fromKey("key"))
    someFun0(45, fromResource(Files.a))

    someFun1(12) {  }
    someFun1(23, ::anotherFun1)
    someFun1(34, fromKey<Int>("key"))
    someFun1(45, fromResource<Int>(Files.a))
}
```