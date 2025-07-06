# Subscribing from a service implementation

Clients use [value subscriptions](def://) to receive updates when [values](def://) are changed. 

[Service implementations](def://) may subscribe on behalf of a client by calling the [subscribe](function://AvValueWorker)
function of a [value worker](def://).

[lib-value](def://) provides a few variants of `serverSubscribe` to make this easier.

The general pattern is to:

1. perform authorization
2. call [serviceSubscribe](function://) with the appropriate parameters as the example below shows
3. return with the list of conditions that belong to the subscription

[ServiceSubscribeExample](example://)

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
- a list of [AvSubscribeCondition](class://) instances

## Unsubscribe

To unsubscribe, the service can simply call [unsubscribe](function://AvValueWorker) with the subscription id.