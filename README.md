# Gravity Wand Mod (Minecraft Fabric 26.1.2)

A fun Minecraft Fabric mod targeting Minecraft version **26.1.2** (the de-obfuscated "Tiny Takeover" series) that introduces a magical **Gravity Wand**!

---

## Features

*   **Launch Entities:** Right-clicking on any mob (passive or hostile) will fling them high into the sky, accompanied by an Enderman teleportation sound effect.
*   **Physicalize & Throw Blocks:** Right-clicking on any solid block (except unbreakable ones like Bedrock) will remove the block from the world, convert it into a physics-based `FallingBlockEntity` with forward and upward momentum, and launch it. The flying block obeys gravity and solidifies back into a block where it lands!
*   **Item Cooldown:** Has a 1-second cooldown (20 game ticks) between uses to prevent excessive spam.

---

## Crafting Recipe

The Gravity Wand can be crafted in Survival Mode using a crafting table with a diagonal recipe:

*   **Top-Right:** 1x `minecraft:ender_eye` (Eye of Ender)
*   **Center:** 1x `minecraft:blaze_rod` (Blaze Rod)
*   **Bottom-Left:** 1x `minecraft:diamond` (Diamond)

```text
[   ] [   ] [ Eye of Ender ]
[   ] [ Blaze Rod ] [   ]
[ Diamond ] [   ] [   ]
```

---

## Development Setup

### Requirements
*   **JDK 25** (Mandatory for Minecraft 26.x)
*   **Fabric Loader** 0.19.2 or later

### Compiling and Building the Mod
To compile the mod and package it into a `.jar` file:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; .\gradlew build
```
The compiled mod will be located in `build/libs/gravitywand-1.0.0.jar`.

### Running in Development
To boot up a client environment and test the mod in-game:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; .\gradlew runClient
```

---

## License

This mod is available under the CC0-1.0 license. Feel free to use, modify, and learn from it.
