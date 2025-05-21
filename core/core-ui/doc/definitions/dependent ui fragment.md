# Dependent UI fragment

A dependent UI fragment is a [platform-dependent](def://) [UI fragment](def://) that can only be used 
with a specific [actual UI](def://).

These fragments usually contain a direct reference to a leaf or container instance of that 
particular [actual UI](def://) (like a Node in JS, a View or ViewGroup in Android, or a UIView in iOS).

The actual implementations of [expect UI fragments](def://) are typically dependent fragments.