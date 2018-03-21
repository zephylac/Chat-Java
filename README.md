# Chat-Java

This chat uses JavaFX as Graphical Engine.



### Compilation

```
javac *.java -Xlint;
```

## Launch server
```
java Main
```

## Launch Client
```
java Client
```

# Available commands

| Commands      | Description                 |
| ------------- |:---------------------------:|
| !color        | Show Available color        |
| !color COLOR  | Change color                |
| !smiley       | Show available images       |
| !help         | Show all available commands |

# Branches
 * textTest : Send directly Text Node over socker. Problem : Text is no serializable
 * SSLTest : Secured connection. Problem : SSL header seems to corrupt Streams
