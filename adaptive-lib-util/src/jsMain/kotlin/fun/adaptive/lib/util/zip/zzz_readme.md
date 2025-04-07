# ZIP

Use the `ZipBuilder` class to build ZIP files on-demand in browser clients:

```kotlin
suspend fun zip() {

    val zipBuilder = ZipBuilder()
    zipBuilder.add("hello.txt", "Hello World!".encodeToByteArray())
    zipBuilder.add("hello-2.txt", "Hello World 2!".encodeToByteArray() )

    downloadFile(
        zipBuilder.finalize(),
        "hello.zip",
        "application/zip"
    )
    
}
```

> [!NOTE]
> 
> `ZipBuilder` uses the browsers built-in [CompressionStream](https://developer.mozilla.org/en-US/docs/Web/API/CompressionStream)
> to perform the actual compression, hence it should be performant enough.
> 
> That said, it is intended for smaller files such as table exports to XLSX and such.
> 