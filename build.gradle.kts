
plugins {
    `java-library`
}

repositories {
    jcenter()
}

version = "0.1"

dependencies {
    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest:2.2")
}
