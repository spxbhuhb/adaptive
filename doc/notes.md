# Notes

These notes either describe ideas to be added or features that are added but not in use at the moment.

## Access Binding - Added

`AdaptiveStateVariableBinding` parameters paired with selector functions in the rendering are transformed
by the compiler plugin to provide metadata about the accessed state variable.

The accessor function may decide what to do based on the metadata. This makes it possible to
use the proper view for a state variable automatically.

```kotlin
@Adaptive
fun example() {
    val a = 12
    accessor { a }
}

@Adaptive
fun <T> accessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    selector: () -> T
) {
    checkNotNull(binding)
    if (binding.metadata.type == "kotlin.Int") {
        T1(binding.value as Int)
    }
}
```

## Group - Idea

The `group` fragment tells Adaptive that a set of fragments belong together.

```kotlin
group {
    search()
    table { /* ... */ }
    table { /* .. */ }
}
```

In the example above `search` adds an input field which acts as a filter for the rows of the tables.

`search` does not know about the tables themselves. It sends an operation to `group`. Group sends the operation
to the fragments in the group which may choose to ignore the operation or do something.

This also would be nice for servers.

## SetStateVariable - Just a Note

`AdaptiveFragment.setStateVariable`

- Ignores values that equals (`==`) with the current one in the state (returns immediately, nothing happens).

## Basics: Support Functions

**Added, but there is no filledButton**

*Support functions* are functions called when something happens: user input,
completion of a launched co-routine etc.

Support functions may change state variables and these changes result in a UI update.

You can define support functions as local functions or as lambdas. Adaptive recognizes
when a block changes a state variable and automatically updates the UI.

The handlers in this example are equivalent. Note that whichever button you
click, labels of all show the new counter value.

```kotlin
fun Adaptive.counter() {
    var counter = 0

    fun increment() {
        counter++
    }

    filledButton { "Click count: $counter" } onClick ::increment
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

## Transform - Added

Transforms change the state of a fragment **after** their external state variables are set but
**before** their internal state variables are calculated.

```kotlin
@Adaptive
fun example() {
    val authorFilter = ""
    editor { authorFilter }
    select() query { authorService.query(authorFilter) }
}

interface SelectTransformApi : AdaptiveTransformApi {
    infix fun query(value: suspend () -> List<String>) {
        setStateVariable(0, value)
    }
}

@Adaptive
fun select(query: (suspend () -> List<String>)? = null): SelectTransformApi {
    val authors = fetch(emptyList()) { query?.invoke() }

    /* ... */

    return thisState()
}
```

## Example for what is

```kotlin
    @Adaptive
fun subTitle(content: String) {
    text(content, *mediumTitle)
}

class AdaptiveSubTitle {

    val content by state<String>()

    fun genBuild(parent: AdaptiveFragment, index: Int) =
        CommonText(parent.adapter, parent, index)

    fun genPatchDescendant(fragment: AdaptiveFragment) {
        val child = children[0] as CommonText

        if (child.haveToPatch(content)) {
            child.setStateVariable(0, content)
        }

        if (child.haveToPatch(instructions)) {
            child.setStateVariable(1, instructionsOf(mediumTitle))
        }
    }

}
```

## Generate pictures with ChatGPT

```text
Adaptive is a friendly leaf. He is in a park, in a rain of many small colorful question marks, holding a big umbrella. Add visible, sharp environment. Make the question marks small.
Make a manga-like, colored picture of this with 1792x1024 resolution. 

Make him look like on the attached example picture.

Adaptive is a friendly leaf, who is standing in front of a chasm, looking down into the deepness.
Make a manga-like, colored picture of this with 1792x1024 resolution.

Make him look like on the attached example picture.
```