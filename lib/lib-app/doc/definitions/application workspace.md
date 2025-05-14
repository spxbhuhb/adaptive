# Application workspace

An [application workspace](def://) is the runtime context or state of an [application](def://). It is
typically an instance of a class that is descendant of [AbstractWorkspace](class://).

Each [application](def://) (server and client) has an [application workspace](def://).

In basic clients [applications](def://), it may be a simple placeholder (an instance of [ClientWorkspace](class://),
for example), while in other cases it may be a sophisticated implementation with full state and UI context management
(an instance of [MultiPaneWorkspace](class://), for example).

## See also

- [What is an application](guide://)
- [AbstractApplication](class://)
- [AbstractWorkspace](class://)
- [ServerWorkspace](class://)
- [ClientWorkspace](class://)
- [MultiPaneWorkspace](class://)