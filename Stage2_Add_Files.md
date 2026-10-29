# Stage 2 notes

For Stage 2, I added VIP and Critic customers while keeping the Stage 1 source
files as they were. The Stage 2 version has its own controller, command-line
interface and `Stage2Main` entry point, so I can run it without replacing the
Stage 1 `Main` or `GameController`.

The base run still has six customers arriving on turns 1, 4, 7, 10, 13 and 16.
I made customer 2 a VIP and customer 5 a Critic, so both types appear in every
base-setting run. The other four customers are Regular customers.

VIPs lose 5 satisfaction points for each turn they wait and receive 10% off the
listed price. Regular customers and VIPs use the existing customer-ID rule to
choose food. Critics lose 12 satisfaction points per waiting turn and always
choose Pasta. I chose Pasta as the Critic's preference because it is one of the
three existing menu items; the Critic follows the same ordering and service
steps as the other customers.

The new customer types implement `Stage2CustomerBehavior`. The Stage 2
controller asks that behavior for the food and price, while satisfaction loss
continues to use the existing polymorphic method on `Customer`. This lets the
Stage 2 classes provide their behavior without editing the Stage 1 files.

## New Java files

- `src/Stage2CustomerBehavior.java`
- `src/VIPCustomer.java`
- `src/CriticCustomer.java`
- `src/Stage2GameController.java`
- `src/Stage2GameCLI.java`
- `src/Stage2Main.java`

## Running Stage 2

From the repository root, compile and run with JDK 11 or later:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Stage2Main
```

The Stage 1 game can still be started with `java -cp out Main`.

## Testing status

The Stage 2 code still needs to be compiled and replayed with the victory and
defeat inputs. Record the actual Stage 2 results here after running those
checks; Stage 1's old results should not be presented as Stage 2 test results.
