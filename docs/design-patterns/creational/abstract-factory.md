# Abstract Factory Design Pattern

## What is the Abstract Factory Pattern?

The **Abstract Factory** is a creational design pattern that provides an interface for creating **families of related or dependent objects** without specifying their concrete classes.

Where the [Factory Method](factory-method.md) creates *one* product and lets subclasses decide its type, an Abstract Factory groups *several* factory methods together so that all the products it creates are guaranteed to belong to the same family (same theme, platform, vendor, region, etc.).

### Core Concept
The pattern has four participants:

| Participant | Role |
| :--- | :--- |
| **AbstractFactory** | Declares a creation method for each kind of product (`createButton()`, `createCheckbox()`, ...). |
| **ConcreteFactory** | Implements the creation methods to produce products of one specific family (e.g., `WindowsFactory`, `MacFactory`). |
| **AbstractProduct** | An interface for each kind of product (`Button`, `Checkbox`). |
| **ConcreteProduct** | A family-specific implementation of a product (`WindowsButton`, `MacCheckbox`). |

The **Client** only ever talks to `AbstractFactory` and `AbstractProduct` types.

```
            Client
              |
              v
      <<AbstractFactory>>  ---------------->  <<Button>>     <<Checkbox>>
     createButton()                              ^   ^           ^   ^
     createCheckbox()                            |   |           |   |
        ^           ^                            |   |           |   |
        |           |                            |   |           |   |
 WindowsFactory   MacFactory  ---creates--->  WinBtn MacBtn  WinChk MacChk
```

---

## Key Characteristics

- **Creates families of products** – each concrete factory produces a full, consistent set of related objects.
- **Enforces consistency** – a client using `MacFactory` can never accidentally mix in a `WindowsButton`.
- **Programs to interfaces** – the client depends only on abstract factory and abstract product types.
- **Factory is usually swapped as a whole** – changing the family means replacing one factory object, typically chosen once at startup/configuration time.
- **Built from factory methods** – each `createXxx()` method on the abstract factory is effectively a factory method.
- **Concrete factories are often singletons** – one instance per family is usually sufficient.
- **Two axes of variation** – *product kinds* (rows: Button, Checkbox) and *product families* (columns: Windows, Mac). Adding a family is easy; adding a product kind is hard (see [Other Considerations](#other-considerations)).

---

## When to Use It?

Use the Abstract Factory pattern when:

1. **Your code must work with various families of related products**, and you don't want it to depend on their concrete classes (they may be unknown up front or you want to allow future extension).
2. **Products from one family are designed to be used together**, and you need to enforce that constraint.
3. **The system should be configurable with one of several families** – e.g., chosen by OS, environment, tenant, or configuration file.
4. **You want to expose a library of products** while revealing only their interfaces, not their implementations.
5. **You already have several Factory Methods** that always vary together – that's a signal to consolidate them into an abstract factory.

Avoid it when you only have a single product type (use Factory Method) or when there's just one family that is unlikely to change (plain constructors are simpler).

---

## How to Use It

The example below builds a cross-platform UI toolkit where the widgets must match the operating system.

### 1. Define the Abstract Products
Declare an interface for each distinct product in the family.

```java
public interface Button {
    void render();
    void onClick(Runnable action);
}

public interface Checkbox {
    void render();
    boolean isChecked();
}
```

### 2. Create Concrete Products for Each Family
Implement every abstract product once per family.

```java
// ----- Windows family -----
public class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering a Windows-style button");
    }

    @Override
    public void onClick(Runnable action) {
        action.run();
    }
}

public class WindowsCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering a Windows-style checkbox");
    }

    @Override
    public boolean isChecked() {
        return false;
    }
}

// ----- Mac family -----
public class MacButton implements Button {
    @Override
    public void render() {
        System.out.println("Rendering a macOS-style button");
    }

    @Override
    public void onClick(Runnable action) {
        action.run();
    }
}

public class MacCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering a macOS-style checkbox");
    }

    @Override
    public boolean isChecked() {
        return false;
    }
}
```

### 3. Declare the Abstract Factory
One creation method per product kind.

```java
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
```

### 4. Implement a Concrete Factory per Family
Each factory returns only products from its own family.

```java
public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

public class MacFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}
```

### 5. Write Client Code Against the Abstractions
The client receives a factory (ideally via constructor injection) and never references a concrete class.

```java
public class Application {
    private final Button button;
    private final Checkbox checkbox;

    public Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }

    public void paint() {
        button.render();
        checkbox.render();
    }
}
```

### 6. Choose the Factory Once, at the Composition Root
The only place that knows about concrete classes is the bootstrap code.

```java
public class Main {
    public static void main(String[] args) {
        GUIFactory factory = createFactory(System.getProperty("os.name"));
        Application app = new Application(factory);
        app.paint();
    }

    private static GUIFactory createFactory(String osName) {
        if (osName.toLowerCase().contains("mac")) {
            return new MacFactory();
        }
        return new WindowsFactory();
    }
}
```

Switching the entire look-and-feel now means changing a single line, and there is no way for the application to mix Windows and Mac widgets.

### Testing Tip
Because the client depends on `GUIFactory`, you can pass a test double:

```java
GUIFactory stubFactory = new GUIFactory() {
    public Button createButton()     { return mock(Button.class); }
    public Checkbox createCheckbox() { return mock(Checkbox.class); }
};
new Application(stubFactory).paint();
```

---

## Why is it Helpful?

### 1. Guaranteed Product Compatibility
All products obtained from one factory are designed to work together. The pattern makes "mismatched family" bugs impossible at the type level of the client.

### 2. Loose Coupling
Client code depends only on interfaces. Concrete classes are referenced in exactly one place – the concrete factory – which keeps the rest of the codebase free of `new ConcreteXxx()` calls.

### 3. Single Responsibility Principle
Creation logic for a whole family lives in one class, making it easy to find, review and change.

### 4. Open/Closed Principle
Adding a new family (e.g., `LinuxFactory` + `LinuxButton` + `LinuxCheckbox`) requires **no changes** to existing client or factory code.

### 5. Easy Swapping and Testing
Replacing the factory instance changes the behavior of the whole subsystem – ideal for switching environments (prod vs. test), vendors, or themes, and for injecting mocks in unit tests.

---

## Where to Use It?

Real-world scenarios where the pattern fits naturally:

| Domain | Abstract Factory | Families | Products |
| :--- | :--- | :--- | :--- |
| **Cross-platform UI** | `GUIFactory` | Windows, macOS, Linux | Button, Checkbox, Menu |
| **UI theming** | `ThemeFactory` | Light, Dark, High-contrast | Colors, Fonts, Icons |
| **Database access** | `DaoFactory` | MySQL, PostgreSQL, MongoDB | `UserDao`, `OrderDao`, `Connection` |
| **Cloud providers** | `CloudFactory` | AWS, Azure, GCP | Storage, Queue, Compute client |
| **Payments** | `PaymentFactory` | Stripe, PayPal, Adyen | Gateway, Refund processor, Webhook validator |
| **Regional/pizza ingredients** | `PizzaIngredientFactory` | NY, Chicago | Dough, Sauce, Cheese |
| **Games** | `EnemyFactory` | Easy, Medium, Hard | Soldier, Vehicle, Boss |

Examples in the JDK and popular frameworks:

- `javax.xml.parsers.DocumentBuilderFactory` / `javax.xml.transform.TransformerFactory`
- `javax.xml.xpath.XPathFactory`
- `java.sql.Connection` – acts as a factory for related `Statement`, `PreparedStatement`, `CallableStatement` objects specific to a driver
- `java.awt.Toolkit` – creates platform-specific peers for AWT components
- Spring's `BeanFactory` / `ApplicationContext` (a generalized, configurable factory)

> **Tip:** The pizza example in this repo (`us.inest.dp.creational.factory_method`) can be extended with a `PizzaIngredientFactory` (e.g., `NYPizzaIngredientFactory`, `ChicagoPizzaIngredientFactory`) so each regional store uses a consistent set of dough, sauce, and cheese – a classic Abstract Factory.

---

## Other Considerations

### Drawbacks
- **Adding a new product kind is expensive.** Adding `createSlider()` to `GUIFactory` forces a change to *every* concrete factory. The pattern favors new families over new product kinds.
- **Class explosion.** `F` families × `P` products → `F × P` concrete product classes plus `F` factories.
- **Extra indirection.** For small or stable systems, the abstraction can be over-engineering.

### Abstract Factory vs. Factory Method

| Aspect | Factory Method | Abstract Factory |
| :--- | :--- | :--- |
| **Creates** | One product | A family of related products |
| **Mechanism** | Inheritance – subclass overrides a method | Composition – client is given a factory object |
| **Granularity** | Single method | Object with several creation methods |
| **Typical relationship** | Often used *inside* an abstract factory | Usually *implemented with* factory methods |

### Relationship to Other Patterns
- **Singleton** – concrete factories are often singletons since one instance per family is enough.
- **Builder** – Builder constructs *one complex object* step by step; Abstract Factory returns *families of simple objects* immediately.
- **Prototype** – a factory can be implemented by cloning registered prototype products instead of subclassing, reducing the number of factory classes.
- **Bridge** – an Abstract Factory can create and configure a specific Bridge implementation.
- **Facade** – an Abstract Factory can be used as an alternative to Facade to hide platform-specific creation.

### Implementation Tips
- **Inject the factory** (constructor injection / DI container) rather than calling a static lookup throughout the code.
- **Select the factory in one place** – a configuration-driven registry such as `Map<String, Supplier<GUIFactory>>` avoids sprawling `if/else` chains.
- **Mitigating new product kinds:** a parameterized method like `<T> T create(Class<T> type)` adds flexibility but sacrifices compile-time safety – use with care.
- **Default methods** in Java interfaces can provide fallback implementations when adding a new product kind, easing migration of existing factories.
- **Keep products immutable or stateless where possible** so factories can safely be shared across threads.

---

## Summary Table

| Feature | Description |
| :--- | :--- |
| **Type** | Creational Pattern |
| **Intent** | Provide an interface for creating families of related or dependent objects without specifying their concrete classes. |
| **Key Benefit** | Guarantees that products used together are compatible, while decoupling clients from concrete classes. |
| **Main Trade-off** | Easy to add new families; hard to add new product kinds. |
| **Common Use Case** | Cross-platform UI toolkits, multi-vendor integrations (DB, cloud, payments), theming. |
