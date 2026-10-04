# Decorator Design Pattern

The Decorator pattern is a structural design pattern that allows behavior to be added to an individual object, either statically or dynamically, without affecting the behavior of other objects from the same class.

## 🧩 What is it?

Imagine you are ordering a coffee. You start with a basic coffee (the **Component**). Then, you might want to add milk, sugar, or mocha syrup. Each of these additions is a **Decorator**. 

Instead of creating a class for every possible combination (e.g., `CoffeeWithMilk`, `CoffeeWithMilkAndSugar`, `CoffeeWithMochaAndMilk`), the Decorator pattern lets you wrap the basic coffee in "layers" of additions. Each layer adds its own cost and description to the base object.

## 🛠️ How to Use It

### The Core Components

1.  **Component**: The base interface or abstract class that defines the object to be decorated.
2.  **Concrete Component**: The basic implementation of the component.
3.  **Decorator**: An abstract class that implements the Component interface and contains a reference to a Component object.
4.  **Concrete Decorator**: Classes that extend the Decorator and add specific behaviors or state.

### Implementation Example (Coffee Shop)

Based on the implementation in this repository:

- **Component**: `Beverage` (abstract class defining `getDescription()` and `cost()`).
- **Concrete Component**: `Espresso`, `HouseBlend`, `Mocha` (as a base beverage).
- **Decorator**: `CondimentDecorator` (extends `Beverage`).
- **Concrete Decorators**: `Mocha`, `Soy`, `Whip`.

```java
// Creating a House Blend coffee with Mocha and Soy
Beverage myCoffee = new HouseBlend(); // Basic coffee
myCoffee = new Mocha(myCoffee);      // Wrap with Mocha
myCoffee = new Soy(myCoffee);        // Wrap with Soy

System.out.println(myCoffee.getDescription()); // House Blend, Mocha, Soy
System.out.println(myCoffee.cost());           // Base Cost + Mocha Cost + Soy Cost
```

## 🎯 When to Use It

Use the Decorator pattern when:
- You want to add responsibilities to objects dynamically without affecting other objects of the same class.
- Using inheritance to extend behavior would lead to a "class explosion" (too many subclasses for every possible combination of features).
- You need to be able to withdraw a responsibility from an object at runtime.

**Common real-world examples:**
- **Java I/O Streams**: `new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")))`.
- **GUI Components**: Adding a scrollbar or a border to a window component.
- **Middleware**: Adding logging, authentication, or compression layers to an HTTP request handler.

## 🚀 Why is it Helpful?

### 1. Single Responsibility Principle
Instead of one giant class with every possible feature, you can break the functionality into several smaller decorator classes that each do one thing well.

### 2. Flexibility over Inheritance
Inheritance is static (happens at compile time). Decoration is dynamic (happens at runtime). You can decide which "layers" to add based on user input or configuration.

### 3. Open/Closed Principle
The base component is "closed" for modification (you don't need to change `Beverage` to add a new condiment) but "open" for extension (you can just create a new `CondimentDecorator` subclass).

## ⚠️ Trade-offs to Consider

- **Complexity of Initialization**: Creating a heavily decorated object can involve many nested constructors (e.g., `new A(new B(new C(new D())))`), which can be cumbersome.
- **Small Object Proliferation**: You end up with many small classes that might look very similar.
- **Interface Issues**: If the base component has many methods, the decorator must implement/delegate all of them, which can lead to boilerplate code.
