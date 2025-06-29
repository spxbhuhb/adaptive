# Self-observable

Self-observable instances are [observables](def://) which may notify [observers](def://)
on property value changes.

Not all property changes result in notification, some properties may be ignored. The
actual self-observable implementation decides which property changes require a
notification.

- [Producer](guide://)
- [lifecycle-bound state variables](def://)