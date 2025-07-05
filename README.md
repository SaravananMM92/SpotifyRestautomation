# 🎧 Spotify REST API Automation Framework

A TestNG-based automation framework for testing Spotify's Playlist and Track APIs using **RestAssured**.  
Create, verify, and manage playlists programmatically with robust logging and validation.

---

## 🔍 Table of Contents

- [Features](#features)  
- [Quickstart](#quickstart)  
  - [Prerequisites](#prerequisites)  
  - [Clone & Install](#clone--install)  
  - [Configure Authentication](#configure-authentication)  
- [Project Structure](#project-structure)  
- [Running Tests](#running-tests)  
- [Test Data & Utilities](#test-data--utilities)  
- [Reports & Logging](#reports--logging)  


---

## 🚀 Features

- Create Spotify playlists (**POST**)
- Search and retrieve track URIs (**GET**)
- Add tracks to playlists using URI lists
- Extract and validate important response fields like `snapshot_id`
- Log large JSON responses to external files
- Clean test environment before each suite
- Data-driven design – uses external JSON payloads
- Customizable `@BeforeSuite` cleanup and reusable utilities

---

## 🛠 Quickstart

### Prerequisites

- Java 11+
- Maven 3.6+
- A valid **Spotify OAuth token** with:
  - `playlist-modify-public`, `playlist-modify-private`
  - `playlist-read-private`, `playlist-read-collaborative`

### Clone & Install

```bash
git clone https://github.com/SaravananMM92/SpotifyRestautomation.git
cd SpotifyRestautomation
mvn clean install
```

#### Configure Authentication
- use 'token_store.json' to provide the required tokens.
- Also this project has tests that'll get OAuth code via browser login and redirecturi code using selenium framework.

### Project Structure
src/
├── main/
│
└── test/
    ├── java/
    │   └── tests/            # TestNG test classes
    |   ├── api/              # Endpoint wrappers (search, playlist, items)
    |   └── utils/            # JSON reader/writer, cleanup utilities
    └── resources/
        ├── payloads/         # Input JSON payloads
        └── output/           # Response dumps (cleared before tests)


### Running Ttest

```bash
mvn test
```

#### Test Data and Utilities

- src/test/resources/payloads/*.json: Input payloads for creating playlists and tracks

- Utilities:
JSON reader (ReadJsonPayload) – map and list support
JSON writer (JsonFileCleaner, JsonWriterUtil)
Test file cleanup (@BeforeSuite method)


### Report
- Console logs shortened for readability
- Full API responses dumped to JSON files in src/test/resources/output/
- Run setup cleans these files automatically
- Assertions include status codes, body fields, and snapshot_id
