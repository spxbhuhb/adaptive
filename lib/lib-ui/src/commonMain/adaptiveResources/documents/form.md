# Forms

You can build forms with standalone inputs or with editors.

Standalone input based forms may be useful in special situations, check the documentation
of inputs about them.

Editor based forms offer an easy way to create a from for an Adat class:

```kotlin
@Adaptive
fun accountEditor(
    account: AccountEditorData,
    save: (AccountEditorData) -> Unit
) {
    val form = adatFormBackend(account)
    
    column {
        width { 320.dp } .. gap { 16.dp }
    
        localContext(form) {
            textEditor { account.principalName }
            textEditor { account.name }
            textEditor { account.email }
        }
    
        saveFormButton(form) { save(it) }    
    }
}
```

Points of interest:

* `adatFormBackend` creates a view backend for your form fragment
* `textEditor` uses the backend and the information from the Adat metadata to create an editor
* `saveFormButton` is a utility to validate the form and:
  * call a function if valid
  * show a warning snack if invalid
* field labels are translated automatically

> [!IMPORTANT]
> 
> * the original `account` instance **IS NOT CHANGED**
> * `adatFormBackend` creates a copy and works on that copy
> * `adatFormBackend` is observable, it notifies when a field of the copy changes
> * `saveFormButton` passes the copy to the function parameter 
>

## Available editors

**primitive types**

* `booleanEditor`
* `doubleEditor`
* `intEditor`
* `textEditor`

**selects**

* `selectEditor`
* `selectMappingEditor`
* `multiSelectEditor`
* `multiSelectMappingEditor`

## Label translation

Field labels are translated automatically, assuming:

* there is an `AbstractApplication` context for the fragment
* a string resource with a key equivalent to the property name exists

The translation goes over the `stringStores` of the application and
returns with the first string with the proper key.

If there is no such key, the label will be the name of the property.

> 
> [!NOTE]
> 
> This mechanism does not take the module into account. I'll have to clarify this 
> later, but it's fine for now.
> 