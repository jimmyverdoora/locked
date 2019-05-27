# Locked
Awesome table top game by M Roselli and T Fakhredine - terminal interface for multiplayer games

## How to play
You have to complete a tris either in horizontal or vertical direction with your pieces. You can move one of your pieces by one space horizontally or vertically into an empty space or against an opponent piece; in this case their piece is moved as well in the same direction. You can't push more than one piece and you can't push your own pieces.

## How to launch the game
Using the jar file. Type
```bash
java -jar Locked.jar server
```
to be play as the server. Once the server is launched, the other player has to run
```bash
java -jar Locked.jar client [server IP]
```
and the game will start. The server player has blue pieces while the client has reds.

## Comands
To move a piece just type the piece number followed by the direction (u, d, l, r). For instance:
```bash
2u
```
or
```bash
1r
```
