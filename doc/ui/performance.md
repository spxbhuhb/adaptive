# Performance

For fragment related measurements you can use [measureFragmentTime](../foundation/foundation-fragments.md#measurefragmenttime).

## A quick and dirty check

Adaptive comes with an unavoidable overhead because it builds a tree of fragments and then turns that tree
into actual UI.

I made a very quick and very dirty performance test to see if I can expect performance problems, especially
in web browsers where the number of components can be very high.

I used the chessboard example from the sandbox to generate a simple chessboard.

**Notes**

- without optimizations (`unmount` is somewhat optimized to remove the DOM at once and avoid continuous re-layouts)
- also includes the actual building of the DOM
- uses inline styles for HTML elements which is quite expensive
- during the test I've seen an extremely slow `unmount` (1.5 seconds), this has been solved by adding `actualBatch` to `AdaptiveUIAdapter`.

Chessboard size: 4096 (64x64)

| Hardware                     | Hardware Age | OS          | Browser | `create` | `mount` | `unmount` | `dispose` |
|------------------------------|--------------|-------------|---------|----------|---------|-----------|-----------|
| 3,2 GHz Intel Core i3        | 14 years     | macOS 10.13 | Firefox | 448 ms   | 37 ms   | 31 ms     | 6 ms      |
| 3,6 GHz 8-Core Intel Core i9 | 4 years      | macOS 14.5  | Safari  | 168 ms   | 17 ms   | 25 ms     | 8 ms      |     
|                              | 4 years      |             | Firefox | 98 ms    | 9 ms    | 12 ms     | 2 ms      |
|                              | 4 years      |             | Chrome  | 83.6 ms  | 10.4 ms | 19.5 ms   | 2.1 ms    |           

Chessboard size: 10.000 (100x100)

| Hardware                     | Hardware Age | OS          | Browser | `create` | `mount` | `unmount` | `dispose` |
|------------------------------|--------------|-------------|---------|----------|---------|-----------|-----------|
| 3,2 GHz Intel Core i3        | 14 years     | macOS 10.13 | Firefox | 1.029 ms | 57 ms   | 66 ms     | 13 ms     |
| 3,6 GHz 8-Core Intel Core i9 | 4 years      | macOS 14.5  | Safari  | 183 ms   | 18 ms   | 63 ms     | 15 ms     |     

Chessboard size: 65536 (256x256)

| Hardware                     | Hardware Age | OS          | Browser | `create` | `mount` | `unmount` | `dispose` |
|------------------------------|--------------|-------------|---------|----------|---------|-----------|-----------|
| 3,2 GHz Intel Core i3        | 14 years     | macOS 10.13 | Firefox | 6.154 ms | 385 ms  | 439 ms    | 78 ms     |
| 3,6 GHz 8-Core Intel Core i9 | 4 years      | macOS 14.5  | Safari  | 2.292 ms | 255 ms  | 358 ms    | 74 ms     |     


**Conclusion**

I won't worry about performance for now. Normal applications probably won't exceed the 4k or the 10k case. Those have
acceptable performance on a 14-year-old machine even without optimizations.

Interesting blog about DOM size: [Thoughts on Avoiding an Excessive DOM Size](https://blog.jim-nielsen.com/2021/thoughts-on-avoiding-an-excessive-dom-size/)