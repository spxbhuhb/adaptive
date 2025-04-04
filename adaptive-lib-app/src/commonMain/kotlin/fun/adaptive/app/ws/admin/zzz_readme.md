# WS Module: Admin

Maintains a list of administration workspace items and provides 
an admin tool which lets the user open the content of those items.

The tool shows a list of the admin items, built automatically from the 
content of `AppAdminWsModule.adminItems`.

To add an admin item use the `Workspace.addAdminItem` function:

```kotlin
addAdminItem(module.ACCOUNT_MANAGER_ITEM)
```