# Design Patterns & System Design Repository

A comprehensive collection of design pattern implementations, system design components, and Java certification (SCJP) practice materials.

## 🚀 Project Structure

The repository is organized into three main areas:

### 🧩 Design Patterns (`src/main/us/inest/dp`)
Implementations of classic software design patterns to demonstrate structural, creational, and behavioral principles.
- **Singleton**: Ensuring a class has only one instance.
- **Observer**: Providing a subscription mechanism to notify multiple objects about any events.
- **Decorator**: Attaching additional responsibilities to an object dynamically.
- **Factory Method**: Defining an interface for creating an object, but letting subclasses decide which class to instantiate.

### ⚙️ System Design (`src/main/us/inest/ds`)
Implementations of common distributed systems components and algorithms.
- **Consistent Hashing**: For load balancing and distributed caching.
- **Rate Limiter & Token Bucket**: For controlling the rate of traffic sent or received.
- **Leader Election**: For coordinating nodes in a distributed system.
- **Deadlock Detection**: For identifying circular dependencies in resource allocation.

### 📚 SCJP Study (`src/main/us/inest/scjp`)
Practice exercises and code snippets for the Sun Certified Java Programmer (SCJP) certification.

## 🛠️ Getting Started

### Prerequisites
- Java 8 or higher (Java 11 recommended)
- Apache Maven

### Building the Project
To compile the project and run all tests:
```bash
mvn clean compile
mvn test
```

### Running a Specific Test
To run a single test class:
```bash
mvn test -Dtest=<TestClassName>
```

## 💻 Tech Stack
- **Language**: Java
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Serialization**: Jackson
