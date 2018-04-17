# Chat-Java

This chat uses JavaFX as Graphical Engine.

## Builable Java (with Java9 jlink)

### Clean build
`./gradlew clean`

### Build Client
`./gradlew chat:link`

### Build Server
`./gradlew server:link`

### Build Both
`./gradlew linkAll`

### Launch Client
```
cd chat/build/dist/bin/
./chat
```

### Launch Server
```
cd server/build/dist/bin/
./server
```
## Compilation

```
cd chat/src/main/java/chat
javac *.java -Xlint;
```

### Launch server
```
java Main
```

### Launch Client
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
