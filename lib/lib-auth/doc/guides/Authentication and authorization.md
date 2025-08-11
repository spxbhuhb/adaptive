---
status: review
---

# Authentication and authorization

- [lib-auth](def://) provides the backends for authentication and authorization
- [lib-app](def://) provides some frontends, see [Built-in lib-app features](guide://)

User authentication, authorization and management features. Provides APIs:

- `AuthPrincipalApi`
- `AuthRoleApi`
- `AuthSessionApi`

It is **very important** that most of `lib-auth` works with principals, not accounts.

There is a very basic account implementation for testing purposes, see basic account below.
but the main focus of the module is to provide authentication and authorization.

- Authentication is based on *principals* and *credentials*.
- Authorization is based on *roles* and *principal ids*.

`lib-auth` uses the value store from `lib-values`, and so the basic data model is built with markers and
specs. See the documentation of `lib-values` for more information about these concepts.

## Server components and bootstrap

`AuthServerModule` adds these backend fragments:

```kotlin
service { AuthPrincipalService() }
service { AuthRoleService() }
service { AuthSessionService() }

worker { AuthSessionWorker() }
worker { AuthWorker() }
```

`AuthWorker` performs a very basic bootstrap mechanism:

- if there is no role with the `security-officer` marker, creates one
- sets `AuthWorker.securityOfficer` to the id of the role with the `security-officer` marker
- if there is no principal with the security officer role, creates one (name "so", password "so")
- if the basic account module is present
  - if there is no `accountRef` for the security officer principal
    - creates a basic account
    - sets the `accountRef` of the security-officer principal to the created basic account

## Defs

### Markers

`AuthMarkers`

| Name               | Applied with spec  | Meaning                                                                 |
|--------------------|--------------------|-------------------------------------------------------------------------|
| `principal`        | `PrincipalSpec`    | The value is a principal item.                                          |
| `credentialList`   | `PrincipalSpec`    | Id of the credential list marker value (`CredentialList` value type).   |
| `accountRef`       | `PrincipalSpec`    | Id of the account that belongs to this principal.                       |
| `role`             | `RoleSpec`         | The value is a role item.                                               |
| `security-officer` | `RoleSpec`         | Principals with this role can modify principals and roles.              |
| `basic-account`    | `BasicAccountSpec` | The value is a basic account item, see [basic account](#basic-account). |

### Specs

- `PrincipalSpec`
- `RoleSpec`
- `BasicAccountSpec`

### Marker values

- `CredentialList`

## Credential types

- `password`
- `activation-key`
- `password-reset-key`

## Basic account

`lib-auth` contains a basic account implementation which is useful for development, local use 
and in limited environments. From security point of view this implementation should be fine,
all security related code works with principals instead of accounts.

This implementation consists of:

- `AuthBasicApi`
- `AuthBasicService`
- `BasicAccountSpec`
- DTO classes:
  - `BasisAccountSummary`
  - `BasicSignIn`
  - `BasicSignUp`

## Creating roles during application bootstrap

[AutoCreateRoleExample](example://)

## Authorization checks

[ServiceEnsureExample](example://)

## Checking for roles in the UI

These are available only if you use the `lib-app` module.

The roles known to the application are available in the [knownRoles](property://ClientApplication) property. 
This is loaded with the `AuthRoleService` during client application bootstrap.

> [!IMPORTANT]
> 
> [knownRoles](property://ClientApplication) contains **ALL ROLES, NOT JUST THE USER'S**
> 

Once the user is logged in, there is an actual session in [genericSessionOrNull;](property://AbstractClientApplication)
the functions below use this to check for roles.

From fragments:

[roleBasedFragmentExample](example://)

From non-fragment code:

[nonAdaptiveClientRoleCheck](example://)

## Internals

As authentication and authorization data is very sensitive, the `lib-auth` uses its own value store,
separated from other value stores in the application. This ensures that no query can return with
auth information accidentally as the value API for the auth store is restricted for security officers.

- Passwords are encrypted with `BCrypt`.
- The module ensures that there are no two principals with the same `name`.
