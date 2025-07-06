# How to write a basic application

Directories and files of a very minimal application project.
You can find the actual source code in the `sandbox/sandbox-app-echo` directory.

[sandbox-app-echo](dirTree://sandbox/sandbox-app-echo)

## API

The client and the server side of the application communicate through APIs. In
the particular case of this example application, [EchoApi](class://) is used.

## Server Side

The server side has:

- an application entry point [main](fun://sandboxAppEchoMain.jvm)
- a server module [EchoServerModule](class://)
- a [service implementation](def://) [EchoService](class://)

The application entry point:

- loads the application configuration according to the `settings` block
- uses JSON for client-server communication (default is `proto`)
- creates a `JvmServerApplication` and adds modules according to the `jvmServer` block
- starts the server application (which in turn will start Ktor as `KtorJvmServerModule` is added)

## Client Side

The client side of the application has:

- an application entry point [main](fun://sandboxAppEchoMain.js)
- a client module [EchoClientModule](class://)
- a main UI fragment [echoFrontendMain](fun://)

The application entry point:

- starts a basic browser application (without workspace support)
- uses JSON for client-server communication (default is `proto`)