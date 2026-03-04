---
sidebar_position: 1
sidebar_label: "Quick-Start"
title: "Quick Start: Generate Java Code from API Definitions"
description: "Step-by-step guide to generate Java interfaces and data types from YAML API definitions using the ApiGear code generator."
keywords: [java quickstart, api code generation, java interface, apigear tutorial]
---
import QuickStartCommon from "@site/docs/_quickstart_common.md"

# Quick-Start

The Quick-Start guide explains how to get from an API to a functional *Java* project in a few steps.
For more general information about first steps with ApiGear, see [First Steps](/docs/guide/quick-start).

The quick start uses only the `api` feature. For all available features, see the [overview](../features/features.md).

<QuickStartCommon />

## 5. Use the generated Java project

:::tip Prerequisites
The easiest way to work with the generated code is [Android Studio](https://developer.android.com/studio), which manages the SDK, Gradle, and JDK for you.

For command-line builds, you need JDK 17 or later (see [Eclipse Adoptium](https://adoptium.net/)), Gradle 8.7+, and the Android SDK with compileSdk 35. The minimum supported Android version is **API 33 (Android 13 / Tiramisu)**.
:::

### Project folder structure

For the code generation we assume that both *ApiGear* files reside in an `apigear` subfolder next to the generated files.
In this case, the folder structure should look similar to this.
```bash
📂hello-world
 ┣ 📂apigear
 ┃ ┣ 📜helloworld.solution.yaml
 ┃ ┗ 📜helloworld.module.yaml
 ┣ 📂java_hello_world
 ┃ ┗ 📂ioWorld
 # highlight-start
 ┃   ┗ 📂ioWorld_api/src/main/java/ioWorld/ioWorld_api
 ┃     ┣ 📜IHello.java
 ┃     ┣ 📜IHelloEventListener.java
 ┃     ┣ 📜AbstractHello.java
 ┃     ┣ 📜Message.java
 ┃     ┗ 📜When.java
 # highlight-end
```

The generated code is a Gradle multi-module project with separate source files per type.

### Create and run an example

The `api` feature generates separate files for each type: the `When` enum, the `Message` struct, the `IHello` interface, the `IHelloEventListener` event listener, and the `AbstractHello` abstract base class. You can implement the interface and use it in your project:

```java
import ioWorld.ioWorld_api.*;

import java.util.concurrent.CompletableFuture;

public class HelloExample {
    // A simple implementation of the Hello interface
    static class MyHello extends AbstractHello {
        private Message last = new Message();

        @Override
        public int say(Message msg, When when) {
            System.out.println("say called: " + msg.content + " (" + when + ")");
            return 42;
        }

        @Override
        public CompletableFuture<Integer> sayAsync(Message msg, When when) {
            return CompletableFuture.supplyAsync(() -> say(msg, when));
        }

        @Override
        public void setLast(Message last) {
            if (!last.equals(this.last)) {
                this.last = last;
                fireLastChanged(last);
            }
        }

        @Override
        public Message getLast() {
            return last;
        }

        @Override
        public boolean _isReady() {
            return true;
        }
    }

    public static void main(String[] args) {
        MyHello hello = new MyHello();

        // Use a struct (all-args or no-arg constructor)
        Message msg = new Message("Hello World");

        // Call an operation
        int result = hello.say(msg, When.Now);
        System.out.println("Result: " + result);

        // Subscribe to property changes and signals
        hello.addEventListener(new IHelloEventListener() {
            @Override
            public void onLastChanged(Message newValue) {
                System.out.println("last changed: " + newValue.content);
            }

            @Override
            public void onJustSaid(Message msg) {
                System.out.println("justSaid signal: " + msg.content);
            }

            @Override
            public void on_readyStatusChanged(boolean isReady) {
                System.out.println("ready: " + isReady);
            }
        });

        // Set a property (triggers onLastChanged)
        hello.setLast(msg);
    }
}
```

:::tip
For more features, check the [features overview](../features/features.md).
:::
