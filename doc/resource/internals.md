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

## String resources

Each original string resource file (`.xml`) is compiled into an `.arv` (Adaptive Resource Values) file.

In addition, the following code is generated:

```kotlin
object Strings

val Strings.hello: StringResource
    get() = CommonMainStrings.hello

private val qNone = setOf()
private val qHu = setOf(LanguageQualifier("hu"))

private object CommonMainStrings : StringResourceSet(
    ResourceFile("adaptiveResources/sandbox.lib/values/strings.avr", qNone),
    ResourceFile("adaptiveResources/sandbox.lib/values-hu/strings.avr", qHu)
) {
    val hello = string(0)
}
```

### Loading

Applications load the `.avr` of the active language during startup. It makes no sense 
to delay the loading as the very first screen will most probably show some strings.

`StringResourceSet.load()` is used to load the strings.

### Binary Format

The `.avr` files store the strings in a binary format which contains three blocks:

* header
* offset table
* values

The header contains the file version and the number of values in the file, both as 32-bit integers.

The offset table contains `value-number * 2` 32-bit integers, the offset and length of each value.
Offset is the absolute position of the value in the file.

Values are put one after each other without separators, encoded in UTF-8.

Example (header and offset table in decimal text for readability):
```text
00000001 00000002 // version = 1, value count = 2
00000024 00000002 // offset of the first value, length of the first value
00000026 00000003 // offset of the second value, length of the second value
aabbb             // first value = "aa", second value = "bbb" 
```

### Data size

These are from ChatGPT, so take them with a grain of salt.

The number of strings present in a mobile application can vary widely depending on the complexity,
purpose, and functionality of the app. However, here are some general estimates for different types
of mobile applications:

* Simple Apps (e.g., calculator, flashlight, basic utility apps):
    * Number of Strings: 50-200
* Moderate Complexity Apps (e.g., social media, news, fitness tracking):
    * Number of Strings: 200-1000
* Complex Apps (e.g., e-commerce, banking, extensive productivity tools):
    * Number of Strings: 1000-5000
* Highly Complex Apps (e.g., large-scale enterprise apps, comprehensive games, multi-functional platforms):
    * Number of Strings: 5000+

With 5000 strings and average size of 50 characters/string and 25 characters of metadata, the total size is 375 KB.
If we double the bytes needed per character (Unicode), that is 625 KB.

### Generated code size

**These calculations are based on the code I copied from Compose in May 2024.** Most probably it will get optimized,
but it is good to have some pointers to base our future development on.

Using the following string resource:

```xml
<resources>
    <string name="app_name">"Good Morning"</string>
    <string name="snooze">Snooze</string>
    <string name="sleepiness">Sleepiness:</string>
    <string name="by_joining">By joining you agree to our</string>
    <string name="terms_of_service">Terms of Service</string>
    <string name="and">and</string>
    <string name="privacy_policy">Privacy Policy</string>
</resources>
```

I get these file sizes (~11 KB):

```text
-rw-r--r--  1 tiz  staff  3935 Jul 10 03:51 CommonMainString0.class
-rw-r--r--  1 tiz  staff   721 Jul 10 03:51 Res$string.class
-rw-r--r--  1 tiz  staff  2192 Jul 10 03:51 Res.class
-rw-r--r--  1 tiz  staff  4425 Jul 10 03:51 String0_commonMainKt.class

```

Most of it is probably just overhead. Duplicating the strings:

```xml
<resources>
    <string name="app_name">"Good Morning"</string>
    <string name="snooze">Snooze</string>
    <string name="sleepiness">Sleepiness:</string>
    <string name="by_joining">By joining you agree to our</string>
    <string name="terms_of_service">Terms of Service</string>
    <string name="and">and</string>
    <string name="privacy_policy">Privacy Policy</string>

    <string name="app_name2">"Good Morning"</string>
    <string name="snooze2">Snooze</string>
    <string name="sleepiness2">Sleepiness:</string>
    <string name="by_joining2">By joining you agree to our</string>
    <string name="terms_of_service2">Terms of Service</string>
    <string name="and2">and</string>
    <string name="privacy_policy2">Privacy Policy</string>
</resources>
```

Results in (~17 KB):

```text
-rw-r--r--  1 tiz  staff  6479 Jul 10 03:49 CommonMainString0.class
-rw-r--r--  1 tiz  staff   721 Jul 10 03:49 Res$string.class
-rw-r--r--  1 tiz  staff  2192 Jul 10 03:49 Res.class
-rw-r--r--  1 tiz  staff  7471 Jul 10 03:49 String0_commonMainKt.class
```

So, by adding 81 chars of keys and 89 chars of data **~ 170 bytes**, the size of the generated code
grown by **~ 5.6 KB**, that is **times 32**.

Projecting this increment to size calculations for 5000 strings, the generated code would be **375 KB * 32 = 12 MB**.

I really think that in case of built-in application strings, the size of the actual data is absolutely negligible, the
size of the generated code is a real problem on the other hand. Especially that this is for one language, no qualifiers.