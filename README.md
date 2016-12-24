# ScaLoader
_Loads the Scala library for use with plugins written in Scala_

ScaLoader is a plugin that automatically downloads the Scala libraries,
and injects them into the server's classpath. This is required for any
plugins written in Scala to function.

### Libraries
Currently, this plugin downloads the following 2 libraries:

- [scala-library-2.11.8.jar](http://mvnrepository.com/artifact/org.scala-lang/scala-library/2.11.8)
- [scala-reflect-2.11.8.jar](http://mvnrepository.com/artifact/org.scala-lang/scala-reflect/2.11.8)

### License
This project is licensed under the MIT License. Feel free to use this
plugin in any projects, and make a pull request if you have features to
add.