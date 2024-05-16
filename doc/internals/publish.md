# Publishing

To publish into a Maven repository you have to set up the signing as documented in [The Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html).

Long story short, put the following info into your `gradle.properties`:

```properties
signing.gnupg.executable=/usr/local/bin/gpg
signing.gnupg.keyName=<your-key-short-name>
```

Theoretically you don't need the executable, but for me, it was necessary, I have no idea why.