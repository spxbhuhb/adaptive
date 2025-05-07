---
language: kotlin
tags: [lib-app, application, module, ui, server, workspace]
title: Application Module Directory Structure
---

## Summary

These steps create the directory structure for a new application module with:

- a data model
- an API interface 
- a server side service that implements the API
- a client side UI that provides a workspace-based editor

## Objective

Create the directory structure for a new application module with the following parameters:

- parent package: fun.adaptive
- module name: example

## Steps

### 1. Directory for the module

**Directory**: `src/commonMain/kotlin/fun/adaptive/example`

### 2. Directory for the API interface

**Directory**: `src/commonMain/kotlin/fun/adaptive/example/api`

### 3. Directory for the application module definition classes

**Directory**: `src/commonMain/kotlin/fun/adaptive/example/app`

### 4. Directory for the data model

**Directory**: `src/commonMain/kotlin/fun/adaptive/example/model`

### 5. Directory for the server-side implementation

**Directory**: `src/commonMain/kotlin/fun/adaptive/example/server`

### 6. Directory for the workspace-based UI

**Directory**: `src/commonMain/kotlin/fun/adaptive/example/ws`