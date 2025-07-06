# Communication

While you can use any kind of communication, Adaptive offers a built-in, Websocket-based
communication implementation. With [services](def://) you can simply call suspend
functions defined by an interface on the client side and have the implementation 
executed on the server side.

The current implementation uses Ktor. It is not mandatory, you can easily use another
communication stack if you would like.