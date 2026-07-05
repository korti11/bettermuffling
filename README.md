# Better Muffling

A Minecraft mod that adds configurable sound-muffling blocks. Place one down, open its GUI, and dial per-category volume multipliers for everything within range.

**Minecraft:** 26.1.x | **Loader:** NeoForge 26.1.2.x | **License:** MIT

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/better-muffling) · [Issues](https://github.com/korti11/bettermuffling/issues)

---

## Blocks

### Muffling Block
Crafted from wool, leather, and a note block:

```
W L W
L N L
W L W
```
`W` = any wool, `L` = leather, `N` = note block

Opens a GUI with per-category volume sliders (Master, Music, Records, Weather, Blocks, Hostile Mobs, Friendly Mobs, Players, Ambient, Voice). Optionally enable **Private Mode** so only the placer can adjust settings or break the block.

### Advanced Muffling Block
Surround a Muffling Block with gold ingots:

```
G G G
G M G
G G G
```
`G` = gold ingot, `M` = Muffling Block

Adds include/exclude filtering by sound name and a **Listening Mode** that passively captures the names of sounds playing nearby — useful for building precise filters.

### Advanced Upgrade (item)
Surround a wool block with gold ingots:

```
G G G
G W G
G G G
```
`G` = gold ingot, `W` = any wool

Shift + right-click on a placed Muffling Block to upgrade it to an Advanced Muffling Block in-place, preserving all existing settings.

---

## Configuration

Edit `config/bettermuffling-common.toml` (server-side) and `config/bettermuffling-client.toml` (client-side).

| Option | Scope | Default | Range | Description |
|---|---|---|---|---|
| `maxRange` | Common | 16 | 2–64 | Maximum radius (blocks) that the GUI range slider can reach |
| `minVolume` | Common | 0.0 | 0.0–0.99 | Minimum volume multiplier the sliders can be set to |
| `maxVolume` | Common | 1.0 | 0.01–1.0 | Maximum volume multiplier the sliders can be set to |
| `tooltipEnable` | Client | true | — | Show block stats in the item tooltip |

---

## Building from Source

Requires Java 21.

```bash
# Build the mod JAR (output: build/libs/)
./gradlew build

# Full clean build
./gradlew clean build

# Launch Minecraft client for testing
./gradlew client

# Launch Minecraft server for testing
./gradlew server

# Regenerate data-driven resources (recipes, blockstates, etc.)
./gradlew data
```
