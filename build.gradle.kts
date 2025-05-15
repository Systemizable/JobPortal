plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "me.josephsf"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT for Authentication
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Annotation Processors
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Corrected Javadoc task
tasks.register<Javadoc>("generateJavadoc") {
    source = sourceSets.main.get().allJava
    classpath = configurations.compileClasspath.get()
    setDestinationDir(file("${layout.buildDirectory.asFile.get()}/docs/javadoc"))

    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        title = "JobPortal API Documentation"
        memberLevel = JavadocMemberLevel.PROTECTED
    }
}

// Additional Javadoc configuration for the default javadoc task
tasks.javadoc {
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        title = "Job Portal API Documentation"
    }
}