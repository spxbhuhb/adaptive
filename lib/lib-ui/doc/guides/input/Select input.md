# Select

Select lets the user choose from options.

There are many configuration and rendering options. You can create a dropdown style select, 
a list of options or something like a choice-group, etc.

## Input examples

[examples](actualize://example-group?name=selectInput)

## Editor examples

[examples](actualize://example-group?name=selectEditor)

 
## Playground

[Select input playground](actualize://example/input/select/playground)

## Details

### Variants

**standalone**

* `selectInputDropdown`
* `selectInputList`
* `multiSelectInputList`

**editor**

* `selectEditorDropdown`
* `selectEditorList`
* `multiSelectEditorList`

**mapping editor**

* `selectMappingEditorDropdown`
* `selectMappingEditorList`
* `multiSelectMappingEditorList`

### Keyboard navigation

All single-select variants:

- `Up` selects previous item (wraps to first if at last)
- `Down` selects next item (wraps to last if at first)

Dropdown variant:

- `Down` opens the dropdown if closed
- `Escape` closes the dropdown
- `Enter` closes the dropdown

### Mapping

The mapping variants let you:

* use different types for the option list and th select value,
* provide a mapping function between the two

This is handy in many cases, for example, when you have a list of instances and the value
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

For standalone select, create the backend with one of:

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

multiSelectInputList(backend, ::selectInputItemCheckbox)
```

### Editor

```kotlin
selectEditorList(options, ::selectInputItemCheckbox) { template.selectedOption }

selectMappingEditorList(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectEditorList(options, ::selectInputItemCheckbox) { template.selectedOption }

multiSelectMappingEditorList(options, { it.uuid }, ::selectInputItemCheckbox) { template.selectedOption }
```