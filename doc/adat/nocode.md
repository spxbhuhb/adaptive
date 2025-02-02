# No-Code Adat classes

Adaptive supports data models without actual Kotlin code.

These are functionally equivalent to classes with Kotlin code except that there are no properties
to use in manually written code. You can still use `getValue` and `setValue` to change the values.

No-code Adat class support is provided by:
* `NoCodeAdatClass`
* `AdatClassIntersecion`
* `NoCodeAdatCompanion`

Ways to create no-code Adat classes:

* design your class with Data Model Architect and set the no-code option
* use `AdatCompanion.optional`:
  * creates an Adat companion from another with all properties writable and nullable
  * use `newInstance` of the created companion
* use `AdatClassIntersection`
  * uses `AdatCompanion.optional` to generate a no-code adat class with optional properties
  * calls a function whenever a property is set

## Adat class intersection

This functionality is useful when you have multiple instances of an adat class and want to
provide an editor on the UI that shows an intersection of these classes.

For example, you want to edit the padding of a few fragments at once. In this case:

* the editor field shows an actual value when the value is the same in all instances 
* the editor field shows a decoration when there are multiple different values
* if you change the value of the editor, all instances should change at once

