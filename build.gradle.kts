val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val hikaricp_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.hidetake.swagger.generator") version "2.19.2"
}

group = "ru.droptableusers.sampleapi"
version = "0.0.1"

application {
    mainClass.set("ru.droptableusers.sampleapi.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // KTOR SERVER
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // SWAGGER
    implementation("io.github.smiley4:ktor-swagger-ui:2.8.0")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")

    // HASH
    implementation("de.nycode:bcrypt:2.2.0")
    implementation("org.mindrot:jbcrypt:0.4")

    // DOTENV
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // TELEGRAM
    implementation("com.github.pengrad:java-telegram-bot-api:6.0.1")
    implementation("org.telegram:telegrambots:5.3.0")

    // LOGGING
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("ch.qos.logback:logback-core:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.10")
    implementation("org.slf4j:slf4j-simple:2.0.10")

    // DATABASE
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:42.7.0")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.generateReDoc.configure {
    inputFile = file("data/openapi.yml")
    outputDir = file("data")
    title = "DropTableUsers API"
}

tasks.register("copyDependencies") {
    doLast {
        val libsDir = File("$buildDir/libs/libraries")
        libsDir.mkdirs()

        configurations.getByName("runtimeClasspath").files.forEach {
            if (it.name.endsWith(".jar")) {
                it.copyTo(File(libsDir, it.name))
            }
        }
    }
}
