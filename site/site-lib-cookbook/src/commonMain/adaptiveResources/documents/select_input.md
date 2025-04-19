---

# Documentation

Select offers these variants:

**standalone**

* `selectInput` - `selectInputBackend`
* `multiSelectInput` - `multiSelectInputBackend`

**editor**

* `selectEditor`
* `multiSelectEditor`

Select follows the standard input concept, the state is stored in an `SingleSelectInputViewBackend` or 
in a `MultiSelectInputViewBackend` depending on the variant used.

The input fragments have two parameters: the backend and the Adaptive function to render the items.

Built-in item renderer functions:

* `selectItemText`
* `selectItemIconAndText`
* `selectItemCheckbox`

To create a standalone select, use the input backend builder provided by `selectInputBackend`:

```kotlin
val backend = selectInputBackend<String> {
    this.options = listOf("A", "B", "C")
    toText = { it }
}

selectInput(backend, ::selectInputItemCheckbox)
```

To create an editor select:

```kotlin
selectEditor(options, ::selectInputItemCheckbox) { template.selectedOption }
```

You can check the recipe in the cookbook for more detailed examples.