# Select

Select is a rather complex input with many configuration options and uses.

---

## Hard-coded examples

[Select input text example](actualize:///cookbook/input/select/example/text)

[Select input icon and text example](actualize:///cookbook/input/select/example/icon-and-text)

[Select input checkbox example](actualize:///cookbook/input/select/example/checkbox)

---
            
## Playground

[Select input playground](actualize:///cookbook/input/select/playground)

---

## Details

### Variants

**standalone**

* `selectInput`
* `multiSelectInput`

**editor**

* `selectEditor`
* `multiSelectEditor`

**mapping editor**

* `selectMappingEditor`
* `multiSelectMappingEditor`

### Mapping

The mapping variants let you:

* use different types for the option list and th select value,
* provide a mapping function between the two

This is very useful in many cases, for example, when you have a list of instances and the value
is a set of uuids of those instances.

### Item renderers

Select fragments lets you pass a render function which is then used to render the options
of the select.

Built-in item renderer functions:

* `selectInputItemText`
* `selectInputItemIconAndText`
* `selectInputItemCheckbox`

### Standalone

Select follows the standard input concept, the state is stored in (depending on the variant):

- `SingleSelectInputViewBackend`
- `MultiSelectInputViewBackend`

For standalone selects, create the backend with one of:

- `selectInputBackend`
- `multiSelectInputBackend`

```kotlin
val backend = selectInputBackend<String> {
    this.options = listOf("A", "B", "C")
    toText = { it }
}

selectInput(backend, ::selectInputItemCheckbox)
```

```kotlin
val backend = multiSelectInputBackend<String> {
    this.options = listOf("A", "B", "C")
    toText = { it }
}

multiSelectInput(backend, ::selectInputItemCheckbox)
```

### Editor

```kotlin
selectEditor(options, ::selectInputItemCheckbox) { template.selectedOption }

selectMappingEditor(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectEditor(options, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectMappingEditor(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }
```

You can check the recipe in the cookbook for more detailed examples.