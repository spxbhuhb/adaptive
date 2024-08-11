# Immutable Adat Classes

An [adat class](README.md) is immutable when all of its properties are immutable.

> [!IMPORTANT]
>
> A property is immutable when:
> * it is declared as `val`
> * **AND** it has no getter
> * **AND** the value itself is immutable
>
>
>  So, `val l : MutableList` is **NOT** immutable as the value is mutable.
>

> [!NOTE]
>
> Immutability is tricky with collection types where `List`, `Map` and `Set` actually does not
> guarantee immutability as the actual instance may be mutable.
>
> I'll let this be **as is** for now, maybe I'll integrate with `kotlinx.immutable` later to make sure
> that what is immutable is actually immutable.
>

Immutable classes are handy to keep the application logic consistent. You don't have to
worry about values changing without the whole instance changing.

`AdatClassMetadata.isImmutable` is `true` when the class is immutable, `false` otherwise.

However, this comes with a problem when you want to provide a user interface to edit
these classes. The `copyStore` producer solves this problem.

## Copy Store

Adaptive provides a producer called `copyStore` that produces a new copy whenever a fragment
changes a property in the class.

The built-in fragments recognize that `someAdat` belongs to a copy store and instead changing the value in-place
they send a change request to the store. The store then produces a new value which triggers a patch
in the fragment.

(Actually, all fragments that use `AdaptiveStateValueBinding` work automatically with `copyStore`).

```kotlin
@Adat
class SomeAdat(
    val s1 : String,
    val s2 : String
)

@Adaptive
fun someEditor() {
    val someAdat = copyStore { SomeAdat("", "") }
    
    input { someAdat.s1 }
    input { someAdat.s2 }
}
```

### Nested instances

The copy store supports nested instances, so you can build complex data structures.

> [!NOTE]
>
> As of now, copy store **DOES NOT** support nested instances stored in lists, maps and sets.
> I'll add that in the near future as well.
>

```kotlin
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive

@Adat
class SomeAdat(
    val s1: String,
    val soa: SomeOtherAdat
)

@Adat
class SomeOtherAdat(
    val i1: Int,
    val yaa: YetAnotherAdat
)

@Adat
class YetAnotherAdat(
    val s2: String
)

@Adaptive
fun someEditor() {
    val someAdat = copyStore { SomeAdat("", SomeOtherAdat(12, YetAnotherAdat(true))) }

    yetAnotherEditor(someAdat.soa.yaa)
}

@Adaptive
fun yetAnotherEditor(yaa : YetAnotherAdat) {
    input { yaa.s2 }
}
```

### Replacing the top-level instance

To replace the whole instance of the copy store, use the `replaceWith` function:

```kotlin
import `fun`.adaptive.foundation.Adaptive

@Adaptive
fun someEditor() {
    val someAdat = copyStore { SomeAdat("") }
    
    text(someAdat.s1) .. onClick { someAdat.replaceWith(SomeAdat("Hello")) }
}
```