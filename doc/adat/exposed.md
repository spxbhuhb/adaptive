# Exposed

Adaptive provides automatic mapping between Exposed tables and Adat classes.
To use this feature you have to:

- add the `@ExposedAdat` annotation to your table
- extend a provided table class

As of now, we have two table classes:

- `AdatTable` - extends `Table` class of Exposed
- `AdatUuidTable` - extends `UUIDTable` class of Exposed

