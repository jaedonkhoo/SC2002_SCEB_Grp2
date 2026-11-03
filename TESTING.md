# Gameplay test notes

All runs use the required base setting. Compile using the instructions in
[README.md](README.md), then run the commands below from the repository root.
Each input file supplies one integer per prompt and omits automatically skipped
staff prompts.

## Stage 1

These results apply to the `stage-1` version. Use that version in a separate
checkout for these tests; the current Stage 2 victory run is documented below.

### Victory run

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

### Defeat run

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

## Stage 2

These tests apply to the current Stage 2 source. The six arrivals remain on
turns 1, 4, 7, 10, 13 and 16. Customer 3 is VIP, Customer 5 is Critic, and the
others are Regular. Compile again after changing versions.

### Victory run

Input: [stage-2-victory.txt](tests/inputs/stage-2-victory.txt)

```sh
java -cp out Main < tests/inputs/stage-2-victory.txt
```

Verified final result:

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 4
Unhappy Departures: 0
Total Revenue: $58.20
Average Satisfaction: 79.3
```

Verified checkpoints for this run (timings refer to the end of each turn):

| Check | Expected result |
|---|---|
| VIP pricing | Customer 3 orders Pasta for $16.20 on turn 8, receiving 10% off. |
| Critic preference | Customer 5 orders the most expensive item, Pasta, for the full $18.00 on turn 14. |
| Satisfaction | VIP loses 5 per waiting turn and is served at 85; Critic loses 12 and is served at 64. Neither loses satisfaction after service. |
| Payment timing | VIP: served on turn 10, READY_TO_PAY on 11, paid on 12. Critic: served on 16, READY_TO_PAY on 17, paid on 18. |

### Defeat run

Reuse the all-wait input from Stage 1; no separate duplicate file is needed:

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

### Invalid-action regression checks

Verified retries for non-integer input, invalid menu choices, unknown customer
IDs, duplicate orders and serving an unfinished order. Each rejection explains
the failure and allows another choice without consuming the staff task. Retrying
with valid actions produces the same Stage 2 victory statistics above.
