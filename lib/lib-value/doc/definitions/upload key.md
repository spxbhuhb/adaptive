# Upload key

An upload key is a unique key associated with a [value blob](def://) upload process. The key
is created when a client starts the upload process and lives until the upload process finishes
(either successfully or by being aborted).

The upload key belongs to a [session](def://), only the [session](def://) that created the 
upload key can use it.