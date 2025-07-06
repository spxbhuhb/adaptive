# How to define a value domain

[value domain](def://?inline)

## Details

[Value domains](def://) are defined by [value domain definitions](def://).

To create one, extend [AvValueDomainDef](class://) and use provided builder functions:

- [marker](function://AvValueDomainDef), 
- [refLabel](function://AvValueDomainDef)
- [status](function://AvValueDomainDef)
- [tree](function://AvValueDomainDef)

Domain definitions are typically implemented as Kotlin objects with an accompanying 
property called `avDomain` that is internal and its getter returns with the object.

This pattern ensures that:

- the [value domain definition](def://) is available for other modules
- the general name `avDomain` is not polluting the common namespace
- inside the module it is easy to reference to the domain definition

[exampleDomainDef](example://)

## See also

- [value](def://)
- [value store](def://)
- [value domain definition](def://)
- [building value trees](guide://)