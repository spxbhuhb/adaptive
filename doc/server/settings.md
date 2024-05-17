# Settings

> [!NOTE]
>
> This document describes the current implementation which is admittedly lacking.
> I plan to improve this in the future, but this is low priority for now.
> 
> What I plan to keep:
> - the configuration part, it's pretty, I like it
> - the `by setting<T>() { }` syntax, it's pretty
> 
> What I plan to change:
> - move settings into the adapter
> - the settings should be turned into state variables in the server fragment
> - the values should be deserialized with wireformat to support wider range
> - service settings should be cached and copied into service instances
> - I should be able to use settings in UI fragments as well

Setting providers load key-value pairs from various sources such as files, 
environment variables, SQL tables etc. 

The keys are strings, the values may be one of:

- Boolean
- Int
- Long
- String

The providers are typically configured during application startup like this:

```kotlin
settings {
    
    environment()
    
    propertyFile(optional = false) { "./etc/sandbox.properties" }

    inline(
        "EMAIL_HOST" to "localhost",
        "EMAIL_PORT" to 2500
    )
    
}
```

Once the providers are configured you can get settings in any server fragment implementation:

```kotlin
package app.pkg

class SomeWorker : ServiceImpl<SomeWorker> {
    
    val port by setting<Int> { "SOME_PORT" }
    
}
```

## Lookup Algorithm

The `setting` function asks the providers **in reverse order** to find the setting that belongs
to a given key. Assuming the example above, first the inline settings are checked, then the
ones from the property file and last the ones from the environment variables.
