# EntityCore

EntityCore is a Java 17+ Bukkit-family NPC plugin scaffold using the requested package `firepdx.pgc.net` and main class `firepdx.pgc.net.EntityCore`.

This initial implementation provides:

- `/npc` command surface for create, delete, rename, list, move, tp, skin, hologram, save, reload, debug, export, import, and help.
- Durable YAML-backed NPC definitions.
- Public developer API entry points.
- Modular package layout for skins, holograms, AI, packets, storage, scheduler, integrations, compatibility, events, and extensions.
- Platform isolation points for future NMS/packet renderers.

The enormous cross-version packet/NMS renderer, proxy plugins, external database drivers, and third-party integrations should be added as separate modules behind the interfaces already present here. Keeping those boundaries explicit is what lets the public API survive Minecraft version churn.

## Build

Install Maven, then run:

```bash
mvn package
```

The plugin jar will be written to `target/entitycore-1.0.0-SNAPSHOT.jar`.

## Package

```java
package firepdx.pgc.net;
```

## Main Class

```java
firepdx.pgc.net.EntityCore
```
