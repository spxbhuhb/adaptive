# Introduction

Adaptive is a consolidated application platform for Kotlin Multiplatform.

I've asked ChatGPT: "How would you explain a consolidated application platform?"

Here is the answer, and surprisingly, it is very close to what Adaptive is (and/or hopefully will become):

> At its heart, a consolidated application platform reduces complexity by bringing
> together disparate functionalities that developers and IT teams typically
> use separately. These include:
>
> * Application development frameworks (e.g. web, mobile, backend)
> * Deployment tools (e.g. CI/CD pipelines, container orchestration)
> * Infrastructure management (e.g. cloud provisioning, scaling)
> * Monitoring and logging
> * Security and access control
> * APIs and integrations
>
> Rather than stitching together many vendors and tools manually, a
> consolidated platform provides them under one roof—either
> as a single product or as a tightly integrated suite.

Adaptive lets us write full-stack applications purely in Kotlin, without relying on any
third-party libraries.

This is a bold statement, and the best part is that it is true. As of now, Adaptive
has these runtime dependencies (Android has a bit more like appcompat, but that is unavoidable):

* `kotlinx.coroutines`
* `kotlinx.datetime`
* `kotlinx.io`
* `Ktor` (optional)
* `logback` (optional, but needed for Ktor)

**That's it. No Compose, no react, no Bootstrap, no 435 different libraries.**

**Everything is written from scratch, in Kotlin.**

I know that writing everything myself is a controversial topic, and many will say,
"Don't reinvent the wheel." While there's some truth to that, consider these two thoughts:

- Do F1 race cars run on the wooden wheels of medieval wagons?
- Why are there 3 million packages on NPM?