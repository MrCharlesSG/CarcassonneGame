<!-- TOC start (generated with https://github.com/derlin/bitdowntoc) -->

- [CarcassonneGame](#carcassonnegame)
   * [Version 1 (Game Logic)](#version-1-game-logic)
      + [Login](#login)
      + [Game](#game)
         - [Game Information](#game-information)
         - [Actions](#actions)
            * [Game](#game-1)
   * [Version 2 (Serialization & Reflection)](#version-2-serialization-reflection)
      + [Load and Save](#load-and-save)
      + [Documentation](#documentation)
   * [Version 3 (Networking)](#version-3-networking)
      + [Simultaneos Game](#simultaneos-game)
      + [Chat](#chat)
      + [Refactoring](#refactoring)
         - [Controller](#controller)
         - [Game](#game-2)
         - [Utils](#utils)
   * [Version 4 (Threads)](#version-4-threads)
      + [Preparation](#preparation)
      + [Threads](#threads)
   * [Version 5 (XML)](#version-5-xml)
      + [How it Works](#how-it-works)

<!-- TOC end -->

<!-- TOC --><a name="carcassonnegame"></a>
# CarcassonneGame

Project of the subject of Java Programming 2 of the Software Engineering degree at Algebra University College (Zagreb). The project consist in a  replica of the popular Carcassonne game. It is programmed in javafx.
This is the explanation of the game: https://wikicarpedia.com/car/Base_game

<!-- TOC --><a name="version-1-game-logic"></a>
## Version 1 (Game Logic)

The version 1 consist in creating the basics of game logic. This version has simplified this logic. In the original game you are able to put followers in the farms but not in this version. Al the other things you can do in the fisical game are enabled in this version.
I decided to implement it with pixels due to issues with the 'put follower' user function.
Althought there are buttons for saving or loading the game, this functionalities aren't implemneted yet in this version.
<!-- TOC --><a name="login"></a>
### Login
In the V1 the max users are 2, but it is just a temporaly solution becaouse one of the verison 3 will start implement networking.

<img width="407" alt="CarcassoneLoginV1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/cc5c9321-7326-4ed9-8cb4-0a4bf72b7d19">

<!-- TOC --><a name="game"></a>
### Game

<!-- TOC --><a name="game-information"></a>
#### Game Information

The user is able to see:
 - Whose turn is it
 - The number of remaining tiles in the "bag" and diferent types
In the side bar user will see the next tile and the puntuation and remaining followers of each user

<img width="446" alt="CarcassoneGame1V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/3c9a2a4a-0565-4f3c-9b14-f7b9907bcfcb">

<!-- TOC --><a name="actions"></a>
#### Actions

There are two ways of making actions in this version, one is throw the buttons in the side panel and the other is trow the menu bar. User can:
  - Put Tile in the selected position
  - Change the tile (what will be penalized with 5 points)
  - Remove follower of the next tile
  - Rotate the next tile
For selecting a position in the gameboard the user just nead to click on it in the gameboard view. The selected position has the backgroun color of the player.
For puting a follower the user can (before puting tile) select the position in the next tile view.

![CarcassoneGame2V1-2](https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/24faba13-548a-4013-a4c5-69a527503a16)

<!-- TOC --><a name="game-1"></a>
##### Game

The user is able to create a new game or to finish the current game.

<img width="447" alt="CarcassoneGame3V1" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/82919644-69b8-43cf-a283-c21c3fddd88a">

<!-- TOC --><a name="version-2-serialization-reflection"></a>
## Version 2 (Serialization & Reflection)

In this version I have fixed some bugs from the previous version. Its been implemented the load and save of the game (serialization) and the auto-generated documentation (reflection)

<!-- TOC --><a name="load-and-save"></a>
### Load and Save

There has been also implemented the save and load, where the user will be able to save the game status and loaded when ever he wants. This functionality is implemented with serialization what will be usefull in next version where networking will be enabled.

<img width="447" alt="CarcassoneGame4V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/cd03a2e8-74ed-4816-a60a-01373ee37d27">
<img width="446" alt="CarcassoneGame3V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/e2cd73ab-2804-432c-94b4-26eb76b7a0b5">
<img width="447" alt="CarcassoneGame2V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/5153c1da-2ec6-446a-98ba-d7b9aedf2fd4">
<img width="449" alt="CarcassoneGame1V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/89caf881-e987-431d-acd4-903e9cb57b28">

<!-- TOC --><a name="documentation"></a>
### Documentation

Through the app, the user can autogenerate the documentation of the project. This documentation is nothing but the package structure and all the fields, methods and contructors of every class (except Main class). The user can also visit this documentation clicking the button read, who will drive him to the browser and the index.html of this documentation.

<img width="445" alt="CarcassoneGame5V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/a9c35946-cef4-4369-b70e-85d99437ba2f">
<img width="645" alt="CarcassoneGame6V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/d269e37a-9003-41a1-8abf-d4d874b02b5f">
<img width="865" alt="CarcassoneGame7V2" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/f6ddae20-f1d9-4926-98c2-e165122b64a5">

<!-- TOC --><a name="version-3-networking"></a>
## Version 3 (Networking)

This is the version that has changed the most from its previous one so far. In this version it's been implemented the networking. Now, users (Client and Server), can play the same game simultaneously thanks to shockets. Also, users will be able to chat between. 
This version also held the refactoring of various classes that were huge.

<!-- TOC --><a name="simultaneos-game"></a>
### Simultaneos Game

I've created two threads so one do as a Server and the other as a Client. The first thread is going to be always the Server, and is going to be the Client the one how will create the game. 
After every `putTileAction` the game is sended to the other thread.
For the player who is not his turn it is disable all the game functionalities. As a client, the funtionality of loading and saving game is disabled. When the Server loads the game is inmediately send to the Client how will loaded.

<img width="947" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/b6750253-8a56-46c9-9891-37eeeeab1c4e">
<img width="899" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/983a3f52-f8bc-427a-8c3e-64eeaead47b2">
<img width="895" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/983e4e20-5913-4036-a21f-02ab52ec5df2">
<img width="898" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/986249c9-e05b-4529-8101-3575a30eb413">

<!-- TOC --><a name="chat"></a>
### Chat

In this chat every of the user will be able to send a message by pressing enter or by clicking the button. The messages contains the time they were created, the user who send it and the text it self.
The Chat has been implemented with the Java Remote Method Invocation (RMI), show I have create the `RemoteChatService` interface and its implementation as the common object. Here it is were is explained https://www.geeksforgeeks.org/remote-method-invocation-in-java/

<img width="772" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/62c627ce-721f-490e-be7a-e55480795f67">
<img width="768" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/67b87b55-0da7-49b3-8346-db278158f6f3">

<!-- TOC --><a name="refactoring"></a>
### Refactoring

During this version I have refactored the `Controller`, the `Game` and the `Tile`. This is the major refactoring I have done for the moment.

<!-- TOC --><a name="controller"></a>
#### Controller

Before this refactoring the `Controller` class was arround 400 hundred lines, now is 250 lines. There is still room for refactoring because the Command pattern can be implemented.

For the `Controller` I've just take of it everything rellated to the views and with the initialization of the `Game`. For that I've created a `ViewsManager` class who its clients does not know what is happening inside it. The clients just ask him to uptate its view and the intance of `Game` it have. The clients instanciate this class by giving him the ScoreBoard (will be explained later), the `GridPane` of the `nextTile` and of the game, the `TextArea` and `TextField` for the `Chat` and a `Label` for telling whose turn.

The `ViewsManager` have and instance of this classes that are actually only visible for him:
  - `ChatView`
  - `GameBoardViewView`
  - `NextTileView`
  - `PlayerTurnView` (the label that if it is player turns or not)
  - `ScoreBoardView`
Every of that classes, but `ChatView`, extends `GameView` that have a static instance of `GameWorld` (the interface of the game), the abstract function `updateView`. So when the controller ask the `ViewsManager` to update, he just iterate over a map of all of the `GameView` extended classes and call the `updateView` function. I have a map becouse I sometimes just update the `NextTileView` or the `GameBoardViewView`.

For disabling the view, the `GameView` has a `static boolean enable` variable, so the function that the buttons of the `NextTileView` or the `GameBoardViewView` use this variable at the beginning for being executed or not.

Now, the initialization of the `Game` is done by the `GameFactory`. This `GameFactory` has the name of the JSON file that has all the Tiles information. I have created various JSONs just for testing purposes, but I still maintain the original.

<!-- TOC --><a name="game-2"></a>
#### Game

As the `Game` class in the previous versions was a monster (arround 500 lines) I have manage to create classes arround the 200 lines and also leaving `Game` with less than 200 lines.
I have created a `GameWorld` interface, which will be used by every of the classes and only the `GameFactory` knows of the existance of the `Game` class (the implementation). For this implementation I have created two subclasses (that are only visible for the game package). This subclasses does not extend `Game` and are:
  - `GameStatus`, that logicaly store all the information related with the game status. It is the serializable class. So when ever `GameWorld` its being serializable, `Game` (that implements `Externalizable`) is going to serialize only this class.
  - `GameOperation`, this class just do all the operations the interface `GameWorld` needs to do such us `closeTiles`.

 This classes are only instanciate in the `Game` class.

 #### Tile

Before refactoring, as the `Game` class, this guy was also a monster. Now is 200 lines, this is how I did it.

I have created an abstract class call `TileManager` which is extended by `TilePathManager` and `TileCityManager`. In this time the client (`TileImpl`) do now the existance of this classes and need to call the right functions if he wants the application to work. 
For example in the case of seen if the user can put a user in the path of the tile (this verification is than by the tile). The client has to call the function `canPutFollower` of its instance of `TilePathManager`, if he calls `TileCityManager` will receive an `IllegalArgumentException`. Same will happen with other more specific functions such as `countPathsForClosingPath`. This managers are not flexible, if client don't do what they demand, they will crash the application.

The abstract class `Tile` was already done in previous versions.

<!-- TOC --><a name="utils"></a>
#### Utils

For supporting all this refactoring, have been created three new Utils classes that conatins every "smart" thing I have done for each type of classes that can be reused. 
The most important Util created is the generic function done in `GridUtils` that standarize the way in which all grid are accessed. 
The utils that have been created for adding the previous `DocumentationUtils` and `ReflectionUtils` are:
  - `GridUtils`
  - `TileUtils`
  - `ViewUtils`


<!-- TOC --><a name="version-4-threads"></a>
## Version 4 (Threads)
For this version we had to create two threads, one was supose to read a file and the other will write. What the file will contain will be all the moves the game has had. 

<!-- TOC --><a name="preparation"></a>
### Preparation
For this version the offline mode was compulsory becouse the threads functionality will only work for that mode. For implementing the offline mode I create I pre-menu where the user was able choose what mode he want to play. Now the user, not matter what mode he play, is able to write his name. 
So now the offline mode looks like this

<img width="298" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/80d70f1e-82b2-4760-9350-2e9902e52e1d">
<img width="295" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/3244ff75-0a0a-45d6-a0bd-4e695adfc600">
<img width="297" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/d988b13a-c874-46c2-aef6-f88458f8a97e">
<img width="449" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/789a3a5b-7fb2-417f-8861-607cce401be3">

The online Mode looks like this

<img width="295" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/c5246acb-024e-44aa-9ae9-4d8f47980d55">
<img width="294" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/5bbaecd1-7216-4008-9b56-87a50e405b49">
<img width="601" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/872cbeb5-d0b0-46bb-93e2-cb1b950e8b84">

<!-- TOC --><a name="threads"></a>
### Threads
For the creation of the threads I have created an abstract class `GameMoveThread` that is extended by the other two threads `LatestMoveThread` and `SaveMoveThread`. The main function of this abstract class is to sincronize each of the the funtions that the two threads will execute. This functions are `public synchronized void saveMove(GameMove gameMove)` and `public synchronized GameMove getTheLastMove()`. The sincronization is basically a flag. When a thread put the flag on, none of the other threads can access to the file. 
For the display of the move I have created, oviously, a `GameMove` class, that store the necessary data of a move. For more separations of concerns I have created a class call `TileDescription`.

<img width="451" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/d6ea500b-da86-47a1-b9d7-d8b16acf6357">

<!-- TOC --><a name="version-5-xml"></a>
## Version 5 (XML)
This is for now (January 2024) the last version of the project. So this is the END.
In this version the user is able to replay the last offline version. In the replay mode the user is able to pause the replay, to play it automatically (every 3 seconds a tile will appear in the screen) and manually (can do next or previous tile). When the last tile is put, the replay will be restarted.
This is how the replay mode looks like.

<img width="300" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/5ef3ba5a-8912-456a-9a71-71df445caecc">
<img width="448" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/53f2b969-1c59-4a5d-ba8e-b7bc495ff7ff">
<img width="448" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/b81065e1-b122-49b0-adbe-547dccff3ecf">
<img width="450" alt="image" src="https://github.com/MrCharlesSG/CarcassonneGame/assets/94635721/444b1aad-1098-4431-8c9e-f5abcadd3bee">

<!-- TOC --><a name="how-it-works"></a>
### How it Works
The game move has four properties:
  - `Player`. Contains the name and the colour of the player that has put the tile.
  - `Position`. The position of the tile in the gameBoard
  - `LocalDateTime`. Only used for the functionality of the previous version.
  - `TileDescription`. Has the "description" of the tile and the position of the follower (if have it)
When parsing to XML the `GameMove` class is encharge of parsing the player (only the name) and the position, and is `TileDescriptions` the one who parses itself. The same will happen when parsing from XML. The `fromXml()` functions are static functions of each clases that return the especific class, and the `toXml()` are non static. Each of the functions receive the XMLElement where they need to be storage or extracted and the document.
For writing in the XML there is a `XMLUtils` that has all the necessary functions.
Every time the user put a tile the function `saveLastMove()` is being call. This function only writes in the XML and call the threads (of the previous version) when is an offline game.
