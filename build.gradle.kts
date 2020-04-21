
version = "0.1"
group = "org.trypticon.pdn"
description = "Java implementation of the Paint.NET file format"

plugins {
    `java-library`
    `maven-publish`
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("REPOSITORY")}")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("PASSWORD")
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}