* Do NOT start a Gradle build.
* NEVER start a Gradle build.
* DON'T EVER start a Gradle build.

## Basic concepts

For basic concepts see:

- [Documentation system](/doc/doc-main/doc/guides/Documentation%20system.md)
- [Documentation](/doc/doc-main/doc/guides/Documentation.md)

## Examples

Examples are located in the `doc/doc-example/src/fun/adaptive/doc/example` directory.
The structure of the example system is described in the [Documentation system](/doc/doc-main/doc/guides/Documentation%20system.md) guide.

## Fragments

Functions annotated with `@Adaptive` define `fragments`.

When working with fragments, keep these in mind:

- [Fragments](/core/core-core/doc/guides/foundation/Foundation.md)
- [Instructions](/core/core-core/doc/guides/foundation/Instruction.md)

Especially pay attention to the boundary between the state definition and rendering part
of the fragment as described in the [Instruction](/core/core-core/doc/guides/foundation/Instruction.md) guide.

Keep in mind that you cannot add instruction between the fragments. You can add them:

1) right after the opening `{` of an Adaptive lambda
2) as function parameters if the called function supports it
3) after a rendering statement if it returns with an `AdaptiveFragment` using the `..` operator

## Comments

Do NOT add trivial comments like this:

```kotlin
// 1) Flatten all paths
val allPaths = collection.values.flatten()
```

Do NOT add trivial function/class documentation like this.
If you don't know what it does, don't add it.

```kotlin
/**
 *  Calculates the width.
 *  
 *  @param availableWidth the available width
 */
fun calculateWidth(availableWidth: Int) {
    // ...
}
```