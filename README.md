# Ultimate Tic Tac Toe game engine
This repository contains the engine for the Ultimate Tic Tac Toe game for the Riddles.io platform.

## Requirements

- JVM 1.8

On Ubuntu: `sudo apt install openjdk-8-jre`

## Running

Running is handled by the match-wrapper. This application handles all communication between the engine and bots and saves the results of the match.

The `wrapper-commands.json` file is currently configured for python bots. If you wish to use another language, this file will need editing. This should be pretty self-explanatory. Just update the command field to the command used to run your bot.

To play the game, run the `match-wrapper-1.3.2.jar` with `wrapper-commands.json` as an argument. On linux, you can use the following command:
```
java -jar match-wrapper.jar "$(cat wrapper-commands.json)"
```

Or simply:
```
./run_wrapper.sh
```

*Note: if running on other systems, find how to put the content of wrapper-commands.json as argument when running the match-wrapper.jar*

Have a look at the match-wrapper repo for more details:
[https://github.com/riddlesio/match-wrapper](https://github.com/riddlesio/match-wrapper)
