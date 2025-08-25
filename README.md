# ApiGear Java Template

This is a template for creating a Java project using ApiGear.
For more details on *ApiGear* please visit [apigear.io](https://apigear.io) or the [*ApiGear* documentation](https://docs.apigear/).

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

This is not fully implemented template. It supports only some of the ApiGear features.

The template offers the following feature switches which can be enabled during code generation:
* `api`: create interface files and an abstract implementation
* `stubs`: create a stub implementation, that handles all the properties, but is missing the business logic.
* `android`: create the adaptation layer for android messenger communication. Produces the client and the service sides for the interfaces.
* `jnibridge`: create the java jni implementation, that is missing the c++ side and build files. See example of using it in the jni feature in unreal template.
* `testclientapp`: create example messenger client application. This is not ready to use example - the logic for generated buttons should be filled (which values to send), the rest is already there. The app serves only first defined interface.
* `testserviceapp`:   create example messenger service application. This is not ready to use example - the logic for generated buttons should be filled (which values to send), the rest is already there. The app serves only first defined interface.

## Planned Features

- github workflow
- demo app
- imports: support for importing other APIs 
- extends: support for extending other APIs
- external: support for external APIs
- test: support for unit testing
- olink: object link support (req. objectlink core java library) using websockets
