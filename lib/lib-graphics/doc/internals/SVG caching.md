# SVG caching

Caching SVG drawing results can improve application performance immensely
in cases when the same icon is drawn many times, such as in lists or in trees.

The entry point of SVG drawing is the `svg` fragment in `fun.adaptive.graphics.svg.api`.
`svg` is an expect fragment, implemented by `SvgSvg`.

Code typically uses the `icon` fragment from `lib-ui` instead of calling `svg` directly.
`icon` simply calls `svg`, it provides a good separation between general use SVGs icon ones.

`SvgSvg` calls `CanvasSvg` for the actual drawing. `CanvasSvg` uses the
resource subsystem to load the SVG source XML, parse it and draw on the canvas.

To cache drawing results properly, we have to:

1. be able to save and load the result of drawing
2. recognize if a cached value can be re-used
3. synchronize cache access
4. prevent infinite growth of the cache

## Saving and loading

This is platform-dependent. On browser, `ActualBrowserCanvas` already use double-buffering 
to prevent flicker. If we save the buffer, we can use it again without redraw.

## Recognizing the possibility of re-use

Conditions:

1. the resource must be the same,
2. the instructions must be the same.

The resource subsystem caches resource data and returns with the same byte array whenever
we read the given resource. We can use referential equality (`===`) to check if the data
is the very same.

Checking the equality of the `GraphicsResourceSet` is not good enough as different resource
environments may return with different data for the same resource.

Instructions can be checked with structural equality (`==`). This could result in some
duplicated cache entries, but the goal would be achieved as the main beneficiaries
of the caching would typically use the same instruction sets.

## Synchronizing cache access

Fragments initiate SVG rendering en-mass for all displayed items at once.

Without synchronization, all the rendering processes would think that there is no
cached entry, draw the SVG and then put it into the cache. The next use would 
rely on the cache, but the initial render would be slow.

To solve this:

- use a lock to protect cache access
- store the fragments waiting for the drawing result with the entry
- when the drawing finishes, notify all fragments that there is a result to use

## Prevent infinite cache growth

I'm not sure that this is a real problem, but in some edge cases it might become
problematic. For example, imagine a code that changes the color of an icon to a
random color periodically. This would result in a huge cache.

We can simply use reference counting to solve this problem. When an SVG fragment
uses a cache entry, it increments the entry counter. When the fragment mounts or
switches to another entry, it decrements the counter. When the counter reaches zero, 
the cache entry is deleted.

We can combine this with a delay, probably one that depends on the highest concurrent
use count.

Also, we can set a limit below which the cache entries are not dropped; this would
improve general application performance.

If we add some statistics, we could decide which approach is the best.





