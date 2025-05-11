---
title: Transform a JSON file
tags: [core-core, json, transform, file]
type: procedural
---

# Summary

This document describes how to load, process and save the content of a JSON file using the 
JSON transformer provided by Adaptive.

# Objective

Load, modify and save the content of a JSON file with the following original content:

```json
{
    "name": "test",
    "age": 12,
    "address": {
        "street": "test street",
        "number": 45
    }
}
```

The modified file should contain `13` as `age`.

# Steps

## 1. Write a transformer

### Purpose

Write a transformer that is able to transform a JSON structure.

### Code

```kotlin
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.visitor.JsonTransformerVoidWithContext

class ExampleJsonTransformer : JsonTransformerVoidWithContext() {

    override fun visitNumber(jsonNumber: JsonNumber): JsonElement {
        return if (keyOrNull == "age") JsonNumber(13) else jsonNumber
    }
    
}
```

### Explanation

Adaptive provides visitors and transformers to inspect and process JSON structures.

This transformer checks if the given `JsonNumber` instance is part of a `JsonObject` in an object with the key `age`, if so, 
changes the number to `13`.

The `keyOrNull` function returns with `null` of the number is not part of a `JsonObject`.

## 2. Write a function that performs the transformation

### Purpose

This function will be used to transform the content of a JSON file.

### Code

```kotlin
package `fun`.adaptive.wireformat.json.training

import `fun`.adaptive.utility.readJson
import `fun`.adaptive.utility.writeJson
import kotlinx.io.files.Path

fun main(argv: Array<String>) {

    val path = Path(argv[0])
    
    val original = path.readJson() ?: throw IllegalArgumentException("File ${path.name} is empty")

    val transformed = original.accept(ExampleJsonTransformer(), null)

    path.writeJson(transformed, overwrite = true)

}
```

### Explanation

This function uses the transformer from Step 1 and a few utility functions to read and write file content.

`path.readJson` reads the content of a file, parses it as JSON and returns with the root `JsonElement`.
`JsonElement.accept` executes the given transformer on the element and returns with the transformed one.
`path.writeJson` writes the content of the JSON to the file at the path.