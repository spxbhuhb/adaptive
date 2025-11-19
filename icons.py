import os
import shutil

def collect_svgs(source_dir, target_dir):
    """
    Recursively collect all .svg files from source_dir,
    copy them to target_dir, and rename them by adding '.1'
    before the .svg extension.
    """
    if not os.path.isdir(source_dir):
        raise ValueError(f"Source directory does not exist: {source_dir}")

    os.makedirs(target_dir, exist_ok=True)

    for root, _, files in os.walk(source_dir):
        for filename in files:
            if not filename.lower().endswith(".1.svg") and filename.lower().endswith(".svg"):
                source_path = os.path.join(root, filename)

                name_without_ext = filename[:-4]   # remove ".svg"
                new_name = f"{name_without_ext}.1.svg"
                target_path = os.path.join(target_dir, new_name)

                shutil.copy2(source_path, target_path)
                print(f"Copied: {source_path} -> {target_path}")


if __name__ == "__main__":
    src = "."
    dst = "./build/adaptive/icons"
    collect_svgs(src, dst)
