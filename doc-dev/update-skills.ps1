# Refresh the vendored JetBrains Kotlin agent skills in .opencode/skills.
#
# Usage: .\doc-dev\update-skills.ps1
$ErrorActionPreference = 'Stop'

$repoUrl = 'https://github.com/Kotlin/kotlin-agent-skills.git'
$skills = @(
    'kotlin-tooling-kotlin-toolchain',
    'kotlin-tooling-gradle-to-kotlin-toolchain-project',
    'kotlin-tooling-gradle-to-kotlin-toolchain-plugin'
)

$repoRoot = Split-Path -Parent $PSScriptRoot
$dest = Join-Path $repoRoot '.opencode\skills'
$tmpDir = Join-Path ([IO.Path]::GetTempPath()) ("kotlin-agent-skills-" + [guid]::NewGuid().ToString('N'))

try {
    Write-Host "Cloning $repoUrl (sparse)..."
    git clone --depth 1 --filter=blob:none --sparse $repoUrl $tmpDir | Out-Null
    $sparsePaths = $skills | ForEach-Object { "skills/$_" }
    git -C $tmpDir sparse-checkout set @sparsePaths | Out-Null

    New-Item -ItemType Directory -Force -Path $dest | Out-Null
    foreach ($skill in $skills) {
        $src = Join-Path $tmpDir "skills\$skill"
        if (-not (Test-Path $src)) {
            Write-Warning "upstream skill '$skill' not found, skipping"
            continue
        }
        $target = Join-Path $dest $skill
        if (Test-Path $target) { Remove-Item -Recurse -Force $target }
        Copy-Item -Recurse $src $target
        Write-Host "updated .opencode/skills/$skill"
    }
} finally {
    if (Test-Path $tmpDir) { Remove-Item -Recurse -Force $tmpDir }
}

Write-Host "Done. Review with: git status --short .opencode/skills"
