# Inputs

There are three categories of inputs:

- primitive
- standalone
- editor

## Input Categories

### Primitive Inputs

Primitive inputs are the most basic, they are typically manually implemented using
[platform-dependent](def://) input classes and provide no styling, validation, etc.
A good example is `singleLineTextInput`.

You can use them to build your own custom inputs, but you might be better off
using standalone inputs or editors.

### Standalone Inputs

Standalone inputs use an `InputViewBackend` to store the state of the input.
Internally, `InputViewBackend` is quite complex as it provides decoration, validation, styling, etc.

That said, you don't really have to worry about this complexity as the actual usage is quite simple:

```kotlin
@Adaptive
fun askName() {
    
    val nameBackend = inputBackend("name") { label = "Name" }
    
    textInput(nameBackend)
    
    button("Submit") .. onClick { println(nameBackend.inputValue) }
}
```

The `inputBackend` function provides a builder which lets you set the label, placeholder,
change and validation callbacks, etc.

Standalone inputs are styled and automatically react/redraw when the input backend changes.
There are quite complex rules about how an input is styled, but you don't really have to worry about that.

### Editors

While you can build up quite complex forms using standalone inputs, it is better to use editors when
you have an Adat class to work with.

The reason is that editors are aware of Adat constraints, can update the Adat instance directly and
provide one-step validation for the edited data.

Editors use standalone inputs under the hood, but you don't have to create the states manually.

Instead, you create an `AdatFormViewBackend` with the `adatFormBackend` function. This backend
does the heavy lifting for you, just wrap whatever editor you want in a `localContext`.

When the user wants to submit the form you can call `isInvalid` to check if the form is valid.
`isInvalid` validates the Adat class according to the defined constraints and also executes
all manual validation checks you pass to `adatFormBackend`.

```kotlin
@Adat
class SomeData(
    val field1: String = "A",
    val field2: String = "B"
) {
    override fun descriptor() {
        properties {
            field1 blank false
            field2 minLength 5
        }
    }
}

@Adaptive
fun editSomeData() {
    val template = SomeData()
    val form = adatFormBackend(template)

    column {
        gap { 16.dp } .. width { 200.dp }

        localContext(form) {
            textEditor { template.field1 }
            textEditor { template.field2 }
        }

        button("Save") .. onClick {
            if (form.isInvalid(touchAll = true)) return@onClick

            println("saved: ${form.formValue.encodeToPrettyJson()}")
        }
    }
}
```

## Input validation

### Constraint-based validation

The `InputViewBackend.isInConstraintError` property stores the result of constraint-based validation.

There are different ways to perform this validation on an input, which one is used depends on the
actual situation:

- Adat metadata-based validation
- specific validation at the input level
- specific validation at the form level

Adat metadata-based validation is automatically performed when you use `adatFormBackend` and editors.
In this case you typically don't add input level validation, but you may want to provide one at form
level, for example, comparing two password fields:

```kotlin
val form = adatFormBackend(template) {
    expectEquals(it::password, it::passwordConfirm, dualTouch = true)
}
```

Input level validation may be used by setting the `validateFun` of any given input state:

```kotlin
val b = inputBackend("content") { validateFun = { it == "valid-content" } }
```

### Conversion Error

For many inputs it is possible that the actual value on the UI cannot be converted into
the proper type.

For example, when you have a numeric input, you want the user to be able to paste invalid
values into it. You don't want to refuse those as it would feel very strange for the user.
Instead, you let the user paste the invalid value and edit it to make it valid.

Let's say the user pastes "v=123" into a numeric field and then wants to delete the "v=". This
is a perfectly valid expectation.

However, those invalid values cannot be stored as `inputValue` is type safe.

The solution is the `InputViewBackend.isInConversionError` property which is true when the
value cannot be converted into a type safe instance.

### isInvalid

The `InputViewBackend.isInvalid` property is true when the input is invalid because of
constraint-based validation or conversion error.

## User feedback

Standalone and editor inputs change their appearance according to the input backend state:

- disabled
- focused valid
- focused invalid
- unfocused valid
- unfocused invalid

An input does not show as invalid before the first "touch". The input is touched when the
use changes the value or the `isInvalid` function of a form backend is called with `touchAll = true`.

This mechanism ensures that an empty form does not show all fields as invalid before the user
has even started to edit them.

## Focus

All inputs are focusable. Use the `focusFirst` instruction to focus on a given
input first.

>
> [!NOTE]
>
> I think `focusFirst` works only for singleLineTextInput for now. I'll add it to others as well.
> 