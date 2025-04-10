# Access control

- Each value in a value store has an access control provider (ACP).
- The uuid of the ACP is stored in the `acp` field of the value.
- When `acp` is `null`, the ACP in effect will be (first existing):
  - ACP of the first ancestor with a non-null `acp`
  - ACP registered for the given item type
  - the default ACP of the `AvValueStore`

Access types:

- add
- read
- write
- delete
- control (modify access se)

ACPs **are values in the store**, extending the `AvAccessControlProvider` abstract class.

`AvValueWorker` calls the appropriate method of the ACP to check if the executor of
the operation has the given type of access to the value.


