# User interface

[user interface](def://?inline)

## Platform support

Adaptive user interfaces can run on a [platform](def:://) when there is an [adapter](def://)
for the given platform and the basic [platform-dependent](def://) [ui fragments](def://) are
implemented.

As of now:

- browser is the leading platform, where all functions are supported
- Android has some basic support, hasn't been tested for a while, lacks Canvas and input for sure
- iOS has some basic support, hasn't been tested for a while, lacks Canvas and input for sure
- Desktop has no support

Adding support for a given platform is quite easy, I just haven't had the time and/or need to
progress with non-browser platforms.

Layout calculations are 100% platform-independent, they should work on any platform.
