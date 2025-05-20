# Application backend

An application backend is a part of an [application](def://) responsible for performing background
work and providing capabilities, composed of [services](def://) and [workers](def://). 

Application backends exist in both [client applications](def://) and [server applications](def://).

Application backends in [server applications](def://) typically offer services for [client applications](def://) and other peers.

Application backends in [client applications](def://) typically provide services for that one actual client and run locally
on that client. For example, a browser client typically has a frontend, which handles the [user interface](def://),
and an application backend that handles data received asynchronously from the server.

In [Adaptive](def://) terminology, client-side and server-side application backends are both referred to as application 
backends because they utilize the same underlying technology and structural principles, although they work with different 
sets of [fragments](def://).

## See Also

- [backend fragment implementation](def://)
- [BackendAdapter](class://)