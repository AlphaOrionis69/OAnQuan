# O An Quan

A Java implementation of the traditional Vietnamese board game "Ô ăn quan". This repository contains the project's source code (primarily Java) and supporting assets.

## Overview

Ô ăn quan is a two-player mancala-like game played on a board with small pits and two larger "quan" pits. Players take turns distributing stones and capturing based on the game's rules. This repository implements the core game logic in Java and can be used as a starting point for a command-line or graphical user interface.

## Features

- Core game rules and logic implemented in Java
- Clear project structure for extending to CLI or GUI
- Unit-testable game components (if tests are included)

## Requirements

- Java 8 or higher
- (Optional) Maven or Gradle if build configuration files are present

## Build & Run

General instructions — adapt to this repository's structure:

- From an IDE: Import the project as a Java project and run the main class.
- From the command line (simple):
  1. Compile: javac -d out $(find src -name "*.java")
  2. Run: java -cp out <your.main.Class>

If the repository includes a Maven or Gradle build, use the usual commands:

- Maven: mvn package
- Gradle (wrapper): ./gradlew run

If you'd like, tell me which file contains the main method (or the build tool you use) and I can update these instructions with exact commands.

## Contributing

Contributions are welcome. Please:

1. Fork the repository
2. Create a feature branch
3. Open a pull request with a clear description of changes

If you'd like a contributor guide or issue templates, I can add them.

## License

No license file is included in the repository. If you want a license, let me know which one (MIT, Apache-2.0, GPL-3.0, etc.) and I will add a LICENSE file.

---

This README was updated to provide a clearer description of the project. If you want a more detailed README (examples, screenshots, exact build/run steps), tell me where the entry point is or what build tool you use and I'll update it accordingly.
