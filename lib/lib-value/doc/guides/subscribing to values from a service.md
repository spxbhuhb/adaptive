# Subscribing from a service

Clients use subscriptions to receive updates when values are changed. To subscribe, the client typically
calls a service function with in turn calls the value worker's `subscribe` function.

[lib-value](def://) provides a few variants of `serverSubscribe` to make this easier.

The general pattern is to:

1. perform authorization
2. call `serverSubscribe` with the appropriate parameters as the example below shows
3. return with the list of conditions that belong to the subscription

```kotlin
class ExampleService : ServiceImpl<ExampleService>, ExampleApi {

    val valueWorker by worker<AvValueWorker> { it.domain == "general" }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        ensureLoggedIn()
        return serviceSubscribe(valueWorker, subscriptionId, ExampleMarkers.EXAMPLE_MARKER)
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureLoggedIn()
        valueWorker.unsubscribe(subscriptionId)
    }
}
```

This pattern is important because:

1. The subscription id is specified by the client, so it is known before the service call.
    1. This is needed to ensure proper cleanup in the case of fast unsubscribing.
2. The exact conditions are determined by the service implementation.
    1. The client has to know the conditions to be able to perform the local subscription.

## ServiceSubscribe Variants

The `serviceSubscribe` function has several variants to make it easier to subscribe to values.

All variants take the value worker and subscription id as the first and second parameters.

The third parameter may be:

- a value ID
- a vararg of markers
- a list of `AvSubscribeCondition` instances

## Unsubscribe

To unsubscribe, the service can simply call `AvValueWorker.unsubscribe` with the subscription id.