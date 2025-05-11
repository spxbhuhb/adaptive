# Immutable Adat Classes

An [adat class](readme.md) is immutable when all of its properties are immutable.

> [!IMPORTANT]
>
> A property is immutable when:
> * it is declared as `val`
> * **AND** it has no getter
> * **AND** the value itself is immutable
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
these classes. The `copyOf` producer solves this problem.

## Adaptive copy

- The `copyOf` producer creates a new copy whenever the `update` function of the Adat instance is called.
- Built-in accessor based fragments (such as `editor`) automatically handle copies.

```kotlin
@Adat
class SomeAdat(
    val s1 : String,
    val s2 : String
)

@Adaptive
fun someEditor() {
    val someAdat = copyOf { SomeAdat("", "") }
    
    editor { someAdat.s1 }
    editor { someAdat.s2 }
}
```

- The `storeFor` function creates a fragment-independent store for an Adat instance.
- The `valueFrom` producer patches the fragment whenever the value in the store changes.

```kotlin
val globalStore = storeFor { SomeAdat("", "")  }

@Adaptive
fun someEditor() {
    val someAdat = valueFrom { globalStore }
    editor { someAdat.s1 }
    editor { someAdat.s2 }
}
```

### Updating values

To update value in a copy store, call the `update` extension function. 

1. `update` sends the change to the store
2. the store produces a new instance
3. `someAdat` is set to the new instance
4. all fragments that use `someAdat` are patched

```kotlin
@Adat
class SomeAdat(
    val s1 : String,
    val s2 : String
)

@Adaptive
fun someFun() {
    val someAdat = copyOf { SomeAdat("", "") }

    text(someAdat.s1) .. onClick { someAdat.s1.update { "new value" } }
}
```

### Nested instances

The copy store supports nested instances, so you can build complex data structures.

> [!NOTE]
>
> As of now, copy store **DOES NOT** support nested instances stored in lists, maps and sets.
>

```kotlin
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
    val someAdat = copyOf { SomeAdat("", SomeOtherAdat(12, YetAnotherAdat(true))) }

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
@Adaptive
fun someEditor() {
    val someAdat = copyOf { SomeAdat("") }
    
    text(someAdat.s1) .. onClick { someAdat.replaceWith(SomeAdat("Hello")) }
}
```