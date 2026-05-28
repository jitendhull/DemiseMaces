plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "com.github.jiten.demisemaces"
version = "1.1.0-Alpha"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    runServer {
        // Configure the test server to use Minecraft 1.21
        minecraftVersion("1.21")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
