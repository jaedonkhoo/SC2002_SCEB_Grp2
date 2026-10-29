# Stage 2

Stage 1 still starts from `Main`, and Stage 2 starts from `Stage2Main` using its own controller and command-line interface.

There are six customer arrivals, on turns 1, 4, 7, 10, 13 and 16. Customer 2 is a VIP and customer 5 is a Critic. The other four customers are Regular customers. VIP customers get a 10% discount. The Critic always orders Pasta. Regular and VIP customers choose their food using the existing customer-ID rule.

The Critic's Pasta order uses an item already on the menu. After ordering, VIP and Critic customers go through the same seating, food preparation and serving steps as the other customers. Their food and price behavior is handled through the Stage 2 customer behavior interface.

## Files added for Stage 2

- `src/Stage2CustomerBehavior.java`
- `src/VIPCustomer.java`
- `src/CriticCustomer.java`
- `src/Stage2GameController.java`
- `src/Stage2GameCLI.java`
- `src/Stage2Main.java`

These files belong on the Stage 2 branch. Keep the Stage 1 entry point as `Main`; run Stage 2 from `Stage2Main`.

## How to run Stage 2

In Eclipse, open `Stage2Main.java`, choose **Run As → Java Application**, then follow the prompts in the Console. This is the normal text-based way to play the game.

From a macOS or Linux terminal, run these commands from the project folder. JDK 11 or later is needed:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Stage2Main
```

Stage 1 can still be started with `java -cp out Main` after compiling.

## Manual run results

I played Stage 2 through the normal menu in Eclipse using Java 21. In the victory run, I seated customers, took their orders, prepared the food, served them and let the cashier process payment. The run included Regular, VIP and Critic customers, and the orders included Salad, Burger and Pasta. Pasta took two preparation units. The sixth customer was seated on the last turn but there was not enough time left to finish the order.

That run ended in victory after 18 turns: 5 customers served and paid, no unhappy departures, $67.50 revenue and 74.8 average satisfaction.

I also played a defeat run by entering `0` at each Waiter action prompt. It ended after 18 turns with no customers served or paid, 1 unhappy departure, $0.00 revenue and average satisfaction shown as N/A. The final output is recorded in [TESTING.md](TESTING.md).

