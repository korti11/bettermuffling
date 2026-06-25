# Contributing to Better Muffling

Thanks for your interest in contributing. This covers bug reports, feature requests, and pull requests.

## Reporting bugs

Use the [Bug Report](https://github.com/korti11/bettermuffling/issues/new?template=bug_report.yml) issue template. Before filing, search existing issues to avoid duplicates. Include your mod version, Minecraft version, NeoForge version, steps to reproduce, and the relevant section of `logs/latest.log`.

## Suggesting features

Use the [Feature Request](https://github.com/korti11/bettermuffling/issues/new?template=feature_request.yml) issue template. Describe the problem you're trying to solve and the proposed solution.

## Pull requests

### Prerequisites

- Java 21
- An IDE with Gradle support (IntelliJ IDEA recommended)

### Setup

1. [Fork the repository](https://github.com/korti11/bettermuffling/fork) on GitHub.
2. Clone your fork locally:
   ```bash
   git clone https://github.com/<your-username>/bettermuffling.git
   cd bettermuffling
   ```
3. Add the upstream remote so you can pull in future changes:
   ```bash
   git remote add upstream https://github.com/korti11/bettermuffling.git
   ```
4. Build to verify everything is working:
   ```bash
   ./gradlew build
   ```

For IDE support, import as a Gradle project. IntelliJ will pick up run configurations for `client`, `server`, and `data` automatically.

### Branch naming

Branch off from the relevant version branch (e.g. `1.21.x`). Use a short, descriptive name: `fix/cloning-recipe` or `feat/range-indicator`.

### Making changes

- Keep changes focused — one fix or feature per PR.
- If you add a new block or item, add the corresponding lang entry in `src/main/resources/assets/bettermuffling/lang/en_us.json`.
- If you change recipes, run `./gradlew data` to regenerate data-driven resources and commit the output.
- Client-only code (rendering, GUI, sound handling) belongs under a `client/` subpackage and must not be referenced from common/server paths.

### Submitting

Push your branch to your fork and open a pull request against the appropriate version branch (`1.21.x`, etc.). PRs must pass `./gradlew build` cleanly — CI runs on every PR automatically.

### Commit style

Use a short imperative subject line, optionally followed by a blank line and more detail:

```
fix: prevent cloning recipe from producing two basic blocks

The advanced recipe now requires exactly one basic block and
rejects combinations that would otherwise output the wrong result.
```
