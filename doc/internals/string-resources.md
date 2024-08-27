# String Resources

String resources are compiled into `.arv` (Adaptive Resource Values) files.

## Data size

These are from ChatGPT, so take them with a grain of salt.

The number of strings present in a mobile application can vary widely depending on the complexity,
purpose, and functionality of the app. However, here are some general estimates for different types
of mobile applications:

* Simple Apps (e.g., calculator, flashlight, basic utility apps):
    * Number of Strings: 50-200
* Moderate Complexity Apps (e.g., social media, news, fitness tracking):
    * Number of Strings: 200-1000
* Complex Apps (e.g., e-commerce, banking, extensive productivity tools):
    * Number of Strings: 1000-5000
* Highly Complex Apps (e.g., large-scale enterprise apps, comprehensive games, multi-functional platforms):
    * Number of Strings: 5000+

With 5000 strings and average size of 50 characters/string and 25 characters of metadata, the total size is 375 KB.
If we double the bytes needed per character (Unicode), that is 625 KB.

### Generated code size

**These calculations are based on the code I copied from Compose in May 2024.** Most probably it will get optimized,
but it is good to have some pointers to base our future development on.

Using the following string resource:

```xml
<resources>
    <string name="app_name">"Good Morning"</string>
    <string name="snooze">Snooze</string>
    <string name="sleepiness">Sleepiness:</string>
    <string name="by_joining">By joining you agree to our</string>
    <string name="terms_of_service">Terms of Service</string>
    <string name="and">and</string>
    <string name="privacy_policy">Privacy Policy</string>
</resources>
```

I get these file sizes (~11 KB):

```text
-rw-r--r--  1 tiz  staff  3935 Jul 10 03:51 CommonMainString0.class
-rw-r--r--  1 tiz  staff   721 Jul 10 03:51 Res$string.class
-rw-r--r--  1 tiz  staff  2192 Jul 10 03:51 Res.class
-rw-r--r--  1 tiz  staff  4425 Jul 10 03:51 String0_commonMainKt.class

```

Most of it is probably just overhead. Duplicating the strings:

```xml
<resources>
    <string name="app_name">"Good Morning"</string>
    <string name="snooze">Snooze</string>
    <string name="sleepiness">Sleepiness:</string>
    <string name="by_joining">By joining you agree to our</string>
    <string name="terms_of_service">Terms of Service</string>
    <string name="and">and</string>
    <string name="privacy_policy">Privacy Policy</string>

    <string name="app_name2">"Good Morning"</string>
    <string name="snooze2">Snooze</string>
    <string name="sleepiness2">Sleepiness:</string>
    <string name="by_joining2">By joining you agree to our</string>
    <string name="terms_of_service2">Terms of Service</string>
    <string name="and2">and</string>
    <string name="privacy_policy2">Privacy Policy</string>
</resources>
```

Results in (~17 KB):

```text
-rw-r--r--  1 tiz  staff  6479 Jul 10 03:49 CommonMainString0.class
-rw-r--r--  1 tiz  staff   721 Jul 10 03:49 Res$string.class
-rw-r--r--  1 tiz  staff  2192 Jul 10 03:49 Res.class
-rw-r--r--  1 tiz  staff  7471 Jul 10 03:49 String0_commonMainKt.class
```

So, by adding 81 chars of keys and 89 chars of data **~ 170 bytes**, the size of the generated code
grown by **~ 5.6 KB**, that is **times 32**.

Projecting this increment to size calculations for 5000 strings, the generated code would be **375 KB * 32 = 12 MB**.

I really think that in case of built-in application strings, the size of the actual data is absolutely negligible, the
size of the generated code is a real problem on the other hand. Especially that this is for one language, no qualifiers.