# ApiGear Java Template

This is a template for creating a Java project using ApiGear.

## Usage

Clone the repository and install the submodules. 

```bash
git clone https://github.com/apigear-io/template-java.git
cd template-java
git submodule update --init --recursive
```

Clone the repository and run `task install` to install the ApiGear CLI. You need to have Go installed.

We use [Task](https://taskfile.dev/) to manage the project. Install it and run `task` to see all available commands.

```bash
task install
```

- Run `task run` to generate the code.
- Run `task test` to validate the generated code against the goldenmaster.


See the [ApiGear documentation](https://apigear.io/docs/intro) for more information.

## Supported Features

This is a very basic template. It supports only the core features of ApiGear.

## Features

- API:
    - Simple API using interfaces, enums, POJOs, etc.

## Planned Features

- scaffold: maven support
- imports: support for importing other APIs 
- extends: support for extending other APIs
- external: support for external APIs
- test: support for unit testing
- olink: object link support (req. objectlink core java library) using websockets
