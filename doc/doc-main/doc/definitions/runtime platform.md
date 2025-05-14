# Runtime platform

A [runtime platform](def://) is the combined environment where your application code executes. It includes 
the underlying operating system and the specific software layer that directly runs the code. This layer can
be a web browser, a Java Virtual Machine (JVM), a Node.js server, or the native operating system environment
for a compiled application.

The [PlatformType](class://) enumeration class lists the supported platform types.

The [getPlatformType](function://) function returns with the [PlatformType](class://) of the current [runtime platform](def://).