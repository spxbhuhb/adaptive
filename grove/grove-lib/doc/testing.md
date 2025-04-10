# Testing

Testing related properties of `SheetViewController`:

| Property           | Purpose                                          |
|--------------------|--------------------------------------------------|
| `printTrace`       | prints operation trace to standard output        |
| `recordOperations` | records the operations into `executedOperations` |
| `recordMerge`      | applies operation merge on recorded operations   |
| `recording`        | the recorded operations                          |
| `snapshot`         | a snapshot of sheet items                        |

You can easily create a snapshot of the sheet like this:

```kotlin
controller.snapshot.encodeToJson()
```

The snapshot contains:

- models
- items (as `ClipboardItem`)
- operations (if recording is enabled, empty list otherwise)