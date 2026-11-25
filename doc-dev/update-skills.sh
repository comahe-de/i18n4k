#!/usr/bin/env bash
# Refresh the vendored JetBrains Kotlin agent skills in .opencode/skills.
#
# Usage: ./doc-dev/update-skills.sh
set -euo pipefail

REPO_URL="https://github.com/Kotlin/kotlin-agent-skills.git"
SKILLS=(
  "kotlin-tooling-kotlin-toolchain"
  "kotlin-tooling-gradle-to-kotlin-toolchain-project"
  "kotlin-tooling-gradle-to-kotlin-toolchain-plugin"
)

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="$REPO_ROOT/.opencode/skills"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

echo "Cloning $REPO_URL (sparse)..."
git clone --depth 1 --filter=blob:none --sparse "$REPO_URL" "$TMP_DIR/repo" >/dev/null
git -C "$TMP_DIR/repo" sparse-checkout set $(printf 'skills/%s ' "${SKILLS[@]}") >/dev/null

mkdir -p "$DEST"
for skill in "${SKILLS[@]}"; do
  src="$TMP_DIR/repo/skills/$skill"
  if [ ! -d "$src" ]; then
    echo "WARNING: upstream skill '$skill' not found, skipping" >&2
    continue
  fi
  rm -rf "$DEST/$skill"
  cp -R "$src" "$DEST/$skill"
  echo "updated .opencode/skills/$skill"
done

echo "Done. Review with: git status --short .opencode/skills"
