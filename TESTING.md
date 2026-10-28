# Stage 1 test notes

These runs use the required base setting and the current CLI, including
automatic waiting when a staff member has no valid task. The input files are
plain text: each line supplies one integer to the next prompt.

Verified on 29 October 2026 using OpenJDK 26.0.2 on macOS, with Java 11 language
and API compatibility checked by `javac --release 11`. The expected results
below were checked against fresh compilation of the source.

## Build and replay

Run from the repository root:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Main < tests/inputs/stage-1-victory.txt
java -cp out Main < tests/inputs/stage-1-defeat.txt
```

Run each command separately for an independent game. Input redirected from a
file is not echoed, so several prompts may appear together on one output line.
Use `java -cp out Main` and the tables below for an interactive walkthrough.

In the input columns, spaces separate successive entries; press Enter after
each integer. **Auto wait** means no input at all. In particular, do not insert
a `0` for the Waiter on turn 9 of the victory run or for skipped Chef prompts.

## Reproducible victory

Input file: [stage-1-victory.txt](tests/inputs/stage-1-victory.txt)

| Turn | Waiter action | Waiter input | Chef action | Chef input |
| --- | --- | --- | --- | --- |
| 1 | Seat customer 1 at table 1 | `1 1 1` | Auto wait | — |
| 2 | Take customer 1's Salad order | `2 1` | Prepare customer 1, unit 1/1 | `1 1` |
| 3 | Serve customer 1 | `3 1` | Auto wait | — |
| 4 | Seat customer 2 at table 2 | `1 2 2` | Auto wait | — |
| 5 | Take customer 2's Burger order | `2 2` | Prepare customer 2, unit 1/1 | `1 2` |
| 6 | Serve customer 2 | `3 2` | Auto wait | — |
| 7 | Seat customer 3 at table 1 | `1 3 1` | Auto wait | — |
| 8 | Take customer 3's Pasta order | `2 3` | Prepare customer 3, unit 1/2 | `1 3` |
| 9 | Auto wait | — | Prepare customer 3, unit 2/2 | `1 3` |
| 10 | Serve customer 3 | `3 3` | Auto wait | — |
| 11 | Seat customer 4 at table 2 | `1 4 2` | Auto wait | — |
| 12 | Take customer 4's Salad order | `2 4` | Prepare customer 4, unit 1/1 | `1 4` |
| 13 | Serve customer 4 | `3 4` | Auto wait | — |
| 14 | Seat customer 5 at table 1 | `1 5 1` | Auto wait | — |
| 15 | Take customer 5's Burger order | `2 5` | Prepare customer 5, unit 1/1 | `1 5` |
| 16 | Serve customer 5 | `3 5` | Auto wait | — |
| 17 | Seat customer 6 at table 2 | `1 6 2` | Auto wait | — |
| 18 | Take customer 6's Pasta order | `2 6` | Prepare customer 6, unit 1/2 | `1 6` |

Expected final statistics:

```text
RESULT: VICTORY!
Turns Played: 18 / 18
Customers Served: 5
Customers Paid: 5
Unhappy Departures: 0
Total Revenue: $66.00
Average Satisfaction: 79.2
```

Payment checkpoints:

| Customer | Food | Served on turn | Ready to pay at end of turn | Paid at end of turn | Satisfaction at payment | Bill |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Salad | 3 | 4 | 5 | 84 | $9.00 |
| 2 | Burger | 6 | 7 | 8 | 84 | $15.00 |
| 3 | Pasta | 10 | 11 | 12 | 76 | $18.00 |
| 4 | Salad | 13 | 14 | 15 | 76 | $9.00 |
| 5 | Burger | 16 | 17 | 18 | 76 | $15.00 |

The satisfaction average is `(84 + 84 + 76 + 76 + 76) / 5 = 79.2`.
Customer 6 has only one of two preparation units completed after turn 18 and
contributes no revenue or paid-customer satisfaction.

All three victory targets are already met after turn 15, but the game must
continue through turn 18. Customer 5's payment on turn 18 must be included in
the final result.

## Customer lifecycle checks

Follow customer 1 in the victory run:

| End of turn | Customer state | Order state | Satisfaction | Revenue | Table 1 |
| --- | --- | --- | --- | --- | --- |
| 1 | SEATED | No order | 92 | $0.00 | Occupied by customer 1 |
| 2 | SEATED | READY | 84 | $0.00 | Occupied by customer 1 |
| 3 | EATING | SERVED | 84 | $0.00 | Occupied by customer 1 |
| 4 | READY_TO_PAY | SERVED | 84 | $0.00 | Occupied by customer 1 |
| 5 | PAID | PAID | 84 | $9.00 | Empty |

Customer and order rows are printed at turn start. For example, the end-of-turn
3 `EATING` state appears in turn 4's customer table. The end-of-turn summaries
show the updated statistics immediately.

Serving first sets `JUST_SERVED`; it becomes `EATING` at that turn's end. The
customer cannot pay during the serving turn or the following eating turn.
The first payment is at the end of turn 5, so the table is still occupied during
the manager choices on turn 5.

Customer 3 checks the two-unit preparation rule: Pasta is `PREPARING` after
turn 8, becomes `READY` after turn 9, and is served on turn 10. Automatic Waiter
waiting on turn 9 does not skip the Chef's preparation opportunity.

## Reproducible defeat

Input file: [stage-1-defeat.txt](tests/inputs/stage-1-defeat.txt)

Choose Waiter action `0` once on every turn, from 1 through 18. The Chef always
waits automatically because no order exists. The file therefore contains
exactly eighteen lines of `0`.

Expected final statistics:

```text
RESULT: DEFEAT!
Turns Played: 18 / 18
Customers Served: 0
Customers Paid: 0
Unhappy Departures: 2
Total Revenue: $0.00
Average Satisfaction: N/A
```

Customer 1 reaches 0 satisfaction and leaves at the end of turn 13. Customer 2
leaves at the end of turn 16. Each departure is counted once. The later arrivals
remain waiting at the end of turn 18; their remaining satisfaction is not
included in the paid-customer average. Both tables stay empty throughout.

## Invalid input and retry checks

Start a fresh interactive game. These unsuccessful attempts must not advance
the turn or use the Waiter's task:

1. At the turn 1 Waiter menu, enter `abc`, then `9`. Expect a whole-number
   explanation, then a message listing valid menu choices.
2. Enter `2`, then `3`. Both actions are marked unavailable. Each must explain
   the missing prerequisite and return to the menu without asking for IDs.
3. Enter `1`, customer ID `999`, table ID `1`. Expect `Customer ID not found.`
4. Enter `1`, customer ID `1`, table ID `999`. Expect `Table ID not found.`
5. Enter `1`, customer ID `1`, table ID `1`. Seating must still succeed during
   turn 1. The Chef then waits automatically.
6. On turn 2, take customer 1's order. At the Chef menu, enter `9`, then choose
   preparation with customer ID `999`. Both must allow a retry. Prepare customer
   1 successfully using `1`, then `1`.
7. Continue with the victory table from turn 3. The final result must still be
   5 paid customers, $66.00 revenue and average satisfaction 79.2.

These retries were also replayed during verification of the documented runs.

## Additional implementation checks

Focused temporary Java checks during development also verified:

- One staff task per turn, including direct calls to staff/controller methods.
- Automatic waiting, voluntary waiting and preparation of newly taken orders.
- Rejected actions leaving customer/order state, preparation progress and staff
  availability unchanged.
- Cancellation and table release when an unserved customer abandons.
- Payment and eating delays, including a table remaining occupied until payment.
- Duplicate `recordPayment()` calls returning `false` without changing revenue,
  paid-customer count or average satisfaction; distinct customers still record.

Those temporary checks are not a committed unit-test suite. The two input
fixtures and the manual walkthroughs above are the reproducible repository
test evidence for Stage 1.
