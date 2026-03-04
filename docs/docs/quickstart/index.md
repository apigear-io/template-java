---
sidebar_position: 1
sidebar_label: "Quick Start"
title: "Quick Start: Generate Java Code from API Definitions"
description: "Step-by-step guide to generate Java interfaces and data types from YAML API definitions using the ApiGear code generator."
keywords: [java quickstart, api code generation, java interface, apigear tutorial]
---
import QuickStartCommon from "@site/docs/_quickstart_common.md"

# Quick-Start

The Quick-Start guide explains how to, in a few steps, get from an API to a functional *Java* project.
For more general information about first steps with ApiGear, see [First Steps](/docs/guide/quick-start).

The quick start uses only the `api` feature. For all available features, see the [overview](features/features.md).

<QuickStartCommon />

## 5. Use the generated Java project

:::tip Prerequisites
This guide assumes you have JDK 11 or later installed. See [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [Eclipse Adoptium](https://adoptium.net/) for downloads.
:::

### Project folder structure

For the code generation we assume that both *ApiGear* files reside in an `apigear` subfolder next to the generated files.
In this case the folder structure should look similar to this.
```bash
📂hello-world
 ┣ 📂apigear
 ┃ ┣ 📜helloworld.solution.yaml
 ┃ ┗ 📜helloworld.module.yaml
 ┣ 📂java_hello_world
 # highlight-next-line
 ┃ ┗ 📂io.world.api
 ┃   ┗ 📜IoWorld.java
```

The generated package follows the pattern `<module>.api`, here `io.world.api` as the module name (defined in line 2 of `helloworld.module.yaml`).
The `api` feature generates interfaces, event listeners, abstract base classes, enums, and structs.

### Create and run an example

The generated `IoWorld.java` contains all types for the module: the `When` enum, the `Message` struct, the `IHello` interface, the `IHelloEventListener` event listener, and the `AbstractHello` abstract base class. You can implement the interface and use it in your project:

```java
import io.world.api.IoWorld.*;

public class HelloExample {
    // A simple implementation of the Hello interface
    static class MyHello extends AbstractHello {
        private Message last;

        @Override
        public int say(Message msg, When when) {
            System.out.println("say called: " + msg.content + " (" + when + ")");
            return 42;
        }

        @Override
        public void setLast(Message last) {
            Message oldValue = this.last;
            this.last = last;
            fireLastChanged(oldValue, last);
        }

        @Override
        public Message getLast() {
            return last;
        }
    }

    public static void main(String[] args) {
        MyHello hello = new MyHello();

        // Use a struct
        Message msg = new Message("Hello World");

        // Call an operation
        int result = hello.say(msg, When.Now);
        System.out.println("Result: " + result);

        // Subscribe to property changes and signals
        hello.addEventListener(new IHelloEventListener() {
            @Override
            public void onLastChanged(Message oldValue, Message newValue) {
                System.out.println("last changed: " + newValue.content);
            }

            @Override
            public void onJustSaid(Message msg) {
                System.out.println("justSaid signal: " + msg.content);
            }
        });

        // Set a property (triggers onLastChanged)
        hello.setLast(msg);
    }
}
```

:::tip
For more features, check the [features overview](features/features.md).
:::
