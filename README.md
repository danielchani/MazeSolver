# MazeRunner

A modular maze-generation and -solving application with both console and JavaFX client-server interfaces, demonstrating design patterns, TCP/JSON communication, and pluggable algorithms.

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Prerequisites](#prerequisites)
5. [Installation and Build](#installation-and-build)
6. [Usage](#usage)
   - [Console Mode](#console-mode)
   - [Single-Player UI](#single-player-ui)
   - [Multiplayer UI](#multiplayer-ui)
7. [Testing](#testing)
8. [Project Structure](#project-structure)
9. [Contributing](#contributing)
10. [License](#license)

## Overview

MazeRunner is a client-server maze application that supports:

- **Algorithm Module**: A reusable JAR implementing maze-generation and maze-solving algorithms (BFS and DFS).
- **Server**: A TCP server that accepts JSON-encoded requests, routes them via controllers, and persists maze data.
- **Client**: A JavaFX-based UI offering both single-player and multiplayer modes, with real-time character movement and a countdown timer.

## Features

- **Maze Generation**: Create mazes of configurable size.
- **Maze Solving**: Solve mazes using BFS or DFS, with visualization.
- **Single-Player Mode**: Navigate the maze using arrow keys, view solution path, and restart.
- **Multiplayer Mode**: Two players join the same maze (Player 1: arrows, Player 2: WASD), 60-second timer, synchronized movements.
- **Design Patterns**: Strategy for algorithms, Factory for controller dispatch, Repository/DAO for persistence.
- **JSON over TCP**: Lightweight protocol for client-server communication.

## Architecture

```text
Client (JavaFX UI)
  ↕ JSON/TCP
ServerDriver (port 34567)
  → Server
    → HandlerRequest
      → ControllerFactory
        ├─ MazeController (generate / solve / reset)
        └─ GameController (join / move)
          ↕
     IDao<MazeDataModel> / MyDMFileImpl
          ↕
     DataSource file (persistent storage)
```

## Prerequisites

- Java 17 or later
- JavaFX 24 SDK
- Maven or Gradle (optional, see build instructions)
- Git client

## Installation and Build

1. **Clone the repository**

   ```bash
   git clone https://github.com/<your-username>/MazeRunner.git
   cd MazeRunner
   ```

2. **Import into IDE**

   - Open the project in IntelliJ IDEA or Eclipse.
   - Configure the JavaFX SDK (add the `lib` folder to your module’s VM options).

3. **Build the Algorithm Module**

   - Navigate to the `AlgoModules` directory.
   - Run your build tool (`mvn package` or `gradle build`) to produce `AlgoModules.jar`.

4. **Compile Server and Client**

   - Ensure `AlgoModules.jar` and `gson-2.8.9.jar` are added to the `libs` folder.
   - Build the project in your IDE or run the provided Gradle/Maven tasks.

## Usage

### Console Mode

1. Run the text-based driver:
   ```bash
   java -cp server/target/server.jar;libs/AlgoModules.jar;libs/gson-2.8.9.jar \
     main.java.mazegame.GameMain
   ```
2. Observe maze generation and solution printed to the console.

### Single-Player UI

1. Launch the JavaFX application with VM options:
   ```
   --module-path /path/to/javafx-sdk-24/lib \
   --add-modules javafx.controls,javafx.fxml
   ```
2. Select **Game → Single-player** from the menu.
3. Enter a maze ID and click **Generate**.
4. Use arrow keys to navigate your green player dot.
5. Click **Solve** to display the BFS/DFS path in red.
6. Click **Restart** to clear and start over.

### Multiplayer UI

1. Launch two instances of the JavaFX application (same VM options).
2. In each, select **Game → Multiplayer**.
3. Enter the same maze ID and click **Join**.
4. Player 1 moves with arrow keys; Player 2 moves with W/A/S/D.
5. A 60-second timer counts down for the round.
6. Click **Restart** to reset both clients.

## Testing

- **Algorithm Module**
  - JUnit tests in `AlgoModules/src/test/java` verify BFS and DFS correctness.
- **Service Layer**
  - Optional tests in `server/src/test/java` for `MazeService` and `GameService` CRUD operations.

Run tests with Maven or Gradle:

```bash
mvn test
# or
gradle test
```

## Project Structure

```
root/
├─ AlgoModules/
│  ├─ src/main/java      # IAlgo, BFSMazeSolver, DFSMazeSolver
│  └─ src/test/java      # JUnit tests
├─ server/
│  ├─ src/main/java
│  │  ├─ network/        # ServerDriver, Server, HandlerRequest
│  │  ├─ controller/     # MazeController, GameController
│  │  ├─ repository/     # IDao, MyDMFileImpl
│  │  └─ service/        # MazeService, GameService
│  └─ DataSource.txt     # Persistent storage
├─ maze-client/
│  ├─ src/main/java
│  │  ├─ client/         # Client.java (JSON/TCP wrapper)
│  │  └─ gui/            # CombinedApp.java, MazeCanvas.java
├─ libs/                 # External jars (gson, javafx modules)
└─ README.md
```

## Contributing

Contributions are welcome. To propose changes:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes and push (`git push origin feature/YourFeature`).
4. Open a pull request describing your changes.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

