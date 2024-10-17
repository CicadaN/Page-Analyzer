plugins {
    application
    jacoco
    checkstyle
    id("io.freefair.lombok") version "8.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.assertj:assertj-core:3.25.2")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.12")

    implementation ("org.postgresql:postgresql:42.6.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")

    implementation("io.javalin:javalin:6.3.0")
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.12")

    testImplementation("org.assertj:assertj-core:3.25.2")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}




