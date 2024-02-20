# Game of 3

### Requirements
- Java 21
- Docker

### Design Considerations
- Only a single game can be played at a time.
- If new game is started, previous game will be discarded.
- REST Operations provided:
  - Get game: provides the latest state of current game
  - Create game: creates a new game
  - Join game: Allows the 2nd player to join game
  - Move: Allows player to perform move by supplying the input
- Users are identified by UUIDs created by application upon calling:
  - Create game
  - Join game
- Event triggers:
  - When required number of players join the game, event sent to notify the player for their turn.
  - When a user performs their turn, event sent to notify next player to take their turn
- Notifications are being printed to console at the moment.
- Notifications are printed to System.err stream to display it in RED color  
- It's not attempted to achieve high coverage of test case at the moment but the idea is to show different type of tests implemented to test different layers.
- At the moment, no logging is configured.

### Steps to run(`The steps have been tried on windows powershell and may require changes if executed on unix`)
- Build application using command
  `./mvnw clean package`
- Go to target directory by running command `cd target`
- run `java --enable-preview -jar game-of-three-0.0.1-SNAPSHOT.jar`. This should start the application

### Steps to build docker image
- run `docker build -t game-of-three:1.0 .` from within the project root directory

P.S. - Working on windows system so cannot test any build/shell script, hence provided individual steps


### REST API
NOTE: In case the application is running on a different host and port, replace `localhost:8080` with correct `<host>:<port>`
Swagger: `localhost:8080/swagger-ui/index.html` (only accessible once the project is running)

### Create Game
#### Request
`POST /api/v1/games`

    curl --location 'localhost:8080/api/v1/games' --header 'Content-Type: application/json' --data '{"initNumber": 10, "player": {"inputType": "MANUAL"}}'

#### Response
    {
        "id": "7d9adaed-ebcd-429c-a0b3-029bb05db6b0",
        "initNumber": 10,
        "currentNumber": 10,
        "players": [
            {
                "id": "eae1337d-75bd-4c3f-ba32-44f72594f19d",
                "inputType": "MANUAL"
            }
        ],
        "moves": [],
        "inProgress": false,
        "finished": false,
        "playersCount": 1
    }

### Get Active Game
#### Request
`GET /api/v1/games`

    curl --location 'localhost:8080/api/v1/games'

#### Response
    {
        "id": "7d9adaed-ebcd-429c-a0b3-029bb05db6b0",
        "initNumber": 10,
        "currentNumber": 3,
        "players": [
            {
                "id": "eae1337d-75bd-4c3f-ba32-44f72594f19d",
                "inputType": "MANUAL"
            },
            {
                "id": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
                "inputType": "MANUAL"
            }
        ],
        "moves": [
            {
                "performedByPlayerId": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
                "addedValue": -1,
                "resultingNumber": 3
            }
        ],
        "inProgress": false,
        "finished": false,
        "playersCount": 2
    }

### Join game
#### Request
`POST /api/v1/games/{game-id}/join`

    curl --location 'localhost:8080/api/v1/games/7d9adaed-ebcd-429c-a0b3-029bb05db6b0/join' --header 'Content-Type: application/json' --data '{"player": {"inputType": "MANUAL"}}'

#### Response
    {
        "activeGame": {
            "id": "7d9adaed-ebcd-429c-a0b3-029bb05db6b0",
            "initNumber": 10,
            "currentNumber": 10,
            "players": [
                {
                    "id": "eae1337d-75bd-4c3f-ba32-44f72594f19d",
                    "inputType": "MANUAL"
                },
                {
                    "id": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
                    "inputType": "MANUAL"
                }
            ],
            "moves": [],
            "inProgress": true,
            "finished": false,
            "playersCount": 2
        },
        "joinedPlayer": {
            "id": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
            "inputType": "MANUAL"
        }
    }

### Make move
#### Request
`POST /api/v1/games/{game-id}/{player-id}/move`

    curl --location 'localhost:8080/api/v1/games/7d9adaed-ebcd-429c-a0b3-029bb05db6b0/6e452d52-1b9a-4527-a4ec-63d2e140d4ef/move' --data '{"value": "MINUS_ONE"}'

#### Response
    {
        "activeGame": {
            "id": "7d9adaed-ebcd-429c-a0b3-029bb05db6b0",
            "initNumber": 10,
            "currentNumber": 10,
            "players": [
                {
                    "id": "eae1337d-75bd-4c3f-ba32-44f72594f19d",
                    "inputType": "MANUAL"
                },
                {
                    "id": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
                    "inputType": "MANUAL"
                }
            ],
            "moves": [
                {
                    "performedByPlayerId": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
                    "addedValue": -1,
                    "resultingNumber": 3
                }
            ],
            "inProgress": true,
            "finished": false,
            "playersCount": 2
        },
        "joinedPlayer": {
            "id": "6e452d52-1b9a-4527-a4ec-63d2e140d4ef",
            "inputType": "MANUAL"
        }
    }




