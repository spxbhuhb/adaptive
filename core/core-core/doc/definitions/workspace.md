# Workspace

Workspaces are part of [applications](def://). Each [application](def://) has two workspaces:
a frontend workspace, which handles UI rendering, and a backend workspace which handles background
tasks.

- Clients with [user interfaces](def://) typically utilize both frontend and backend workspaces.
- Clients without a [user interface](def://) may use an empty frontend workspace.
- Servers typically use an empty frontend workspace.