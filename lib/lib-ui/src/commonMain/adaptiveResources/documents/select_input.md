# Select

Select lets the user choose from options.

There are many configuration and rendering options. You can create a dropdown style select, 
a list of options or something like a choice-group, etc.

---

## Hard-coded examples

[Select input dropdown example](actualize:///cookbook/input/select/example/dropdown)

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

* `selectInputList`
* `selectInputDropdown`
* `multiSelectInput`

**editor**

* `selectEditorList`
* `selectEditorDropdown`
* `multiSelectEditor`

**mapping editor**

* `selectMappingEditorList`
* `selectMappingEditorDropdown`
* `multiSelectMappingEditor`

### Mapping

The mapping variants let you:

* use different types for the option list and th select value,
* provide a mapping function between the two

This is very useful in many cases, for example, when you have a list of instances and the value
is a set of uuids of those instances.

### Option renderers

Select fragments lets you pass a render function which is then used to render the options
of the select.

Built-in item renderer functions:

* `selectInputOptionText`
* `selectInputOptionIconAndText`
* `selectInputOptionCheckbox`

### Value renderers

Dropdown select fragments lets you pass a render function which is then used to render the value
of the select.

This is different from rendering the options.

Built-in item renderer functions:

* `selectInputValueText`
* `selectInputValueIconAndText`

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

selectInputList(backend, ::selectInputItemCheckbox)
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
selectEditorList(options, ::selectInputItemCheckbox) { template.selectedOption }

selectMappingEditorList(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectEditor(options, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectMappingEditor(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }
```

You can check the recipe in the cookbook for more detailed examples.