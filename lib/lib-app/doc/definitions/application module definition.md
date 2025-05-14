# Application module definition

Application module definitions describe the features offered by an [application module](def://).

They are typically placed in the `app` directory of the module.

The typical application module setup is a set of classes placed in the `app` directory
of the module source code.

For example, the setup of an application module named `Sample` would be:

- `SampleModule` - base module setup, extends [AppModule](class://)
- `SampleClientModule` - basic client setup, extends `SampleModule`
- `SampleWsModule` - workspace-based client
