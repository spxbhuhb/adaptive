# Lifecycle-bound state variable

Lifecycle-bound state variables are [internal state variables](def://) bound to
the [fragment lifecycle](def://) and are automatically notified about [fragment lifecycle](def://) 
changes.

This may be needed when the state variable holds something that requires a cleanup action, 
such as a subscription that requires an unsubscription.

The compiler plugin recognizes lifecycle-bound state variables and adds the necessary
code to notify them on [fragment lifecycle](def://) change.

# See also

- [fragment lifecycle](def://)
- [state variable](def://)
- [internal state variable](def://)
- [LifecycleBound](class://)