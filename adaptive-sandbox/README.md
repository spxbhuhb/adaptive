# Sandbox

This project is a general playground to use during development.

* part of the root composite build
* two subproject:
  * lib - a library to test dependencies, exports, imports etc.
  * app - a server (JVM) and a UI (browser)

To run:

```shell
./gradlew adaptive-sandbox:adaptive-sandbox-app:run
./gradlew adaptive-sandbox:adaptive-sandbox-app:jsBrowserRun
```