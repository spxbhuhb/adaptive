# Writing an application module definition

[Application module definitions](def://) define and register features offered by [application modules](def://).

In this guide we use `Example` as module name.

They are typically put into the `app` directory of a module and consists of the files shown below.
This is a convention, you may tailor it as needed.

```text
<module-package>
└── app
    ├── ExampleModule        - registers data models and resources
    ├── ExampleClientModule  - extends ExampleModule, registers frontent fragments
    ├── ExampleServerModule  - extends ExampleModule, configures server
    └── ExampleWsModule      - extends ExampleClientModule, registers multi-pane workspace components
```

## Data model and resource registration

The data model of a module typically consists of three parts:

- [adat classes](def://)
- enum classes
- value marker and reference label definitions

The first two have to be added to the [wireformat registry](def://) to support deserialization.
Add them in the [wireFormatInit](function://AppModule) as the example shows.

Some [resources](def://), such as strings, should be preloaded. Loading these resources during
application startup is asynchronous, add them to the appropriate resource store list as the
example shows to start the loading.

[ExampleModule](example://)

`ExampleModule` sets up wire formats for Adat-based serialization and application resources.

Both `ExampleClientModule` and `ExampleServerModule` modules extend `ExampleModule` to ensure
consistent behavior across environments.

`commonMainStringsStringStore0` is imported from the package of generated [resources](def://). See
[What is the resource subsystem](guide://) for more information.

## Frontend fragment registration

## Server configuration

This class handles back-end-specific registration. It ensures that services (like REST endpoints) and workers (e.g., cron jobs) are initialized when the server starts.
