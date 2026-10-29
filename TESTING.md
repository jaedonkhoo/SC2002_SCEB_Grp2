Test results

I compiled the project with Eclipse's JDK 21 compiler using `--release 11`, then ran the Stage 1 and Stage 2 input files from the project root. The Stage 1 inputs are kept separate from the Stage 2 inputs.

## Stage 1 victory

```sh
java -cp out Main < tests/inputs/stage-1-victory.txt
```

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 5
Unhappy Departures: 0
Total Revenue: $66.00
Average Satisfaction: 79.2
```

## Stage 1 defeat

```sh
java -cp out Main < tests/inputs/stage-1-defeat.txt
```

The Waiter waits on every turn, so no orders are placed. The Chef waits automatically.

```text
RESULT: DEFEAT!
Turns Played: 18 / 18
Customers Served: 0
Customers Paid: 0
Unhappy Departures: 2
Total Revenue: $0.00
Average Satisfaction: N/A
```

## Stage 2 victory

This run includes Regular, VIP and Critic customers. Customer 2 gets the VIP discount. Customer 5 orders Pasta, which takes two preparation units. The Critic is served, but does not pay before the 18-turn limit. The sixth customer is seated on the last turn.

Run it from the project root:

```sh
java -cp out Stage2Main < tests/inputs/stage-2-victory.txt
```

On Windows Command Prompt, use:

```bat
type tests\inputs\stage-2-victory.txt | java -cp out Stage2Main
```

The run ends in victory because it reaches the base target of at least four paid customers, $45 revenue and 60 average satisfaction.

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 4
Unhappy Departures: 0
Total Revenue: $49.50
Average Satisfaction: 81.5
```

## Stage 2 defeat

This input makes the Waiter wait on every turn, so no orders are placed. The Chef waits automatically when there is nothing to prepare.

```sh
java -cp out Stage2Main < tests/inputs/stage-2-defeat.txt
```

```text
RESULT: DEFEAT!
Turns Played: 18 / 18
Customers Served: 0
Customers Paid: 0
Unhappy Departures: 1
Total Revenue: $0.00
Average Satisfaction: N
