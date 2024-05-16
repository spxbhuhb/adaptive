# Exporting and Importing Fragments

Building applications with multiple modules (as in multiple separated projects) needs some manual 
configuration at the moment. This is a bit cumbersome but needed because of the current limitations 
of the compiler, see https://youtrack.jetbrains.com/issue/KT-58886.

* You have to export the fragments from the modules they are defined in.
* You have to import the fragments into the adapters they are used with.

If you forget export or import you'll get an exception during runtime.

## Export

When a library provides fragments for other modules, it has to have an export object as shown below.

The name and the place of this object is not important (I think...), the annotation and the superclass is.

```kotlin
@AdaptiveFragmentCompanionCollector
object SomeExport : AdaptiveFragmentFactory<Any>()
```

## Import

When a module uses fragments from other modules, it has to import those fragments as shown below.

```kotlin
browser(SomeExport) {
   /** ... **/   
}
```

## Background

The compiler plugin creates a companion object for each `public` adaptive function.
`internal` and `private` functions don't have a companion object as they cannot be exported. You
wouldn't be able to use them anyway as the Kotlin compiler would stop you from calling the function.

The compiler plugin also looks for classes with the `@AdaptiveFragmentCompanionCollector` annotation. If such
a class is found, the plugin adds an init block that calls the `addAll` function of `AdaptiveFragmentFactory`.
This call gets the companion of all public fragments defined in the module.

```kotlin
object exports : AdaptiveFragmentFactory<TestNode>() {
    init {
        addAll(SomeSome1, SomeCompanion2, SomeCompanion3)
    }
}
```