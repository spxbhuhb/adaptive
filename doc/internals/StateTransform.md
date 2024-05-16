# State Access Transform

`StateAccessTransform` transforms state variable accesses from the original code into 
calls to `AdaptiveFragment.getClosureVariable()` and `AdaptiveFragment.setStateVariable()`.

The transformed expressions:

* original function parameter reads (IrGetValue)
* top level local variable reads (IrCall to the getter)
* top level local variable writes (IrCall to the setter)
* anonymous function parameter reads (IrGetValue)

`AdaptiveFragment.setStateVariable()`:

* sets the dirty mask bit that belongs to the state variable
* it **DOES NOT** call patch, that is the responsibility of `AdaptiveFragment.invoke`

The transform is called when building functions of the generated class:

* initializer
* `patch()` - to build value of state variables (parameters of the called original functions)
* `invoke()` - to transform the body of the support functions