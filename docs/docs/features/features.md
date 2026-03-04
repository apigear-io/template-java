---
sidebar_position: 2
sidebar_label: "Features"
title: "Java Template Features Overview"
description: "Overview of ApiGear Java template features: API generation with interfaces, enums, structs, and abstract base classes."
keywords: [java features, api generation, apigear]
---

import CodeBlock from '@theme/CodeBlock';
import helloWorldModuleComponent from '!!raw-loader!./data/helloworld.module.yaml';

# Features

This guide explains how to use the generated code, what features are available, and their benefits.

:::info
A feature is a part of the template that generates a specific aspect of the code. For example, the `api` feature generates the core interfaces and data types.
:::

## Get started

This template generates Java types from your API definitions. The generated code includes interfaces, abstract base classes, enums, and structs ready to use in any Java project.

:::note
Basic Java knowledge is required. Familiarity with interfaces, abstract classes, and event listener patterns will help you get the most out of the generated code.
:::

### Code generation

Follow the documentation for [code generation](/docs/guide/quick-start) in general and [CLI](/docs/cli/generate) or the [Studio](/docs/studio/intro) tools.
Or try the [quick start guide](../quickstart/index.md) first, which shows how to prepare an API and generate code from it.

:::tip
For questions regarding this template please go to our [discussions page](https://github.com/orgs/apigear-io/discussions). For feature requests or bug reports please use the [issue tracker](https://github.com/apigear-io/template-java/issues).
:::

### Example API

The following code snippet contains the _API_ definition which is used throughout this guide to demonstrate the generated code and its usage.

<details>
    <summary>Hello World API (click to expand)</summary>
    <CodeBlock language="yaml" showLineNumbers>{helloWorldModuleComponent}</CodeBlock>
</details>

## Features

### Core Features

Core features generate Java types from your API definition:

- [api](#generated-code-structure) - generates interfaces, event listeners, abstract base classes, enums, and structs
- `stubs` (planned) - adds basic stubs for the `api`, providing classes that can be instantiated with default behavior
- `test` (planned) - generates unit test stubs for the API
- `demo` (planned) - generates a demo application showcasing API usage

Each feature can be selected using the solution file or via the command line tool.

:::note
_Features are case sensitive, make sure to always **use lower-case.**_
:::

:::tip
The _meta_ feature `all` enables all specified features of the template. If you want to see the full extent of the generated code, `all` is the easiest solution.
Please note, `all` is part of the code generator and not explicitly used within templates.
:::

## Generated code structure

For the Hello World API, the `api` feature generates a single Java source file containing all types for the module:

- **`When`** enum — maps to the `When` enum defined in the API
- **`Message`** class — maps to the `Message` struct, fields are annotated with [Jackson](https://github.com/FasterXML/jackson) `@JsonProperty` for JSON serialization
- **`IHello`** interface — the `Hello` interface with property getters/setters and operations
- **`IHelloEventListener`** event listener — callback interface for property changes and signals
- **`AbstractHello`** abstract class — base implementation managing listeners and property change notifications

:::note
The generated structs use [Jackson](https://github.com/FasterXML/jackson) annotations (`@JsonProperty`) for JSON serialization. If you use JSON serialization in your project, add the Jackson dependency:
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```
:::

## Folder structure

The following diagram shows the folder structure generated for the `api` feature.

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

:::note
The module name `io.world` is combined with the feature name to form the package directory `io.world.api`. The source file name `IoWorld.java` is derived from the module name in PascalCase.
:::
