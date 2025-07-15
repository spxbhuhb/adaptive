# Values in the UI

[lib-ui](def://) supports using [values](def://) with a number of convenience functions 
and [fragments](def://).

There are two methods for getting a value to show on the UI: 

- Fetching gets the actual state of the value but **DOES NOT** receive updates afterward.
- Subscribing gets the actual state **AND** receives updates afterward.

## Fetching 

Fetching is simple, you can use [AvValueApi](interface://) or any other [service API](def://)
to get the value or a list of values.

1. From [view backends](def://) call the API directly.
2. From [fragments](def://) call a producer such as [fetch](function://AdaptiveFragment).

## Subscribing

Subscribe to [values](def://) to get the value and receive updates from the server until
unsubscribed.

1. From [view backends](def://) create a built-in subscriber, see [Values in view backends](guide://)
2. From [fragments](def://) use one of the [value producers](guide://)