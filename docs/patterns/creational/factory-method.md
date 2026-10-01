# Factory Method Design Pattern

## What is the Factory Method Pattern?

The **Factory Method** is a creational design pattern that provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.

Instead of calling a constructor directly to instantiate an object (using the `new` operator), you call a "factory method" to do it. This decouples the client code from the concrete classes it needs to instantiate.

### Core Concept
In a typical scenario, you have a base `Product` interface and multiple `ConcreteProduct` implementations. You also have a `Creator` class that declares the factory method. Subclasses of the `Creator` override this method to return a specific `ConcreteProduct`.

---

## How to Use It

### 1. Define the Product Interface
Create an interface or abstract class that defines the behavior of the objects the factory will produce.

```java
public interface Transport {
    void deliver();
}
```

### 2. Create Concrete Products
Implement the interface for different variations of the product.

```java
public class Truck implements Transport {
    @Override
    public void deliver() {
        System.out.println("Deliver by land in a box.");
    }
}

public class Ship implements Transport {
    @Override
    public void deliver() {
        System.out.println("Deliver by sea in a container.");
    }
}
```

### 3. Define the Creator (The Factory)
The Creator class declares the factory method. It can be abstract, or it can provide a default implementation.

```java
public abstract class Logistics {
    // The "Factory Method"
    public abstract Transport createTransport();

    public void planDelivery() {
        // Call the factory method to create a product object
        Transport t = createTransport();
        t.deliver();
    }
}
```

### 4. Implement Concrete Creators
Each concrete creator overrides the factory method to return a specific product.

```java
public class RoadLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Truck();
    }
}

public class SeaLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Ship();
    }
}
```

### 5. Client Usage
The client code works with the abstract `Logistics` and `Transport` classes, remaining unaware of the concrete types being used.

```java
public class Client {
    public static void main(String[] args) {
        Logistics logistics;
        
        // Depending on configuration or environment, pick a creator
        boolean useSea = false;
        if (useSea) {
            logistics = new SeaLogistics();
        } else {
            logistics = new RoadLogistics();
        }
        
        logistics.planDelivery(); 
    }
}
```

---

## Why is it Helpful?

### 1. Decoupling (Dependency Inversion)
The client code does not need to know the exact class of the object it is creating. It only knows about the interface. This means you can add new product types (e.g., `AirLogistics` returning `Plane`) without changing the client code.

### 2. Single Responsibility Principle
You move the product creation code into one place (the factory method), making the rest of your code cleaner and easier to maintain.

### 3. Open/Closed Principle
Your code is **open for extension** (you can add new creators and products) but **closed for modification** (you don't have to change existing code to introduce new types).

### 4. Flexibility
It allows a system to be independent of how its products are created, composed, and represented. This is especially useful when the exact type of the object isn't known until runtime.

## Summary Table

| Feature | Description |
| :--- | :--- |
| **Type** | Creational Pattern |
| **Intent** | Define an interface for creating an object, but let subclasses decide which class to instantiate. |
| **Key Benefit** | Decouples creator from concrete products. |
| **Common Use Case** | Frameworks where the library provides the "skeleton" but the user provides the specific "implementation". |
