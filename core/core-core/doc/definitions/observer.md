# Observer

Observers get notification when an [observable](def://) changes.

Each observer has to add itself as a listener for notifications to the given [observable](def://).

Each observer has to remove itself as a listener for notification from the given [observable](def://) when
the notifications are no longer needed (for example, when the listener is disposed).

## See also

- [what is a producer](guide://)
- [lifecycle-bound state variables](def://)