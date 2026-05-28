# ⚔️ DemiseMaces

**DemiseMaces** is a modern Minecraft Paper plugin (1.21+) that introduces four powerful, custom-coded Maces into your server. Each mace possesses unique, devastating abilities to dominate the battlefield!

---

## ✨ Features

Unleash the true power of the Mace with four legendary variants:

### 💨 Wind Mace
*A legendary mace forged from the howling winds.*
* **Right Click:** **Dash Forward** - Propel yourself forward at high speed.
* **Press F (Swap Hand):** **Launch Upwards** - Launch yourself vertically into the air.

### 🌌 Void Mace
*A dark relic that manipulates gravity itself.*
* **Right Click:** **Push Entities** - Violently repel all nearby enemies away from you.
* **Press F (Swap Hand):** **Pull Entities** - Drag all nearby enemies towards your location.

### ❄️ Frost Mace
*An ancient weapon that freezes the very air.*
* **Right Click:** **Absolute Freeze** - Instantly freeze all surrounding entities in place for 15 seconds.
* **Press F (Swap Hand):** **Frost Projectile** - Hurl your mace as a deadly projectile that shatters on impact, dealing massive damage.

### 💥 Detonate Mace
*A highly volatile weapon that loves destruction.*
* **Passive:** **Explosive Strikes** - Every hit generates a localized explosion that deals bonus damage.
* **Press F (Swap Hand):** **Cataclysmic Burst** - Trigger a massive AoE explosion that obliterates terrain and sends enemies flying.

---

## 🛠️ Configuration & Customization

DemiseMaces is fully customizable! Upon running the plugin for the first time, a `config.yml` will be generated in `plugins/DemiseMaces/`. 

You can use this file to modify:
* **Cooldowns** for every ability.
* **Velocity Multipliers** (How far dashes and pushes launch entities).
* **Radii** (The Area-of-Effect range for abilities).
* **Damage** (Base damage for projectiles and passives).
* **Freeze Duration** for the Frost Mace.

Once you have made your changes in `config.yml`, simply run `/demisemaces reload` in-game to apply them instantly—no server restart required!

---

## 📜 Commands & Permissions

* `/demisemaces give <mace_type>` - Grants the specified legendary mace to the user.
* `/demisemaces reload` - Reloads the `config.yml` file from disk.

**Permission Node:** `demisemaces.admin` (Defaults to Server Operators).

---

## 🚀 Installation

1. Download the compiled `DemiseMaces-1.0.0-SNAPSHOT.jar` file.
2. Drop it into your Minecraft Server's `plugins/` directory.
3. Start or restart your server.
4. Enjoy dealing demise with Maces!

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.
