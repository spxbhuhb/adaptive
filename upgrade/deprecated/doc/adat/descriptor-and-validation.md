# Descriptor and validation

> [!IMPORTANT]
>
> The syntax of `descriptor` is **very strict**. You can use only constants and the pre-defined set of
> constraint/information functions. The reason for this is that the data you provide is
> serialized and the actual code is removed by the compiler plugin. This is a design decision
> which covers most use cases and makes it possible to keep Adat classes fully described by
> metadata.
>
> In case you need additional checks, you can override the `validate` function of the adat class
> and add your own failed constraints as needed.
>

## Descriptor

To specify constraints and provide additional information about the properties of an Adat class,
override the `descriptor` function:

```kotlin
@Adat
class TestAdat(
    val someBoolean: Boolean,
    val someInt: Int,
    val someString : String
) {
    
    override fun descriptor() {
        properties {
            someBoolean value true default false
            someInt minimum 23 maximum 100 default 12
            someString minLength 2 maxLength 4 default "1234" blank false pattern "[0-9]*" secret true
        }
    }

}
```

The descriptor is stored in:

* `AdatPropertyMetadata.descriptors` for each property (serialized version)
* `adatDescriptors` property of the companion object (actual instances)

## Validation

* producers such as `copyOf` or `autoItem`
  * automatically validate Adat instances based on the descriptor
  * perform the validation **before** patching fragments
* you can manually run validation by calling `validate`
* `validate` sets `adatContext.validationResult` (an instance of `InstanceValidationResult`)

UI fragments with property binding can check if there is a failed constraint for the given field and
show information for the user.