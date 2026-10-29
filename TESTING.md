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
