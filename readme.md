[![Maven Central](https://img.shields.io/maven-central/v/fun.adaptive/core-core)](https://central.sonatype.com/search?q=fun.adaptive&name=core-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Adaptive is a fresh approach to Kotlin Multiplatform, a greenfield development with the
goal of comfortably writing full-stack, pure-Kotlin applications without relying 
on third-party, platform-dependent libraries.

Currently, Adaptive is under initial development and not yet ready for broader public use.
I've started the process to push the project into a public beta phase, but it will 
likely take a few months to reach that point.

Check [https://adaptive.fun](https://adaptive.fun) for more information. (Please use a desktop
browser; the page isn't mobile-friendly yet.)

## Compatibility matrix

Adaptive is currently a "bleeding-edge" project. This means the focus is on new features
and refactoring (when needed) rather than stability.

While this may change in the future, we do not currently guarantee API stability.

**NOTE**: Snapshot releases are available for 90 days as per Maven Central policy.

**NOTE**: Versions of dependencies in snapshot releases may change without notice.

| Adaptive                         | Kotlin         | Coroutines | Datetime | Ktor     |
|----------------------------------|----------------|------------|----------|----------|
| `0.25.724+2.2.0-SNAPSHOT`        | `2.2.0`        | `1.10.2`   | `0.7.1`  | `3.2.2`  |
| `0.25.719+2.2.20-Beta1-SNAPSHOT` | `2.2.20-Beta1` | `1.10.2`   | `0.4.0`  | `3.2.2`  |
| `0.25.718`                       | `2.1.20`       | `1.8.1`    | `0.4.0`  | `2.3.11` |