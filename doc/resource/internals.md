# Internals

The resources module started as a fork of Compose Resources. That said, I've decided to
rewrite how resources are handled as I think the original implementation generates too
much code (see [Generated Code Size](#generated-code-size)).

## Data Model

* At high level, each resource in the application is represented by a *type* and a *name*.
* Each resource may have *versions*, each *version* has a unique set of *qualifiers*.
* The code that references the resource typically does not care about which *version* of the resource is used.
* The *version* to be used is decided by a lower layer, based on the environment and/or settings.
* Any given resource *version* have one, and only one, actual file that contains that version.

Resource types **at file level**:

* image (bitmap, typically encoded in a format such as JPG, PNG etc.)
* svg
* file (any binary data)
* font
* string table

All the types above can be represented by the `ResourceFile` and `ResourceSet` classes:

```kotlin
class ResourceFile(
    val path : String,
    val qualifiers : Set<Qualifier>
)

class ResourceSet(
    vararg files : ResourceFile
)    
```

### Whole-file resources

```kotlin
object Images

val Images.background: ImageResource
  get() = CommonMainImages0.background

private val qDark = setOf(ThemeQualifier.DARK)
private val qLight = setOf(ThemeQualifier.LIGHT)

private object CommonMainImages0 {
    val background by lazy { init_background() }
}

private fun init_background() =
    FileResourceSet(
        ResourceFile("adaptiveResources/sandbox.lib/image-light/background.jpg", qDark),
        ResourceFile("adaptiveResources/sandbox.lib/image-dark/background.jpg", qLight)
    )
```

