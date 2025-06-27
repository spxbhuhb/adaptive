# Standalone double input

## Hard-coded examples

[Double inputs](actualize:///cookbook/input/double/example/common)

## Details

**Variants**

* `doubleInput`
* `doubleOrNullInput`
* `doubleUnitInput` - shows a unit
* `doubleOrNullUnitInput` - shows a unit

```kotlin
val value = 12.3

withLabel("Just a number") { state ->
    doubleUnitInput(value, decimals = 2, unit = "m²", state = state) { value = it }
}
```

**Notes**

* Formatting (decimals) is not applied when the user edits the value, this is intentional.

[commonDoubleInputExample](example://)