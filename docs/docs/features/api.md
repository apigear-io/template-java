---
sidebar_position: 1
sidebar_label: "API"
title: "API Feature: Interfaces, Structs & Enums for Java"
description: "Generate Java interfaces, structs, enums, and abstract base classes from API definitions. Includes async operations, property change listeners, and ready-state management."
keywords: [java interface, java enum, java struct, api generation, apigear, completablefuture]
---

import CodeBlock from '@theme/CodeBlock';
import helloWorldModuleComponent from '!!raw-loader!./data/helloworld.module.yaml';

# API Feature

The `api` feature is the foundation for code generation. It generates:

- Java interfaces for each API interface
- Event listener interfaces for property changes, signals, and ready-state
- Abstract base classes with thread-safe listener management
- Data types: classes for structs, enums with Jackson serialization annotations
- Async operation signatures via `CompletableFuture`

:::note
The `api` feature generates interface definitions only. For a working implementation, also enable the [stubs](stubs.md) feature.
:::

## File overview

Using the example API definition:

<details>
  <summary>Hello World API (click to expand)</summary>
  <CodeBlock language="yaml" showLineNumbers>{helloWorldModuleComponent}</CodeBlock>
</details>

The following file structure is generated in the `ioWorld_api` module:

```bash
📂ioWorld/ioWorld_api
 ┣ 📜build.gradle
 ┗ 📂src/main/java/ioWorld/ioWorld_api
   ┣ 📜IHello.java
   ┣ 📜IHelloEventListener.java
   ┣ 📜AbstractHello.java
   ┣ 📜RemoteOperationException.java
   ┣ 📜Message.java
   ┗ 📜When.java
```

Each API type gets its own source file. For a module with multiple interfaces, each interface generates its own `I{Name}.java`, `I{Name}EventListener.java`, and `Abstract{Name}.java`.

## Enums

For each enum in your API, a Java enum is generated with integer-backed values and Jackson serialization:

```java
public enum When {
    @JsonProperty("0")
    Now(0),
    @JsonProperty("1")
    Soon(1),
    @JsonProperty("2")
    Never(2);

    private final int value;

    When(int value) { this.value = value; }

    public int getValue() { return value; }

    public static When fromValue(int value) {
        for (When e : values()) {
            if (e.value == value) return e;
        }
        throw new IllegalArgumentException("Unknown int value: " + value);
    }
}
```

Each enum includes:
- Integer-backed values matching the API definition
- `getValue()` to retrieve the integer value
- `fromValue(int)` static factory for deserialization
- Jackson `@JsonProperty` annotations for JSON serialization

## Structs

For each struct in your API, a Java class is generated:

```java
public class Message {
    @JsonProperty("content")
    public String content;

    public Message(String content) { this.content = content; }
    public Message() { }
    public Message(Message other) { this.content = other.content; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message other = (Message) o;
        return this.content == other.content;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
```

Each struct includes:
- **All-args constructor** — initialize all fields at construction
- **No-arg constructor** — useful for frameworks and default initialization
- **Copy constructor** — create a copy from another instance
- **`equals` / `hashCode`** — equality checks for use in collections and change detection
- **Jackson `@JsonProperty`** annotations for JSON serialization

:::caution
The generated `equals()` uses `==` for object fields (e.g., `this.content == other.content`), which performs reference comparison rather than value comparison. For String fields, this works with interned strings but may fail for dynamically constructed values. Consider modifying the generated `equals()` to use `Objects.equals()` if value equality is needed.
:::

:::note
The generated structs use [Jackson](https://github.com/FasterXML/jackson) annotations (`@JsonProperty`) for JSON serialization. Add the Jackson dependency to your project:
```groovy
implementation 'com.fasterxml.jackson.core:jackson-annotations:2.17.0'
```
:::

## Interfaces

For each interface in your API module, an `IHello` interface is generated with property accessors, operations, and event management:

```java
public interface IHello {
    // properties
    void setLast(Message last);
    Message getLast();
    void fireLastChanged(Message newValue);

    // methods
    int say(Message msg, When when);
    CompletableFuture<Integer> sayAsync(Message msg, When when);

    // signals
    void fireJustSaid(Message msg);

    // ready state
    boolean _isReady();
    void fire_readyStatusChanged(boolean isReady);

    // listener management
    void addEventListener(IHelloEventListener listener);
    void removeEventListener(IHelloEventListener listener);
}
```

### Properties

Properties are exposed through getter/setter methods. Each property also has a `fire` method to notify listeners of changes:

- **`setLast(Message)`** — set the property value
- **`getLast()`** — get the current value
- **`fireLastChanged(Message newValue)`** — notify all listeners of a change

### Operations

Operations are generated with two variants:

#### Synchronous

The basic synchronous method blocks until completion:

```java
int say(Message msg, When when);
```

#### Asynchronous with CompletableFuture

For non-blocking code, use the async variant:

```java
CompletableFuture<Integer> sayAsync(Message msg, When when);
```

Usage example:

```java
// Non-blocking call — returns immediately
CompletableFuture<Integer> future = hello.sayAsync(msg, When.Now);

// Option 1: chain a callback
future.thenAccept(result -> System.out.println("say returned: " + result));

// Option 2: wait for result (blocks current thread)
int result = future.get();
```

### Ready State

Each interface includes a ready-state mechanism:

- **`_isReady()`** — returns whether the implementation is ready to handle requests
- **`fire_readyStatusChanged(boolean)`** — notifies listeners when the ready state changes

This is particularly useful for the [android](android.md) feature where the client becomes ready after binding to the service.

## Event Listeners

For each interface, an event listener is generated to receive property changes, signals, and ready-state notifications:

```java
public interface IHelloEventListener {
    void onLastChanged(Message newValue);
    void onJustSaid(Message msg);
    void on_readyStatusChanged(boolean isReady);
}
```

- **`onLastChanged(Message newValue)`** — called when a property changes; receives the new value
- **`onJustSaid(Message msg)`** — called when a signal is emitted
- **`on_readyStatusChanged(boolean isReady)`** — called when the ready state changes

## Abstract Base Class

The `AbstractHello` class provides a partial implementation with thread-safe listener management:

```java
public abstract class AbstractHello implements IHello {
    private Collection<IHelloEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(IHelloEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(IHelloEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void fireLastChanged(Message newValue) {
        for (IHelloEventListener listener : listeners) {
            listener.onLastChanged(newValue);
        }
    }

    @Override
    public void fireJustSaid(Message msg) {
        for (IHelloEventListener listener : listeners) {
            listener.onJustSaid(msg);
        }
    }

    @Override
    public void fire_readyStatusChanged(boolean isReady) {
        for (IHelloEventListener listener : listeners) {
            listener.on_readyStatusChanged(isReady);
        }
    }
}
```

The abstract class provides:
- **Thread-safe listener collection** using `ConcurrentHashMap.newKeySet()` — safe for concurrent add/remove/iterate
- **Fire method implementations** — iterate all listeners and dispatch events
- **No property storage** — implementations must provide their own storage (see [stubs](stubs.md))

To create your own implementation, extend this class and implement the remaining abstract methods (`setLast`, `getLast`, `say`, `sayAsync`, `_isReady`). Alternatively, enable the [stubs](stubs.md) feature to generate a ready-to-use implementation.

## Error Handling

The `api` feature generates a `RemoteOperationException` class per module for propagating errors across the IPC boundary. When a backend operation throws an exception, the service catches it, classifies it with an error code, and sends the error back to the client. The client wraps it in a `RemoteOperationException` and completes the `CompletableFuture` exceptionally.

```java
public class RemoteOperationException extends RuntimeException {
    public static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_SERVICE_DISCONNECTED = 1;
    public static final int ERROR_SERVICE_NOT_READY = 2;
    public static final int ERROR_INVALID_ARGUMENT = 3;
    public static final int ERROR_NOT_IMPLEMENTED = 4;
    public static final int ERROR_INTERNAL = 5;

    public int getErrorCode();
}
```

### Error codes

| Code | Constant | Meaning | Source |
|------|----------|---------|--------|
| 0 | `ERROR_UNKNOWN` | Generic/unclassified error | Default when no code is provided |
| 1 | `ERROR_SERVICE_DISCONNECTED` | Binder connection lost | Client detects disconnect |
| 2 | `ERROR_SERVICE_NOT_READY` | Backend not ready | Reserved for future use |
| 3 | `ERROR_INVALID_ARGUMENT` | Bad input from caller | `IllegalArgumentException` on service side |
| 4 | `ERROR_NOT_IMPLEMENTED` | Operation not supported | `UnsupportedOperationException` on service side |
| 5 | `ERROR_INTERNAL` | Unexpected backend exception | Default for all other caught exceptions |

The service-side mapping uses `instanceof` checks on standard Java exception types. If the backend throws a `RemoteOperationException` itself (e.g., from a nested service call), its error code is preserved.

### Handling errors

```java
// Async — check the exception
// whenComplete receives RemoteOperationException directly
// (not wrapped in CompletionException) because the client
// calls completeExceptionally() with it
hello.sayAsync(msg, When.Now).whenComplete((result, throwable) -> {
    if (throwable instanceof RemoteOperationException) {
        int code = ((RemoteOperationException) throwable).getErrorCode();
        // handle based on code
    }
});

// Sync — catch the exception
try {
    hello.say(msg, When.Now);
} catch (RemoteOperationException e) {
    int code = e.getErrorCode();
    // handle based on code
}
```

:::note
Each module generates its own `RemoteOperationException` in its own package. When handling errors from multiple modules, use `RuntimeException` as a common base type.
:::

## Gradle Project Structure

The `api` feature generates a pure Java library module (`java-library` plugin) with no Android dependencies. This means the API types can be used in any Java project, not just Android.

The module includes:
- `build.gradle` with `jackson-annotations` dependency
- Standard Gradle source layout under `src/main/java/`

The parent composite build also generates a `gradle/libs.versions.toml` version catalog for dependency management.
