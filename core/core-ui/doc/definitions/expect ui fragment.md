# Expect UI Fragment

An expect UI fragment is a [UI fragment](def://) that is explicitly marked with the
`@AdaptiveExpect` annotation. 

These fragments have separate platform-specific implementations for each [actual UI](def://) 
they support, allowing them to be used across different platforms where an implementation exists.

Expect fragments are typically implemented manually.