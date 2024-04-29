# Tables

```kotlin
table {
    query = { bookStore.bookList() }
    
    rowNumber()
    rowSelect()
    viewer { row.x + row.y }
    editor { row.y }
    custom { render { text(blue) { row.x } } }
    
    action { row.x = row.y * 2 } label "set x to 2*y"
    action { row.y = row.x * 2 } label "set y to 2*x"
}
```

