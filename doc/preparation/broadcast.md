# Broadcast

The service broadcast fragment lets you broadcast service calls to multiple recipients.

```kotlin
server {
    broadcast<CounterApi> {
        service { SingleCounterService() }
        service { DoubleCounterService() }
        service { TripleCounterService() }
    }
}
```

All the services under `broadcast` have to implement the service API of `broadcast`, `CounterApi` in the example.

When a client calls the `CounterApi` service the appropriate service function of **all** services is be called.