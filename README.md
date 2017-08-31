# ScaLoader
*Loads Scala and other libraries for use with plugins written in Scala*

ScaLoader is a plugin that automatically downloads the Scala libraries,
and injects them into the server's classpath. It also allows other
plugins to register their own required libraries. This is required for
any plugins written in Scala to function.

### Libraries
This plugin downloads the following 2 libraries by default:

- [scala-library-2.12.3.jar](http://mvnrepository.com/artifact/org.scala-lang/scala-library/2.12.3)
- [scala-reflect-2.12.3.jar](http://mvnrepository.com/artifact/org.scala-lang/scala-reflect/2.12.3)

Other libraries can be registered from other plugins using
`LibraryRegistry.registerLibrary()`.

*Example:*

```java
public void onEnable() {
    LibraryRegistry.registerLibrary("Scala Library 2.12.3", new URL("http://central.maven.org/maven2/org/scala-lang/scala-library/2.12.3/scala-library-2.12.3.jar"), this);
}
```

A `HashMap` of all libraries and their filenames is stored in
`LibraryRegistry.libraries`.

### Changelog:
v1.1-2.12.3
- Updated Scala 2.12.2 -> 2.12.3

v1.1-2.12.2
- Added ability to register libraries from other plugins
- Updated Scala 2.11.8 -> 2.12.2

v1.0-2.11.8
- Initial release

### License
This project is licensed under the MIT License. Feel free to use this
plugin in any projects, and make a pull request if you have features to
add.