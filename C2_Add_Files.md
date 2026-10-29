# Stage 2 Notes

For Stage 2, I added VIP and Critic customers. I kept the original Stage 1 files
unchanged and added a separate entry point, `Stage2Main`, so I can run the new
version without replacing the Stage 1 game.

The game still has six customers arriving on turns 1, 4, 7, 10, 13 and 16.
Customer 2 is a VIP, customer 5 is a Critic, and the other four are Regular
customers. VIPs lose 5 satisfaction points per waiting turn and get 10% off
their bill. Critics lose 12 points per waiting turn and always choose Pasta.
Regular customers and VIPs use the existing rule based on their customer ID to
choose food.

I chose Pasta as the Critic's preference because it is already on the menu.
The Critic still goes through the same ordering and service steps as the other
customers. The VIP and Critic classes provide their own behavior through
`Stage2CustomerBehavior`.

## New Stage 2 files

- `src/Stage2CustomerBehavior.java`
- `src/VIPCustomer.java`
- `src/CriticCustomer.java`
- `src/Stage2GameController.java`
- `src/Stage2GameCLI.java`
- `src/Stage2Main.java`

I added these files on the Stage 2 branch and left the Stage 1 files, including
`Main.java`, `GameCLI.java` and `GameController.java`, unchanged.

## Running Stage 2

From the project folder, compile and run with JDK 11 or later:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Stage2Main


