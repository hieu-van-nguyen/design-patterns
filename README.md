# Design Patterns & System Design Repository

A comprehensive collection of design pattern implementations, system design components, and Java certification (SCJP) practice materials.

## 🚀 Project Structure

The repository is organized into three main areas:

### 🧩 Design Patterns (`src/main/us/inest/dp`)
Implementations of classic software design patterns to demonstrate structural, creational, and behavioral principles.

#### Creational Patterns
Patterns that deal with object creation mechanisms, trying to create objects in a manner suitable to the situation.
- **Abstract Factory**: Provides an interface for creating families of related or dependent objects.
- **Builder**: Separates construction of a complex object from its representation.
- **Factory Method**: Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
- **Prototype**: Specifies the kinds of objects to create using a prototypical instance.
- **Singleton**: Ensures a class has only one instance and provides a global point of access to it. ([Details](docs/patterns/creational/singleton.md))

#### Structural Patterns
Patterns that deal with object composition or the relationship between entities.
- **Adapter**: Converts the interface of a class into another interface clients expect.
- **Bridge**: Decouples an abstraction from its implementation so that the two can vary independently.
- **Composite**: Composes objects into tree structures to represent part-whole hierarchies.
- **Decorator**: Attaches additional responsibilities to an object dynamically.
- **Facade**: Provides a unified interface to a set of interfaces in a subsystem.
- **Flyweight**: Uses sharing to support large numbers of fine-grained objects efficiently.
- **Proxy**: Provides a surrogate or placeholder for another object to control access to it.

#### Behavioral Patterns
Patterns that identify common communication patterns between objects and realize these patterns.
- **Chain of Responsibility**: Passes requests along a chain of handlers.
- **Command**: Encapsulates a request as an object, thereby letting you parameterize clients with different requests.
- **Interpreter**: Given a language, defines a representation for its grammar along with an interpreter.
- **Iterator**: Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
- **Mediator**: Defines an object that encapsulates how a set of objects interact.
- **Memento**: Captures and externalizes an object's internal state so that the object can be restored to this state later.
- **Observer**: Provides a subscription mechanism to notify multiple objects about any events.
- **State**: Allows an object to alter its behavior when its internal state changes.
- **Strategy**: Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
- **Template Method**: Defines the skeleton of an algorithm in an operation, deferring some steps to subclasses.
- **Visitor**: Represents an operation to be performed on the elements of an object structure.

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
