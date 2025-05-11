# Adat

## Exposed mapping

The plugin can generate mapping between Exposed tables and Adat classes.

### Mechanism

* The mapping is generated when the class has an `@ExposedAdat` annotation.
* Collect all properties which are of type `Column`.
* Get the Adat class from the type parameters of the supertype (first one).
* Collect all properties of the Adat class.
* Check if the `fromRow` function is manually implemented.
    * If not, replace the fake override with an actual implementation.
* Check if the `toRow` function is manually implemented.
    * If not, replace the fake override with an actual implementation.

#### fromRow

```kotlin
override fun fromRow(row : ResultRow): RoleContext =
    RoleContext(
        row[id].asCommon(),
        row[name],
        row[type]
    )
```

* `IrConstructorCallImpl` for the Adat class.
* `putValueArgument` for each Adat property.
    * `IrCallImpl`
        * `symbol = pluginContext.resultRowGet`
        * `dispatchReceiver = irGet(fromRow.getValueParameter(0))` - the ResultRow instance
        * `putValueArgument(0, columns[property])`

#### toRow

```kotlin
override fun toRow(row : UpdateBuilder<*>, value : RoleContext){
    row[id] = value.uuid.asJvm()
    row[name] = value.name
    row[type] = value.type
}
```
