# Upgrade Plan: user-service (20260810064651)

- **Generated**: 2026-08-10 06:47:00
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 24.0.1: C:\Program Files\Java\jdk-24\bin (available, used by Step 1 and Step 3)
- Java 21: not available (baseline step will be skipped; project already targets Java 21)

**Build Tools**
- Maven Wrapper: 3.9.16 (`.mvn/wrapper/maven-wrapper.properties`) (available)
- Maven system installation: none available (wrapper will be used)

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

- Upgrade Java runtime to the latest LTS version available in the project context.

## Options

- Working branch: appmod/java-upgrade-20260810064651
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java 21

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- |
| Java | 21 | 21 | User requested latest LTS runtime target |
| Spring Boot | 4.1.0 | 4.1.0 | Project already uses current Spring Boot version |
| Maven Wrapper | 3.9.16 | 3.9.0 | Supports Java 21 and current Spring Boot 4.x |
| maven-compiler-plugin | managed by Spring Boot parent | 3.11.0 | Recommended for Java 21 support |
| Lombok | managed by parent | N/A | No change required |

## Derived Upgrades

- No dependency upgrades are required for this Java runtime upgrade because the project already declares `java.version` as `21` and uses a compatible Maven wrapper.
- No Spring Boot or build plugin version changes are required at this time.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|------------|---------|--------|--------|--------|
| pom.xml | java.version | 21 | none | 21 | Project already targets requested Java LTS |
| pom.xml | spring-boot-starter-parent | 4.1.0 | none | 4.1.0 | Already compatible and no user request to change Spring Boot |
| .mvn/wrapper/maven-wrapper.properties | Apache Maven | 3.9.16 | none | 3.9.16 | Wrapper is compatible with Java 21 |

### Source Code Changes

| File | Location | Current | Required Change | Reason |
|------|----------|---------|----------------|--------|
| N/A | N/A | N/A | N/A | No source code changes needed for the requested runtime upgrade |

### Configuration Changes

| File | Property/Setting | Current | Required Change | Reason |
|------|------------------|---------|-----------------|--------|
| N/A | N/A | N/A | N/A | No configuration changes required |

### CI/CD Changes

| File | Location | Current | Required Change |
|------|----------|---------|-----------------|
| N/A | N/A | N/A | N/A |

### Risks & Warnings

- **No Java 21 runtime installed on host**: The available JDK is Java 24, which can compile source/target 21, but the baseline Java 21 runtime cannot be validated locally. Mitigation: use Maven wrapper and ensure `java.version` remains 21, then verify compilation and tests.
- **No version control detected**: Changes will not be committed through Git. Keep manual backups if needed.
- **No system Maven installation**: The Maven wrapper will be the only build tool used.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Verify the available JDK and Maven wrapper before running the upgrade validation.
  - **Changes to Make**: Confirm available JDK 24 and Maven wrapper 3.9.16
  - **Verification**: `.\mvnw.cmd -v` and `java -version` | Expected: wrapper and JDK are usable

- Step 2: Setup Baseline
  - **Rationale**: The current project target Java version is 21, but Java 21 is not installed locally; baseline compilation with the requested runtime cannot be performed.
  - **Changes to Make**: none
  - **Verification**: skipped because Java 21 runtime is unavailable locally

- Step 3: Final Validation
  - **Rationale**: Confirm the existing Java 21 target and current Maven wrapper configuration compile and pass tests.
  - **Changes to Make**: none
  - **Verification**: `.\mvnw.cmd clean test -q` with JDK 24 and Maven Wrapper 3.9.16 | Expected: build and tests succeed
