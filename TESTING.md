# Gameplay test notes

All runs use the required base setting. Compile using the instructions in
[Build and Run](README.md#build-and-run), then run the commands below from the repository root.
Each input file supplies one integer per prompt and omits automatically skipped
staff prompts.

## Stage 1

These results apply to the `stage-1` version. Use that version in a separate
checkout for these tests. Tests for the current code are under [Stage 3](#stage-3).

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

These tests apply to the `stage-2` version. The six arrivals remain on
turns 1, 4, 7, 10, 13 and 16. Customer 3 is VIP, Customer 5 is Critic, and the
others are Regular. Use a separate checkout of `stage-2` and compile again before running these inputs.

See [Customer Behaviour](README.md#customer-behaviour) for the selection and pricing rules.

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

## Stage 3

These tests apply to the Stage 3 version. The six arrivals remain on turns
1, 4, 7, 10, 13 and 16. Customer 3 is VIP, Customer 5 is Critic, and the
others are Regular.

Stage 3 adds the Host, three ComboMeal combinations, and Happy Hour; see
[Stage 3 Features](README.md#stage-3-features). Staff with no eligible actions
are automatically skipped.

Compile the Stage 3 source:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
```

### Victory run

Input: [stage-3-victory.txt](tests/inputs/stage-3-victory.txt)

```sh
java -cp out Main < tests/inputs/stage-3-victory.txt
```

This run activates Happy Hour at the start of turn 7. The Host seats
customers, the Waiter takes and serves orders, and the Chef prepares
up to one unit each turn.

Verified final result:

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 5
Unhappy Departures: 0
Total Revenue: $88.26
Average Satisfaction: 85.4
```

Verified checkpoints for this run:

| Check | Expected result |
|---|---|
| Host, Waiter and Chef | On turn 1, Host seats Customer 1, Waiter takes the Salad order, and Chef completes its 1/1 preparation unit. The Waiter serves it on turn 2. |
| Happy Hour duration | Activated on turn 7. Active on turns 7 and 8; expires on turn 9. |
| VIP and Happy Hour pricing | Customer 3 orders Pasta on turn 7 for $12.96 after the VIP and Happy Hour discounts. |
| Satisfaction recovery | Customer 3 ends turn 7 at 95 satisfaction, recovers to 100 at the start of turn 8, and ends turn 8 at 95. |
| Two-unit combo | Customer 4 orders Salad + Burger Combo for $21.60 on turn 10, is served on turn 12 and pays on turn 14. |
| Critic preference | Customer 5 orders Burger + Pasta Combo, the most expensive item, for $29.70 on turn 13. |
| Three-unit preparation | Customer 5's combo requires Chef actions on turns 13, 14 and 15, is served on turn 16 and paid on turn 18. |
| Third combo | Customer 6 orders Salad + Pasta Combo for $24.30 on turn 17. The order is only 2/3 prepared by turn 18 and contributes no revenue. |
| Payment timing | Customer 3 is served on turn 9, becomes READY_TO_PAY on turn 10 and pays on turn 11. |

### Defeat run

Input: [stage-3-defeat.txt](tests/inputs/stage-3-defeat.txt)

```sh
java -cp out Main < tests/inputs/stage-3-defeat.txt
```

This run activates Happy Hour on turn 1 and chooses Wait for the
Host and Waiter throughout the game. The Chef waits automatically
because no orders are placed.

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
