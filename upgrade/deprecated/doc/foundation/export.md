# Exporting and Importing Fragments

Building applications with multiple modules (as in multiple separated projects) needs some manual 
configuration at the moment. This is a bit cumbersome but needed because of the current limitations 
of the compiler, see:

* https://youtrack.jetbrains.com/issue/KT-58886
* https://youtrack.jetbrains.com/issue/KT-55982

Because of these issues I cannot make the fragment export/import fully automatic at the moment.

* You have to export the fragments from the modules they are defined in.
* You have to import the fragments into the adapters they are used with.

If you forget export or import you'll get an exception during runtime.

## Export

When a library provides fragments for other modules, it has to have an export object as shown below.

```kotlin
object SomeExport : AdaptiveFragmentFactory<Any>() {
    init {
        add("common:text") { p,i -> AdaptiveText(p.adapter as AdaptiveBrowserAdapter, p, i) }
    }
}
```

## Import

When a module uses fragments from other modules, it has to import those fragments as shown below.

```kotlin
browser(SomeExport) {
   /** ... **/   
}
```