# ⚔️ DemiseMaces

A modern, high-performance Java Minecraft Paper Plugin built for **Paper 1.21+** using **Java 21** and **Gradle (Kotlin DSL)**. 

This repository is pre-configured with everything you need to develop, compile, and test your Paper plugin efficiently.

---

## ✨ Features Pre-Configured

* **Modern Gradle (Kotlin DSL)**: Fast builds, type-safe configuration, and modern dependency management.
* **Paperweight Run-Paper Plugin**: Launch a local Paper server with your plugin pre-loaded in a single command.
* **Java 21 Toolchain**: Fully configured to take advantage of Java 21's performance and language features.
* **Starter Mace Mechanic**: Includes an epic lightning thunder effect and messages when striking a mob/player with a Mace.
* **GNU GPL v3 License**: Ready-to-go Open Source licensing.
* **Pre-configured `.gitignore`**: Keeps your repository clean from build files, IDE settings, and test server folders.

---

## 🛠️ Prerequisites

To build and run this plugin, you will need:
* **Java 21** or higher (LTS)
* An IDE (e.g., **IntelliJ IDEA** or **VS Code** with Java extensions)

*Note: You do not need to install Gradle manually. The project includes the Gradle Wrapper (`gradlew`).*

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/DemiseMaces.git
cd DemiseMaces
```

### 2. Build the plugin
Compile the code and pack it into a `.jar` file:
```bash
./gradlew build
```
The compiled jar file will be generated in `build/libs/DemiseMaces-1.0.0-SNAPSHOT.jar`.

### 3. Run a Local Test Server
The repository is equipped with the `run-paper` plugin which automates server setup. Simply run:
```bash
./gradlew runServer
```
This command will:
1. Automatically download the correct Paper 1.21 server jar.
2. Set up a local test environment in the `run/` directory (ignored by Git).
3. Automatically load the compiled **DemiseMaces** plugin.
4. Open the Minecraft server console in your terminal!

---

## 📁 Repository Structure

```text
DemiseMaces/
├── .gitignore                      # Git ignore file (builds, local server run)
├── LICENSE                         # GNU GPL v3.0 License
├── README.md                       # Repository documentation
├── build.gradle.kts                # Main Gradle build configuration
├── settings.gradle.kts             # Gradle project name definition
├── gradlew                         # Unix Gradle Wrapper shell script
├── gradlew.bat                     # Windows Gradle Wrapper script
├── gradle/                         # Gradle Wrapper files folder
└── src/
    └── main/
        ├── java/
        │   └── com/github/jiten/
        │       └── demisemaces/
        │           └── DemiseMaces.java   # Main plugin class
        └── resources/
            └── paper-plugin.yml    # Paper plugin metadata configuration
```

---

## 💻 Technical Details

### Plugin Lifecycle
* **`onEnable()`**: Triggered when the server loads the plugin. Registers events and logs initialization messages.
* **`onDisable()`**: Triggered when the server shuts down or reloads. Safely disposes of resources.

### Custom Mechanics Included
* **`onMaceHit(EntityDamageByEntityEvent)`**: Listens to damage events. If a player holding a `MACE` hits an entity, it plays a dramatic lightning storm thunder sound (`Sound.ENTITY_LIGHTNING_BOLT_THUNDER`) and announces a demise blow to the player.

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**. See the [LICENSE](LICENSE) file for more information.
