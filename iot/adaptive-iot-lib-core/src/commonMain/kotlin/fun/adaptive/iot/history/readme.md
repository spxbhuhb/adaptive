# History

* A **history id** is a UUID that is used to group measured or calculated values together.
* Any one given **history id** may have only one data type during its lifetime.
* Different **history ids** may have different data types.

Histories are stored in a three level directory structure:

```text
00
  00
    0000cdef-1edf-4e48-aa8b-98184f632b16
  01
    0001cdef-1edf-4e48-aa8b-98184f632b16  
01
  00
    0100cdef-1edf-4e48-aa8b-98184f632b16
```

* The outermost directory name is the first two digits (hexadecimal) of the history id.
* The middle directory name is the third and fourth digits (hexadecimal) of the history id.
* The innermost directory name is the full history id.
* Inside the innermost directory each history may have multiple files.
* All files have an Adat class which can be used to load/save the file entirely.
* Add history Adat classes have fix sized protobuf record and optimized serializers.

| File name                    | Content class            | Data format |
|------------------------------|--------------------------|-------------| 
| `index.pb`                   | `HistoryIndex`           | protobuf    |
| `meta.json`                  | `HistoryMetaData`        | json        |
| `oob.<type-signature>hrs`    | `<type>HistoryRecordSet` | protobuf    |
| `merge.<type-signature>hrs`  | `<type>HistoryRecordSet` | protobuf    |
| `<uuid>.<type-signature>hrs` | `<type>HistoryRecordSet` | protobuf    |

* `<type>` is the data type stored in the history
* `<type-signature>` is the lowercase signature of `<type>`

Supported types:

| Type   | Signature | Content class            |
|--------|-----------|--------------------------|
| Double | `d`       | `DoubleHistoryRecordSet` |
| Int    | `i`       | `IntHistoryRecordSet`    |

# Disk and memory requirements

* there are 525600 minutes in one year (60*24*365)
* rpm = record per minute

Disk requirements

| Data                        | Calculation                   | Estimated size |
|-----------------------------|-------------------------------|----------------|
| double record               | instant + double + int + tags | 32 B           |
| daily - 1 rpm, double       | double record * 60 * 24       | 46 KB          |
| yearly - 1 rpm, double      | daily * 365                   | 17 MB          |
| 5 years - 1 rpm, double     | yearly * 5                    | 84 MB          | 
| data: 5 years, 5000 doubles | 5 years * 5000                | 401 GB         |
| index entry                 | uuid +long + instant + tags   | 36 B           |
| index yearly, 1 entry/day   | index entry * 365             | 13 KB          |
| index 5 years, 1 entry/day  | index yearly * 5              | 66 KB          |
| index: 5 years, 5000 points | yearly index * 5 * 5000       | 328 MB         |

# Mechanisms

## Add

When adding records through the history API

* `add` returns successfully only after the records are safely stored
* `add` puts the records into the add queue, this queue is then processed asynchronously

For each record the asynchronous processing:

* checks if the record is out-of-bound (OOB, the timestamp is before the last timestamp in the history)
* if not OOB, appends the record to the latest history chunk file
* if OOB, appends the record to the out-of-bounds file and schedules a merge

## Merge

Merge:

* is a process when out-of-bounds history records are merged with the existing history files
* is delayed to allow arrival of more OOB records and prevent continuous merging
* modifies the chunks and the index, but never creates a new chunk
* merge builds a new chunk in a separate `merge.<type-signature>hrs` file
* when the new chunk is ready, merge replaces the original chunk:
  * deletes the original chunk
  * renames the merge file to the name of the original chunk
  * updates the metadata
  * updates the index

## Concurrent access

Add does not block queries as:

- for non-OOB records, metadata and the index before and after the add are compatible
- for OOB records no metadata and index modifications happen

Merge blocks queries as the metadata and the index changes in a non-compatible way.

As merge shall be a very rare operation, hard-locking the metadata and the index
by queries and merging is acceptable.

For each history id we create a `HistoryAccessContext`. This stores:

* the number of running query and add operations
* merge semaphore

We use a global lock `historyAccessLock` that is used to synchronize access
to the access contexts.

Query and add:

* acquires `historyAccessLock`
* checks is the merge semaphore is on
* if so, releases `historyAccessLock`, waits a bit and tries again
* if not, increments running operations counter
* releases `historyAccessLock`
* performs the add/query
* acquires `historyAccessLock`
* decreases the operation counter
* releases `historyAccessLock`

Merge:

* acquires `historyAccessLock`
* sets the merge semaphore on
* checks if the operation counter is zero
* if not, releases `histroyAccessLock`, waits a bit and tries again
* if so, releases `histroryAccessLock`
* performs the merge
* acquires `historyAccessLock`
* sets the merge semaphore off
* releases `historyAccessLock`
