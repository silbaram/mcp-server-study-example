plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"   // Fat‑JAR 플러그인
}

val mcpVersion = "0.9.0"   // 2025‑03‑25 기준 최신
group = "io.github.silbaram"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // BOM( Bill‑of‑Materials )으로 MCP 의존성 버전을 한 번에 맞춤
    implementation(platform("io.modelcontextprotocol.sdk:mcp-bom:$mcpVersion"))
    implementation("io.modelcontextprotocol.sdk:mcp")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    // 메인 클래스 경로를 실제 Main 클래스 패키지·이름으로 맞춰주세요
    mainClass.set("io.github.silbaram.Main")
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "io.github.silbaram.Main"
    }
}

// 컴파일 옵션: UTF‑8
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}
