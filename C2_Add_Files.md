# Stage 2 notes

Stage 2 uses a separate entry point, controller and command-line interface. Run Stage 1 with `Main` and Stage 2 with `Stage2Main`. This keeps both versions available in the same project.

There are six arrivals, on turns 1, 4, 7, 10, 13 and 16. Customer 2 is a VIP and customer 5 is a Critic. The other customers are Regular customers. Regular customers lose 8 satisfaction points for each turn they wait. VIP customers lose 5 points and receive 10% off. The Critic loses 12 points per waiting turn, prefers Pasta and pays full price. Regular and VIP customers use the existing customer-ID food rule.

The Critic's Pasta order is already on the menu and takes two preparation units. After ordering, all customer types use the same seating, preparation and serving flow.

## Stage 2 files

- `src/Stage2CustomerBehavior.java`
- `src/VIPCustomer.java`
- `src/CriticCustomer.java`
- `src/Stage2GameController.java`
- `src/Stage2GameCLI.java`
- `src/Stage2Main.java`

## Running Stage 2

In Eclipse, run `Stage2Main.java` as a Java Application and follow the prompts in the Console. From the project folder on macOS or Linux, compile and run with JDK 11 or later:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Stage2Main
```

Stage 1 can still be started with `java -cp out Main`.

## Test runs

I compiled the project using Eclipse's JDK 21 compiler with `--release 11`. The Stage 2 victory and defeat runs can be repeated with the input files in `tests/inputs`; their commands and results are in [TESTING.md](TESTING.md).
