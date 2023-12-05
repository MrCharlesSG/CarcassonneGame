- [CarcassonneGame](#carcassonnegame)
  - [Version 1 (Game Logic)](#version-1-game-logic)
    - [Login](#login)
    - [Game](#game)
      - [Game Information](#game-information)
      - [Actions](#actions)
        - [Game](#game-1)
  - [Version 2 (Serialization & Reflection)](#version-2-serialization-&-reflection)
    - [Load and Save](#load-and-save)
    - [Documentation](#documentation)

# CarcassonneGame

Project of the subject of Java Programming 2 of the Software Engineering degree at Algebra University College (Zagreb). The project consist in a  replica of the popular Carcassonne game. It is written in javafx
This is the explanation of the game: https://wikicarpedia.com/car/Base_game

## Version 1 (Game Logic)

The version 1 consist in creating the basics of game logic. This version has simplified this logic. In the original game you are able to put followers in the farms but not in this version. Al the other things you can do in the fisical game are enabled in this version.
I decided to implement it with pixels due to issues with the 'put follower' user function.
Althought there are buttons for saving or loading the game, this functionalities aren't implemneted yet in this version.
### Login
In the V1 the max users are 2, but it is just a temporaly solution becaouse one of the verison 3 will start implement networking.

<img width="407" alt="CarcassoneLoginV1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/cc5c9321-7326-4ed9-8cb4-0a4bf72b7d19">

### Game

#### Game Information

The user is able to see:
 - Whose turn is it
 - The number of remaining tiles in the "bag" and diferent types
In the side bar user will see the next tile and the puntuation and remaining followers of each user

<img width="446" alt="CarcassoneGame1V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/3c9a2a4a-0565-4f3c-9b14-f7b9907bcfcb">

#### Actions

There are two ways of making actions in this version, one is throw the buttons in the side panel and the other is trow the menu bar. User can:
  - Put Tile in the selected position
  - Change the tile (what will be penalized with 5 points)
  - Remove follower of the next tile
  - Rotate the next tile
For selecting a position in the gameboard the user just nead to click on it in the gameboard view. The selected position has the backgroun color of the player.
For puting a follower the user can (before puting tile) select the position in the next tile view.

![CarcassoneGame2V1-2](https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/24faba13-548a-4013-a4c5-69a527503a16)

##### Game

The user is able to create a new game or to finish the current game.

<img width="447" alt="CarcassoneGame3V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/82919644-69b8-43cf-a283-c21c3fddd88a">

## Version 2 (Serialization & Reflection)

In this version I have fixed some bugs from the previous version. 

### Load and Save

There has been also implemented the save and load, where the user will be able to save the game status and loaded when ever he wants. This functionality is implemented with serialization what will be usefull in next version where networking will be enabled.

<img width="447" alt="CarcassoneGame4V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/cd03a2e8-74ed-4816-a60a-01373ee37d27">
<img width="446" alt="CarcassoneGame3V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/e2cd73ab-2804-432c-94b4-26eb76b7a0b5">
<img width="447" alt="CarcassoneGame2V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/5153c1da-2ec6-446a-98ba-d7b9aedf2fd4">
<img width="449" alt="CarcassoneGame1V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/89caf881-e987-431d-acd4-903e9cb57b28">

### Documentation

Through the app, the user can autogenerate the documentation of the project. This documentation is nothing but the package structure and all the fields, methods and contructors of every class (except Main class). The user can also visit this documentation clicking the button read, who will drive him to the browser and the index.html of this documentation.

<img width="445" alt="CarcassoneGame5V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/a9c35946-cef4-4369-b70e-85d99437ba2f">
<img width="645" alt="CarcassoneGame6V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/d269e37a-9003-41a1-8abf-d4d874b02b5f">
<img width="865" alt="CarcassoneGame7V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/f6ddae20-f1d9-4926-98c2-e165122b64a5">


