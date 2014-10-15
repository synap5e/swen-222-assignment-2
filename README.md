SWEN-222 Assignment 2
=====================
Git Repository for the SWEN-222 Group Project.

Game Description
-----
A multi-player, first person adventure game set on a spaceship. The plot involves the spaceship's AI going rouge, requiring the player to try to shut it down. 

Roles
------
1. Application Window. Covers construction of the application windows, menus and user input. 
 - Assigned to: Matt
2. Rendering Window. Covers rendering of the game view display. 
 - Assigned to: Simon
3. Game State + Logic. Covers the internal structures needed to represent current state of the game, and the rules used in determining what actions are and are not allowed at a particular state of the game.
 - Assigned to: Maria
4. Client-Server Architecture. Covers all setup and communication between the server and all clients.
 - Assigned to: James
5. Data storage. Covers loading and saving of game world files in an Json format.
 - Assigned to: Shweta

How to run
------
The working directory must contain the folders/files:
assets/
lib/
default_world.json
space-linux.jar

After setting this up run:
java -jar space-linux.jar

How to setup game
------
A single-player game will create both a server and a client, dropping you straight into a game.
The save file defines which game state will be loaded - a non-existent file causes the default new-game to be loaded.

A multi-player game asks for a server address to connect to as the server.

Both game modes have the optional field of a player id. This is used for loading a previous character from the game state.
Your current player id can be found in the in-game menu.

How to play
------
The aim of the game is to open all the various doors and destroy the turret.
Please check out the key bindings before playing to see the various available actions.
Hint, containers held by the player must be dropped before their contents can be rifled.
Objects are dropped by the aim of the player - and items must be dropped on the floor (so aim just in front of your feet)!

Note: completing the game REQUIRES multiple players.

