# Space Invaders

This game is a remake of the classic arcade game Space Invaders, complete with animations and sound effects. This application was written in Java using the libGDX framework. This game can be played in Mac OS by downloading and running the `Space Invaders.jar` file.

Considered one of the most influential video games of all time, Space Invaders helped expand the video game industry from a novelty to a global industry, and ushered in the golden age of arcade video games. It was the inspiration for numerous video games and game designers across different genres, and has been ported and re-released in various forms.

## How to Play:

Space Invaders consists of a fixed shooter in which the player controls a laser cannon by moving it horizontally across the bottom of the screen and firing at descending aliens. The aim is to defeat five rows of eleven aliens that move horizontally back and forth across the screen as they advance toward the bottom of the screen. The player's laser cannon is partially protected by several stationary defense bunkers that are gradually destroyed from the top and bottom by blasts from either the aliens or the player. The player defeats an alien and earns points by shooting it with the laser cannon. As more aliens are defeated, the aliens' movement and the game's music both speed up. Defeating all the aliens on-screen brings another wave that is more difficult, a loop which can continue endlessly. A special "mystery ship" will occasionally move across the top of the screen and award bonus points if destroyed. The aliens attempt to destroy the player's cannon by firing at it while they approach the bottom of the screen. If they reach the bottom, the alien invasion is declared successful and the game ends tragically; otherwise, it ends generally if the player's last cannon is destroyed by the enemy's projectiles.

## Development:

The structure of the game features a lot of object oriented programming in order to implement an efficient design. This game is developed using varying game states which constantly update in real time. The three game states consist of the menu state, play state, and game over state which are all extensions of the state class. The menu state is initialy loaded, and waits on user input to then initialize the play state. When the play state is loaded the game is started, and the player can then play. The play state does all the work to make this game function. It contains various objects for all of the sprites within the game, and performs a lot of the general logic regarding sprite interactions. After the player loses, the game over state is loaded which displays their score and gives them an option to play again.

<b>Sprite Objects:</b>
* Player - The cannon controlled by the player.
* Enemy - Individual enemy controlled by the computer.
* EnemyGroup - Group of 5 enemies controlled by the computer.
* Block - Individual bunker block protecting the player.
* BlockGroup - Group of 14 bunker blocks forming a complete bunker.
* Laser - Laser fired by either player or enemy.
* Ship - UFO flying above enemies.
* Lives - Player's lives count.
* Score - Player's score.

## Gameplay:

<p align="center">
    A demo of gameplay can be viewed here: https://youtu.be/759O_8KQbbY
</p>

<p align="center">
  <img src="https://i.imgur.com/q7lmhSU.jpg" width="384" height="384">
  <img src="https://i.imgur.com/TNpaDxA.jpg" width="384" height="384">
</p>

<p align="center">
  <img src="https://i.imgur.com/o9DjaaD.jpg" width="384" height="384">
  <img src="https://i.imgur.com/9428r2F.jpg" width="384" height="384">
</p>
