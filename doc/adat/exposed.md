# Exposed

> [!CAUTION]
>
> This feature is even more experimental than the others.
>

Adaptive provides automatic mapping between Exposed tables and Adat classes.
To use this feature you have to:

- add the `@ExposedAdatTable` annotation to your table
- extend a provided table class

As of now, we have two table classes:

- `AdatTable` - extends `Table` class of Exposed
- `AdatEntityTable` - extends `UUIDTable` class of Exposed

## AdatTable

This table has no special column for ID, therefore there is no `update` and `remove` function
out-of-the-box.

```kotlin
@Adat
class TestData(
    val someInt: Int,
    val someBoolean: Boolean
) : AdatClass<TestData>

@ExposedAdatTable
class TestTable : AdatTable<TestData, TestTable>() {

    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")

}
```

```kotlin
TestTable().apply {

    // to add a new record
    add { TestData(uuid, 12, "Hello World!") }

    // to list all records
    all().forEach { println(it) }

}
```

## AdatUuidTable

> [!IMPORTANT]
>
> For ID tables, the `id` field is defined by Exposed, you don't have to define it. In the adat class
> you have to include the `id` field.
>

```kotlin
@Adat
class EntityTestData(
    override val id: UUID<UuidTestData>,
    val someInt: Int,
    val someBoolean: Boolean
) : AdatEntity<UuidTestData>

@ExposedAdatTable
class EntityTestTable : AdatEntityTable<EntityTestData, EntityTestTable>() {

    val someInt = integer("some_int")
    val someBoolean = bool("some_boolean")

}
```

For these tables the basic CRUD functions are available automatically:

```kotlin
EntityTestTable().apply {
    val uuid = UUID()

    // to add a new record
    add { EntityTestData(uuid, 12, "Hello World!") }

    // to update an existing record
    update { EntityTestData(uuid, 23, "Hello!") }

    // to delete a record
    delete(uuid)

    // to list all records
    all().forEach { println(it) }
}
```