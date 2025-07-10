# MazeSolver
Maze Generator and BFS/DFS implementations exported to JAR for external applications
MazeRunner
A modular maze-generation and -solving application with both console and JavaFX client-server interfaces, demonstrating design patterns, TCP/JSON communication, and pluggable algorithms.

Table of Contents
Overview

Features

Architecture

Prerequisites

Installation and Build

Usage

Console Mode

Single-Player UI

Multiplayer UI

Testing

Project Structure

Contributing

License

Overview
MazeRunner is a client-server maze application that supports:

Algorithm Module: A reusable JAR implementing maze-generation and maze-solving algorithms (BFS and DFS).

Server: A TCP server that accepts JSON-encoded requests, routes them via controllers, and persists maze data.

Client: A JavaFX-based UI offering both single-player and multiplayer modes, with real-time character movement and a countdown timer.

Features
Maze Generation: Create mazes of configurable size.

Maze Solving: Solve mazes using BFS or DFS, with visualization.

Single-Player Mode: Navigate the maze using arrow keys, view solution path, and restart.

Multiplayer Mode: Two players join the same maze (Player 1: arrows, Player 2: WASD), 60-second timer, synchronized movements.

Design Patterns: Strategy for algorithms, Factory for controller dispatch, Repository/DAO for persistence.

JSON over TCP: Lightweight protocol for client-server communication.

Architecture
text
Copy
Edit
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
Client serializes user actions into JSON requests.

ServerDriver listens for connections and dispatches requests.

HandlerRequest parses JSON and invokes the appropriate controller via ControllerFactory.

Controllers use the DAO to load/save the MazeDataModel from a flat file.

Responses are serialized back to the client for rendering.

Prerequisites
Java 17 or later

JavaFX 24 SDK

Maven or Gradle (optional, see build instructions)

Git client

Installation and Build
Clone the repository

bash
Copy
Edit
git clone https://github.com/<your-username>/MazeRunner.git
cd MazeRunner
Import into IDE

Open the project in IntelliJ IDEA or Eclipse.

Configure the JavaFX SDK (add the lib folder to your module’s VM options).

Build the Algorithm Module

Navigate to the AlgoModules directory.

Run your build tool (mvn package or gradle build) to produce AlgoModules.jar.

Compile Server and Client

Ensure AlgoModules.jar and gson-2.8.9.jar are added to the libs folder.

Build the project in your IDE or run the provided Gradle/Maven tasks.

Usage
Console Mode
Run the text-based driver:

bash
Copy
Edit
java -cp server/target/server.jar;libs/AlgoModules.jar;libs/gson-2.8.9.jar \
  main.java.mazegame.GameMain
Observe maze generation and solution printed to the console.

Single-Player UI
Launch the JavaFX application with VM options:

css
Copy
Edit
--module-path /path/to/javafx-sdk-24/lib \
--add-modules javafx.controls,javafx.fxml
Select Game → Single-player from the menu.

Enter a maze ID and click Generate.

Use arrow keys to navigate your green player dot.

Click Solve to display the BFS/DFS path in red.

Click Restart to clear and start over.

Multiplayer UI
Launch two instances of the JavaFX application (same VM options).

In each, select Game → Multiplayer.

Enter the same maze ID and click Join.

Player 1 moves with arrow keys; Player 2 moves with W/A/S/D.

A 60-second timer counts down for the round.

Click Restart to reset both clients.

Testing
Algorithm Module

JUnit tests in AlgoModules/src/test/java verify BFS and DFS correctness.

Service Layer

Optional tests in server/src/test/java for MazeService and GameService CRUD operations.

Run tests with Maven or Gradle:

bash
Copy
Edit
mvn test
# or
gradle test
Project Structure
pgsql
Copy
Edit
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
Contributing
Contributions are welcome. To propose changes:

Fork the repository.

Create a feature branch (git checkout -b feature/YourFeature).

Commit your changes and push (git push origin feature/YourFeature).

Open a pull request describing your changes.

License
This project is licensed under the MIT License. See LICENSE for details.
