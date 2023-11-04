# CarcassonneGame
Project of the subject of Java Programming 2 of the Software Engineering degree at Algebra University College (Zagreb). The project consist in a  replica of the popular Carcassonne game. It is written in javafx
This is the explanation of the game: https://wikicarpedia.com/car/Base_game
## Version 1
The version 1 consist in creating the basics of game logic. This version has simplified this logic. In the original game you are able to put followers in the farms but not in this version. Al the other things you can do in the fisical game are enabled in this version.
I decided to implement it with pixels due to issues with the 'put follower' user function.
Althought there are buttons for saving or loading the game, this functionalities aren't implemneted yet in this version.
### Interface
#### Login
In the V1 the max users are 2, but it is just a temporaly solution becaouse one of the verison 3 will start implement networking.
<img width="407" alt="CarcassoneLoginV1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/cc5c9321-7326-4ed9-8cb4-0a4bf72b7d19">
#### Game
##### Game Information
The user is able to see:
 - Whose turn is it
 - The number of remaining tiles in the "bag" and diferent types
In the side bar user will see the next tile and the puntuation and remaining followers of each user
<img width="446" alt="CarcassoneGame1V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/3c9a2a4a-0565-4f3c-9b14-f7b9907bcfcb">
##### Actions
There are two ways of making actions in this version, one is throw the buttons in the side panel and the other is trow the menu bar. User can:
  - Put Tile in the selected position
  - Change the tile (what will be penalized with 5 points)
  - Remove follower of the next tile
  - Rotate the next tile
For selecting a position in the gameboard the user just nead to click on it in the gameboard view. The selected position has the backgroun color of the player.
For puting a follower the user can (before puting tile) select the position in the next tile view.
![CarcassoneGame2V1](https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/aac65872-17cc-4530-8b36-115818ea226c)
##### Game
The user is able to create a new game and to finish the current game.
<img width="447" alt="CarcassoneGame3V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/82919644-69b8-43cf-a283-c21c3fddd88a">
