# Notes

`AdaptiveFragment.setStateVariable`

- Ignores values that equals (`==`) with the current one in the state (returns immediately, nothing happens).
- Calls `ValueBinding.callback` for each binding that is different from the one called `setStateVariable`.
- Calls `patchInternal` when the value is changed by a binding.