# SC2002 Restaurant Rush

Restaurant Rush is a Java command-line restaurant management game for SC2002.
This version implements Stage 1: regular customers, two tables, a Waiter, a Chef,
an automatic Cashier, and an 18-turn game with victory and defeat outcomes.

Repository: [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

## Build and run

Use JDK 11 or later. Both `java` and `javac` must be available in your terminal.
There are no external dependencies or network requirements for gameplay.

Run these commands from the repository root in a macOS or Linux terminal:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Main
```

`Main` is the entry point. Compiled classes go into `out/`, which Git ignores.
Recompile after changing source files. In VS Code, you can also run the `main`
method in `src/Main.java` with Java support and a JDK configured.

The documented runs were verified on 29 October 2026 with OpenJDK 26.0.2,
compiling against Java 11 using `--release 11`. Execution on a Java 11 runtime
was not separately tested.

## Base setting

| Setting | Value |
| --- | --- |
| Turns | Exactly 18; no early victory or defeat |
| Tables | 2 |
| Waiter | At most one task per turn |
| Chef | At most one preparation unit per turn |
| Cashier | Automatically processes at most one payment per turn |
| Arrivals | One customer on turns 1, 4, 7, 10, 13 and 16 |
| Initial satisfaction | 100 |
| Waiting penalty | 8 points per turn, with a minimum of 0 |

Victory requires all three conditions at the end of turn 18:

- At least 4 paid customers.
- Revenue of at least $45.00.
- Average satisfaction of paid customers of at least 60.

Otherwise, the result is defeat. Revenue includes only recorded payments, and
the average uses satisfaction when each customer pays. Before any payment, the
display shows `N/A`.

## Menu and customer choice

| Item | Price | Preparation units |
| --- | --- | --- |
| Salad | $9.00 | 1 |
| Burger | $15.00 | 1 |
| Pasta | $18.00 | 2 |

Customers choose food automatically using `(customerId - 1) % 3` with the menu
ordered Salad, Burger, Pasta. Customer IDs follow arrival order, so customers
1 and 4 choose Salad, 2 and 5 choose Burger, and 3 and 6 choose Pasta.
The manager does not choose the food. The CLI displays the choice immediately
after an order is taken. Prices and revenue are stored as integer cents.

## Controls and display

Enter one integer at each prompt. Seating asks for a customer ID and then a
table ID. Other tasks ask for the customer ID; there is no separate order ID.

| Staff | Choice | Action |
| --- | --- | --- |
| Waiter | 1 | Seat a customer |
| Waiter | 2 | Take an order |
| Waiter | 3 | Serve a ready order |
| Waiter | 0 | Wait |
| Chef | 1 | Advance an order by one preparation unit |
| Chef | 0 | Wait |

Action numbers stay fixed. The Waiter menu marks unavailable actions and lists
eligible customer IDs. Seating lists free table IDs, and the Chef menu shows
eligible orders with preparation progress, such as `1/2`.

When a staff member has no valid task, the game prints an automatic-wait message
and skips that staff member's input prompts. Do not enter an extra `0` for a
skipped prompt. If a task is available, choosing `0` remains optional.
Invalid input and invalid actions explain the problem and allow another attempt
without consuming a staff task.

Each turn displays revenue, paid-customer average satisfaction, counts, the
waiting queue, tables and customer/order states. A short summary follows each
turn, and final statistics follow turn 18.

## Turn sequence

1. Add any scheduled arrival, reset staff availability and display the state.
2. Let the Waiter choose a task or wait.
3. Let the Chef choose an order to prepare or wait. Orders taken this turn are
   eligible, but a dish prepared now cannot be served until a later turn.
4. Process at most one customer who was already `READY_TO_PAY`. Payment marks
   the customer and order `PAID`, records revenue and satisfaction, and frees
   the table.
5. Move existing `EATING` customers to `READY_TO_PAY`, then move `JUST_SERVED`
   customers to `EATING`.
6. Apply waiting penalties. Customers who reach 0 leave; any unserved order is
   cancelled, and their table is freed.
7. Advance the turn, or display the final result after turn 18.

The reverse order of the end-of-turn transitions prevents a customer from
advancing through multiple lifecycle stages in one turn. A customer served on
turn T becomes ready to pay at the end of T + 1 and pays at the end of T + 2.
Served, eating and ready-to-pay customers do not lose satisfaction. A table
remains occupied until payment or abandonment.

## Design overview

| Classes | Responsibility |
| --- | --- |
| `Main`, `GameCLI` | Start the application; collect input and display results |
| `GameController` | Coordinate turns, arrivals, available actions and outcomes |
| `GameState` | Store customers, queue, tables, turn number and statistics |
| `Waiter`, `Chef`, `Cashier` | Perform staff operations |
| `Customer`, `RegularCustomer` | Manage customer lifecycle and satisfaction behaviour |
| `Order`, `MenuItem`, `Table` | Manage order progress, menu data and table occupancy |
| `GameConfig` | Define fixed base-setting values |
| Status enums, `ActionResult` | Represent lifecycle states and explain action outcomes |

Domain objects enforce their own transitions. Availability queries reuse those
checks without changing state. `GameState` exposes unmodifiable list views and
tracks recorded payment IDs to prevent duplicate revenue and satisfaction
recording.

## Testing

See [TESTING.md](TESTING.md) for the verified turn-by-turn victory and defeat
runs, lifecycle checkpoints and invalid-input checks. From the repository root,
after compiling:

```sh
java -cp out Main < tests/inputs/stage-1-victory.txt
java -cp out Main < tests/inputs/stage-1-defeat.txt
```

## Stages and release status

- Stage 1: basic restaurant operation, implemented in this version.
- Stage 2: VIP and Critic behaviour, not yet implemented.
- Stage 3: Host, ComboMeal and Happy Hour, not yet implemented.

The intended release tag for this version is `stage-1`. Creating and pushing
that tag is a manual release step after committing the source and test notes.
This README does not imply that the tag has already been published.

When later stages are implemented, add the required stage comparison notes,
reasons for changed files and commit/compare links. Video timestamps and final
submission materials will also be added for the complete three-stage project.
