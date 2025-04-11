# Manual changes

When a value store uses file persistence, the files are typically simple JSON files.
These can be patched easily with standard tools or small programs, assuming the
resulting files are valid and the system is stopped during patching.

To change a string in each file on U**X:

```shell
find . -type f -name "*.json" -exec sed -i '' 's/AvItemIdList/AvRefList/g' {} +
```