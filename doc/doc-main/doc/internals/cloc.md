```shell
GROUPS=("core" "lib" "grove" "site")
SOURCESETS=("commonMain" "commonTest" "androidMain" "jsMain" "jvmMain" "jvmTest")

setopt null_glob

for ss in $SOURCESETS; do
  echo "=== cloc for source set: $ss ==="

  PATHS=()
  for group in $GROUPS; do
    PATHS+=($group/*/src/$ss)
  done

  if [[ $#PATHS -gt 0 ]]; then
    cloc $PATHS 2>/dev/null
  else
    echo "No files found for $ss"
  fi

  echo ""
done
```