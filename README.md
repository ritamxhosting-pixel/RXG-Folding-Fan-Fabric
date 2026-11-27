# RXG Folding Fan Cosmetic — Fabric 26.2

A client-only decorative folding-fan HUD cosmetic prototype for Minecraft Java 26.2.

## Features in this build
- `B`: show/hide the folding-fan HUD cosmetic.
- `V`: play a short inspect flourish while the cosmetic is enabled.
- Animated idle sway and coloured fan ribs.
- Client-only: no Paper plugin or server installation is required.

## Important scope note
This first implementation is a 2D HUD-rendered cosmetic prototype. It is not yet a true 3D held model and does not render on the player body in third person. The keybinds and HUD use the Fabric 26.2 APIs; compile and runtime verification should be done with the repository's GitHub Actions workflow.

## Toolchain
- Minecraft 26.2
- Fabric Loader 0.19.3
- Fabric API 0.161.0+26.2
- Java 25
- Fabric Loom 1.17-SNAPSHOT

## Build
Run `gradle build` with Java 25. Output JARs are in `build/libs/`.
