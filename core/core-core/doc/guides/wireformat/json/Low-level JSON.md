# Low-level JSON

The low-level JSON classes act as a middle layer between textual JSON and high-level
data models.

All of these classes:

- extend the `JsonElement` abstract class
- store their value in the `value` property (except `JsonNull`)
- support visitors and transformers
- support encoding into JSON with `toString()`

# Components

## Element classes

* `JsonNull` - represents a `null` value
* `JsonBoolean` - represents a boolean, `value` is a Boolean
* `JsonNumber` - represents a number, `value` is a String
* `JsonString` - represents a string, `value` is a String
* `JsonArray` - represents an array, `value` is a mutable list of `JsonElement`
* `JsonObject` - represents an object, `value` is a mutable map of String - `JsonElement` pairs

## Helper functions

* `Path.readJson` - read the content of the file and parse it into JSON
* `Path.writeJson` - encode a `JsonElement` and write it into the file
* `JsonElement.asPrettyString` - get a prettified JSON string of the element
* `JsonElement.toPrettyString` - format the element into a prettified String with the given formatter

## Visitors and transformers

JSONS support visitor/transformer pattern with:

* [JsonVisitor](class://)
* [JsonTransformer](class://)
* [JsonTransformerVoid](class://)
* [JsonTransformerVoidWithContext](class://)

[ExampleJsonTransformer](example://)

[jsonTransformExample](example://)