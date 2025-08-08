---
status: review
---

# Question

How do I upload a file?

# Answer

File content is typically stored in [value blobs](def://). To create one:

1. Get the id of a [value](def://) that will be the owner of the [value blob](def://).
2. Start the upload and get an upload key by calling [startUpload](function://AvBlobApi).
3. Send chunks of data by calling [sendChunk](function://AvBlobApi).
4. Once all chunks are sent, close the upload by calling [finishUpload](function://AvBlobApi).

If you want to abort the upload for some reason, call [abortUpload](function://AvBlobApi).

[valueBlobUploadExample](example://)