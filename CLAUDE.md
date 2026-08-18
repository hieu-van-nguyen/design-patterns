# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands
- Build project: `mvn clean compile`
- Run all tests: `mvn test`
- Run a single test: `mvn test -Dtest=<TestClassName>`
- Install to local repo: `mvn clean install`

## Architecture and Structure
The codebase is a Java collection of design patterns, system design components, and certification exercises.

- `src/main/us/inest/dp`: Implementations of classic design patterns. Each pattern is organized into its own sub-package (e.g., `dp.singleton`, `dp.observer`, `dp.decorator`, `dp.factory_method`).
- `src/main/us/inest/ds`: Implementations of distributed systems and data structure components (e.g., Consistent Hashing, Rate Limiter, Token Bucket).
- `src/main/us/inest/scjp`: Java certification (SCJP) study and practice materials.
- `src/test`: JUnit 5 tests for the implementations.

The project uses Maven for dependency management and build automation.
