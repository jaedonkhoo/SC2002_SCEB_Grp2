# Stage 1 test notes

Both runs use the required base setting. Compile using the instructions in
[README.md](README.md), then run the commands below from the repository root.
Each input file supplies one integer per prompt and omits automatically skipped
staff prompts.

## Victory run

Input: [stage-1-victory.txt](tests/inputs/stage-1-victory.txt)

```sh
java -cp out Main < tests/inputs/stage-1-victory.txt
```

Verified final result:

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 5
Unhappy Departures: 0
Total Revenue: $66.00
Average Satisfaction: 79.2
```

## Defeat run

Input: [stage-1-defeat.txt](tests/inputs/stage-1-defeat.txt)

The Waiter chooses `0` on every turn. The Chef waits automatically because no
orders are placed.

```sh
java -cp out Main < tests/inputs/stage-1-defeat.txt
```

Verified final result:

```text
RESULT: DEFEAT!
Turns Played: 18 / 18
Customers Served: 0
Customers Paid: 0
Unhappy Departures: 2
Total Revenue: $0.00
Average Satisfaction: N/A
```
# Stage 2 test results

I ran `Stage2Main` manually in Eclipse with Java 21 and played through the Console menu. These were manual games, not automated tests. The files in `tests/inputs` are named for Stage 1, so I did not use them for these Stage 2 runs.

## Victory run

I used the normal game menu to seat customers, take orders, prepare the food and serve the customers. The run included a Regular customer, a VIP and a Critic. I tried the Salad, Burger and Pasta orders. The Critic ordered Pasta, which needed two preparation units. After the food was served, the cashier processed payment automatically.

The sixth customer was seated on the final turn, so there was no time left to finish that customer's order before the 18-turn limit.

The game showed this result at the end:

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 5
Unhappy Departures: 0
Total Revenue: $67.50
Average Satisfaction: 74.8
```

## Defeat run

For this run, I entered `0` at every Waiter action prompt for all 18 turns. When there were no orders to prepare, the Chef waited automatically.

The game showed this result at the end:

```text
RESULT: DEFEAT!
Turns Played: 18 / 18
Customers Served: 0
Customers Paid: 0
Unhappy Departures: 1
Total Revenue: $0.00
Average Satisfaction: N/A
```
