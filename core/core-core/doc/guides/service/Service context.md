# What is a service context

A **service context** is created for each service call and is accessible through the `serviceContext`
property of `ServiceImpl`. It provides:

- **Session data** for the current service call (if one exists)
- **Connection lifecycle management** hooks
- **Messaging** capabilities back to the client

The `serviceContext` is available inside all `ServiceImpl` implementations and is automatically injected by the framework.

## What the ServiceContext Provides

A `ServiceContext` instance contains:

### Session-related Data

- `sessionUUID`: the UUID of the client session (if any)
- `owner`: the ID of the user associated with the session
- `roles`: the roles assigned to the user

### Utility Functions

- `isLoggedIn`: whether the user is authenticated
- `send`: send a message back over the connection
- `connectionCleanup`: register a callback when the connection is closed
- `sessionCleanup`: register a callback when the session is removed

These features are especially useful for managing interactive, multi-user environments.

## Usage Example

```kotlin
class HelloService : HelloServiceApi, ServiceImpl<HelloService>() {

    override suspend fun hello(myName: String): String {
        publicAccess()

        return if (serviceContext.isLoggedIn) {
            "Sorry, I can talk only with clients I know."
        } else {
            "Hello $myName! Your user ID is: ${serviceContext.owner}."
        }
    }
}
```

In this example:

* The serviceContext determines whether the client is logged in.
* If not, the service call returns a message including the user ID stored in the context.

## Notes

* You do not need to manually pass ServiceContext; it is automatically managed and available.
* Using connectionCleanup and sessionCleanup is recommended when allocating temporary resources per connection/session.
* If no session exists (i.e. the user is unauthenticated), owner and roles may be null or empty.

## See Also

- [Services](guide://)
- [Service transport](guide://)