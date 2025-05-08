---
title: How to Implement a CRUD Like Functionality with Value Store
tags: [ value, crud ]
type: procedural
---

# Summary

This guide will show you how to implement a CRUD like functionality with value store.

# Objective

Given a `T` spec class, implement a CRUD like functionality with a service API and implementation
for `AvItem<T>` using a value store with the following parameters:

package: my.project.example
name: ExampleCrud

# Steps

## 1. Write a Service API

**File**: `src/commonMain/kotlin/my/project/example/api/ExampleCrudApi`

### Purpose

The API is used by the client to interact with the service. The CRUD like functionality is defined
by this API.

### Code

```kotlin
@ServiceApi
interface ExampleCrudApi {

}
```