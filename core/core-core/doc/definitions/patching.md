# Patching

Patching is the mechanism to update a [fragment tree](def://) when a [fragment state](def://) changes.

After the state has been changed, the [fragment](def://) checks the [state variables](def://) and
child [fragments](def://) affected by the change and patches them as well. This goes on as long
as there are dependents.

In [user interfaces](def://) patching typically results in re-rendering some part of the UI.

