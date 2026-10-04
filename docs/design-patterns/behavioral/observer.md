# Observer Design Pattern

The Observer pattern is a behavioral design pattern that defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## 🧩 What is it?

Imagine a newsletter subscription. You (the **Observer**) subscribe to a publisher (the **Subject**). Whenever the publisher releases a new edition, they send it to everyone on their subscriber list. You don't have to keep asking the publisher, "Is there a new edition yet?" Instead, the publisher pushes the update to you.

In software terms:
- **Subject**: The object that holds the state and maintains a list of observers.
- **Observer**: The objects that want to be notified when the subject's state changes.

## 🛠️ How to Use It

### The Core Components

1.  **Subject Interface**: Defines methods to attach, detach, and notify observers.
2.  **Concrete Subject**: The actual class that holds the state and triggers the notification.
3.  **Observer Interface**: Defines the `update()` method that the subject calls.
4.  **Concrete Observer**: Implements the `update()` method to react to the changes.

### Implementation Example (Weather Station)

Based on the implementation in this repository:

- **Subject**: `WeatherData` tracks temperature, humidity, and pressure.
- **Observers**: `CurrentConditionsDisplay` and `StatisticsDisplay` want to show this data.

When `WeatherData` calls `measurementsChanged()`, it notifies all registered displays, which then call their `display()` methods to show the latest weather info.

```java
// Simplifed flow
WeatherData weatherData = new WeatherData();
CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
StatisticsDisplay statsDisplay = new StatisticsDisplay(weatherData);

// When measurements change, both displays are updated automatically
weatherData.setMeasurements(80, 65, 30.4f); 
```

## 🎯 When to Use It

Use the Observer pattern when:
- A change to one object requires changing others, and you don't know how many objects need to change.
- An object should be able to notify other objects without making assumptions about who those objects are (low coupling).
- You need a "broadcast" style of communication.

**Common real-world examples:**
- **UI Frameworks**: Button click listeners in Java Swing or JavaScript.
- **MVC Architecture**: The Model is the Subject, and the View is the Observer.
- **Event-driven systems**: Message queues or Pub/Sub systems.

## 🚀 Why is it Helpful?

### 1. Loose Coupling
The Subject doesn't need to know the details of the Observer classes. It only knows that they implement the `Observer` interface. This means you can add new types of observers without modifying the Subject.

### 2. Dynamic Relationships
Observers can be added or removed at runtime. You can "subscribe" or "unsubscribe" from events on the fly.

### 3. Automatic Synchronization
It eliminates the need for "polling" (where an object constantly checks another object for changes), which saves CPU cycles and makes the application more responsive.

## ⚠️ Trade-offs to Consider

- **Order of Notification**: There is generally no guarantee of the order in which observers are notified.
- **Memory Leaks**: If observers are not properly removed (unsubscribed) when they are no longer needed, the Subject will keep a reference to them, preventing garbage collection (the "Lapsed Listener" problem).
- **Unexpected Updates**: Observers might be notified of changes they don't care about if the notification is too coarse.
