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
tasks.withType<Test> {
    useJUnitPlatform()
    // Set system properties for tests if needed
    systemProperty("spring.profiles.active", "test")
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
// Modified Javadoc task to include tests
tasks.register<Javadoc>("generateJavadoc") {
    source = sourceSets.main.get().allJava + sourceSets.test.get().allJava
    classpath = configurations.compileClasspath.get() + configurations.testCompileClasspath.get()
    setDestinationDir(file("${layout.buildDirectory.asFile.get()}/docs/javadoc"))

    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        title = "JobPortal API Documentation"
        memberLevel = JavadocMemberLevel.PROTECTED
        // Add links to JDK and Spring Boot Javadoc
        links("https://docs.oracle.com/en/java/javase/21/docs/api/")
        links("https://docs.spring.io/spring-framework/docs/current/javadoc-api/")
        // Add test packages to groups if desired
        group("Core API", "me.josephsf.jobportaljosephsfeir.*")
        group("Test API", "me.josephsf.jobportaljosephsfeir.*.test")
        // Enable additional Javadoc tags if needed
        tags("apiNote:a:API Note:", "implSpec:a:Implementation Specification:", "implNote:a:Implementation Note:")
    }
}

// Also update the standard javadoc task
tasks.javadoc {
    source = sourceSets.main.get().allJava + sourceSets.test.get().allJava
    classpath = configurations.compileClasspath.get() + configurations.testCompileClasspath.get()

    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        title = "Job Portal API Documentation"
        links("https://docs.oracle.com/en/java/javase/21/docs/api/")
    }
}