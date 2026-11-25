# Agent skills

OpenCode (and compatible agents) discover project skills in
[`.opencode/skills/<skill-id>/SKILL.md`](../.opencode/skills). Each skill has a short description
that the agent sees; the full instructions are loaded on demand.

## Skills in this repository

| Skill ID | What it is for |
|---|---|
| `i18n4k-build` | **Own skill.** Building, testing and troubleshooting this repository. |
| `i18n4k-release` | **Own skill.** Releasing to Maven Central and the Gradle Plugin Portal. |
| `kotlin-tooling-kotlin-toolchain` | Upstream JetBrains skill: Kotlin Toolchain syntax, products, layouts, pitfalls. |
| `kotlin-tooling-gradle-to-kotlin-toolchain-project` | Upstream JetBrains skill: whole-project Gradle → Toolchain migration. |
| `kotlin-tooling-gradle-to-kotlin-toolchain-plugin` | Upstream JetBrains skill: Gradle plugin → Toolchain plugin porting (reference for the Gradle plugin exception). |

The upstream skills come from <https://github.com/Kotlin/kotlin-agent-skills> and are vendored
verbatim so that later agents do not need network access.

## Refreshing the upstream skills

The GitHub workflow
[`update_agent_skills.yml`](../.github/workflows/update_agent_skills.yml) runs on the first day of
every month (and via *Actions → Update agent skills → Run workflow*). It refreshes the vendored
skills and opens a pull request when anything changed. Review the diff before merging.

To do it locally, run one of the scripts (they clone the upstream repository sparsely and replace
the vendored skill folders):

```sh
# Linux / macOS / Git Bash
./doc-dev/update-skills.sh

# Windows PowerShell
./doc-dev/update-skills.ps1
```

If the shell script lost its executable bit (common on Windows checkouts), run it as
`bash doc-dev/update-skills.sh`.

The scripts are safe to re-run; they overwrite only the upstream skill directories listed in
their `SKILLS` array. Review the diff before committing an update.

Alternatively, install the skills globally with the official CLI:

```sh
npx skills add Kotlin/kotlin-agent-skills
```

## Adding a project skill

1. Create `.opencode/skills/<id>/SKILL.md` with YAML frontmatter:
   ```markdown
   ---
   name: Short display name
   description: When should an agent load this?
   ---
   ```
2. Keep the body short and link to `doc-dev/*.md` for details instead of copying large texts.
3. Supporting files (scripts, references) may live next to `SKILL.md`; they are not loaded
   automatically, so mention them explicitly.
4. Use lowercase-kebab-case IDs (e.g. `i18n4k-build`).

## Why skills instead of one huge `agent.md`?

Skills are loaded only when relevant, so the agent's context stays small, while `agent.md`
remains the always-visible entry point with the non-negotiable rules. Detailed docs live in
`doc-dev/` and are linked from both.
