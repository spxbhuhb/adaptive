# Immutable Adat Classes

An adat class is immutable when all of its properties are immutable.

> [!IMPORTANT]
>
> A property is immutable when it is declared as `val` **AND** the value itself is
> immutable. So, `val l : MutableList` is **NOT** immutable as the value is mutable.
>

Immutable classes are handy to keep the application logic consistent. You don't have to
worry about values changing without the whole instance changing.

`AdatClassMetadata.isImmutable` is `true` when the class is immutable, `false` otherwise.

However, this comes with a problem when you want to provide a user interface to edit
these classes.

## Copy Store

Adaptive provides a producer called `copyStore` that produces a new copy whenever an editor
would change a property in the class:

```kotlin
@Adat
class SomeAdat(
    val s1 : String,
    val s2 : String
)

@Adaptive
fun someEditor() {
    val someAdat = copyStore(SomeAdat("",""))
    
    input { someAdat.s1 }
    input { someAdat.s2 }
}
```

The editors recognize that `someAdat` belongs to a copy store and instead changing the value in-place
they send a change request to the store. The store then produces a new value which triggers a patch
in the fragment.