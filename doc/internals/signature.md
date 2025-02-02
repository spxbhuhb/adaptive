# Signature

## Usages

* identifying functions for service calls
  * `TransportEnvelope.funName` - set by `ConsumerClassTransform.transformServiceFunction`
* select the editor for a property
  * `AdaptiveStateVariableBinding.boundType`
* store types in serialized data
  * `AdatPropertyMetaData.signature`
* find the encoder/decoder for serialization
  * `WireFormatRegistry` - actually uses fully qualified name that comes from the signature

## Processing

* `fun.adaptive.wireformat.signature` package in `adaptive-core`
* `fun.adaptive.adat.wireformat` package
* `fun.adaptive.kotlin.wireformat` package

## Structure

Some types have shorthands (check [KotlinSignatures](/adaptive-core/src/commonMain/kotlin/fun/adaptive/wireformat/signature/kotlin.kt)):

* Kotlin primitive types
* Arrays of Kotlin primitive types
* String
* UUID (Adaptive UUID)

For other types:

1. `L`
2. fully qualified class name
3. (optional)
   1. `<`
   2. list of type parameters
   3. `>`
4. `;`
5. (optional) `?` - if the type is nullable

Examples:

| Signature                                                 | Type              |
|-----------------------------------------------------------|-------------------|
| `T`                                                       | `String`          |
| `[I`                                                      | `IntArray`        |
| `+I`                                                      | `UInt`            |
| `Lkotlinx.datetime.LocalDate;`                            | `LocalDate`       |
| `Lkotlin.collections.Set<Lkotlinx.datetime.LocalDate;?>;` | `Set<LocalDate?>` |
| `Lkotlin.collections.Map<T,*>;`                           | `Map<String,Any>` |

Type parameter list:

* comma separated list of:
  * signature of the type parameter if non-polymorphic
  * `*` if polymorphic

