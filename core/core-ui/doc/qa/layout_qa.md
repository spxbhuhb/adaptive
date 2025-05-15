# Question

How can I make all [fragments](def://) in a column to have equal width based on the largest [fragment](def://).

# Answer

The [resizeMax](property://FillStrategy) property of the [FillStrategy](class://) [instruction](def://)
instructs [column](fragment://) and [row](fragment://) [layouts](def://) to resize each [fragment](def://) to the
size of the largest fragment.

Use the shorthand [resizeMax](property://fillStrategy) to pass the instruction easily:

```kotlin
column {
    fillStrategy.resizeMax
}
```