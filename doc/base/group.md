# Group

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