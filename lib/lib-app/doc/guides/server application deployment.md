# Server application deployment

Server applications deployment typically uses this directory structure:

```text
app
├── bin
├── etc
│   └── <app-name>.properites
├── lib
│   └── <app-name>-<version>-all.jar
├── usr
└── var
    ├── log
    │   └── archive
    ├── static
    │   ├── <app-name>.js
    │   ├── index.html
    │   ├── release.json
    │   └── resources
    └── values
```