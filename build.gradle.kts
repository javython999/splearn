plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.spotbugs") version "6.2.1"
}

group = "com.errday"
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

val mockitoAgent: Configuration = configurations.create("mockitoAgent")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.security:spring-security-core")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit-pioneer:junit-pioneer:2.3.0")
    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")

    spotbugsPlugins("com.github.spotbugs:spotbugs:4.8.3") // 버전은 맞게 조정
    spotbugsPlugins("com.github.spotbugs:spotbugs:4.8.3")
    spotbugsPlugins("com.github.spotbugs:spotbugs-ant:4.8.3") // HTML 리포트를 위해 필요

    mockitoAgent("net.bytebuddy:byte-buddy-agent:1.14.12")
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports {
        named("html") {
            required.set(true)
            outputLocation.set(file("$rootDir/reports/spotbugs/${name}.html"))
        }
        named("xml") {
            required.set(false) // 필요 시 true로 변경
        }
    }
}
spotbugs {
    excludeFilter.set(file("$rootDir/spotbugs-exclude-filter.xml"))
}