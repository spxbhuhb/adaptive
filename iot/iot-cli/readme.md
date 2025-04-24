# IoT CLI utilities

```shell
alias aio-cli="java -jar iot-cli-jvm-0.25.424-all.jar"
```

## CurVal upload

Start the curVal upload background service:

```text
Usage: aio-cli curval-upload [<options>] <queue-path> <url> <username> <password>

Options:
  -h, --help  Show this message and exit

Arguments:
  <queue-path>  Path to the queue directory
  <url>         WebSocket URL for the point service
  <username>    Username for the point service
  <password>    Password for the point service
```

```shell
aio-cli curval-upload ./var http://localhost:3000 so so
```

## Textual history upload

- Upload all driver-side textual histories.
- Needs the CurVal upload background service.

```text
Usage: aio-cli upload-text-history [<options>] <history-path> <queue-path>

Options:
  -h, --help  Show this message and exit

Arguments:
  <history-path>  Path to the textual history
  <queue-path>    Path to the send queue

```

```shell
aio-cli upload-text-history /Users/tiz/src/adaptive-iot/adaptive-iot-zigbee/var/history ./var/upload-text-history-queue
```
