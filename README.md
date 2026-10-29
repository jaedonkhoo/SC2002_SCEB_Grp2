# SC2002 Restaurant Rush

Repository: [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

## Build and run

Requires JDK 11 or later. Run from the repository root in a macOS or Linux terminal:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Main
```

## Customer food selection

Customers choose food using `(customerId - 1) % 3`, with the menu ordered Salad,
Burger, Pasta. IDs follow arrival order: customers 1 and 4 choose Salad, 2 and 5
choose Burger, and 3 and 6 choose Pasta.

## Stage 1 test notes

See [TESTING.md](TESTING.md) for reproducible victory and defeat runs.

## Stage comparisons

Pending Stage 2 and Stage 3 implementation. For each transition, the comparison
will list new files, changed or deleted files with reasons, and the relevant
commit or compare link.

## Video timestamps

Pending recording.

# SC2002 Restaurant Rush

Repository: [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

## Running the game

The project needs JDK 11 or later. In Eclipse, open `Stage2Main.java`, choose **Run As → Java Application**, and follow the prompts in the Console. The game is played by typing choices into the Console.

Stage 1 still starts from `Main.java`. Stage 2 starts from `Stage2Main.java` and has its own `Stage2GameCLI` and `Stage2GameController`.

To run it from a macOS or Linux terminal, run these commands from the project folder:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Stage2Main
```

After compiling, Stage 1 can be started with `java -cp out Main`.

## Stage 2 customers

The six customers arrive on turns 1, 4, 7, 10, 13 and 16. Customer 2 is a VIP and customer 5 is a Critic; the other four are Regular customers. VIP customers get a 10% discount. Regular and VIP customers choose food using the existing customer-ID rule, while the Critic orders Pasta. All three customer types use the same seating, preparation and serving flow.

## Testing

I played Stage 2 manually through the Console menu. The results from the victory and defeat runs are in [TESTING.md](TESTING.md). The files in `tests/inputs` are named for Stage 1; they were not used for these Stage 2 runs.
