# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build the mod JAR (output: build/libs/)
./gradlew build

# Full clean build (used in CI)
./gradlew clean build

# Launch Minecraft client for development testing
./gradlew client

# Launch Minecraft server for development testing
./gradlew server

# Regenerate data-driven resources (recipes, blockstates, etc.)
./gradlew data
```

No automated tests exist in this project.

## Project Overview

**Better Muffling** is a Minecraft Forge mod (currently targeting 1.19.2, Forge 43.2.3, Java 17) that adds configurable sound-muffling blocks. Players place a Muffling Block, open its GUI, and dial per-category volume multipliers for sounds within a configurable radius.

Mod ID: `bettermuffling` | Group: `io.korti.bettermuffling` | Version: `1.1.1`

## Architecture

### Client/Server Split

The mod uses Forge's `DistExecutor` proxy pattern. `BetterMuffling.java` (entry point) initializes either `ClientProxy` or `ServerProxy`. All sound interception and GUI rendering is client-only code under `client/`.

### Data Flow

1. Player right-clicks a Muffling Block → server sends `OpenScreenPacket` → client opens GUI
2. Client GUI sends `RequestMufflingUpdatePacket` → server replies with `MufflingDataPacket` containing full block entity state
3. Player adjusts sliders/toggles in GUI → client sends `MufflingDataPacket` back to server → block entity saved to NBT

### Core Classes

- **`MufflingBlockEntity`** — The central data store. Holds per-`SoundSource` maps for volume levels, sound name include/exclude mode flags, and name sets. All config persists via `writeMufflingData`/`readMufflingData` (NBT).
- **`SoundHandler`** (client-only) — Subscribes to `PlaySoundEvent`. For each sound, checks `MufflingCache` for nearby muffling blocks and applies the appropriate volume multiplier. If `listening` mode is on, records the sound name into the block entity.
- **`MufflingCache`** (client-only) — Local cache of active muffling block positions, used by `SoundHandler` for fast lookup without iterating all loaded chunks.
- **`PacketHandler`** — Registers 3 packet types (IDs 0–2) on a single network channel. Bidirectional for `MufflingDataPacket`, server→client for `OpenScreenPacket`, client→server for `RequestMufflingUpdatePacket`.
- **`EventHandler`** — Prevents unauthorized players from breaking placer-only muffling blocks by intercepting `PlayerEvent.BreakSpeed`.

### Block Variants

- **`MufflingBlock`** — Basic variant. GUI shows per-category sliders only.
- **`AdvancedMufflingBlock`** — Extended variant. Adds include/exclude UI and listening mode to collect live sound names. Both use the same `MufflingBlockEntity` type; `advancedMode` flag on the entity distinguishes behavior.

### Configuration

Forge config (`BetterMufflingConfig`):
- **Client:** `tooltipEnable` — show block stats in item tooltip
- **Common:** `maxRange` (2–64, default 16), `minVolume` (0.0–0.99), `maxVolume` (0.01–1.0)

Block-level settings (per block entity): `range`, `placerOnly`, `advancedMode`, `listening`, and per-`SoundSource` level/include-mode maps.

### Resources

- `src/main/resources/META-INF/mods.toml` — mod metadata and Forge/MC version constraints
- `src/main/resources/assets/bettermuffling/` — blockstates, models, textures, lang (`en_us.json`)
- `src/main/resources/data/bettermuffling/recipes/` — JSON crafting recipes including special cloning and reset recipes handled by custom `RecipeSerializer` classes
