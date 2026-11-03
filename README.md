# SC2002 Restaurant Rush

**Repository:** [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

## Build and Run

Requires JDK 11 or later. Run from the repository root:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Main
```

## Customer Behaviour

Customers select their own food; the player manages staff priorities.

| Type | Satisfaction Loss | Food Preference | Pricing |
|---|---|---|---|
| Regular | 8 | Default selection rule | Full price |
| VIP | 5 | Default selection rule | 10% discount |
| Critic | 12 | Most expensive item | Full price |

The default food-selection rule is `(customerId - 1) % menu.size()`, with menu items ordered Salad, Burger and Pasta.

Critics select the first item in the event of a price tie. Prices are stored in integer cents and rounded to the nearest cent when discounted.

**Stage 2 arrivals:** Customer 3 (turn 7) is VIP, Customer 5 (turn 13) is Critic, and all remaining customers are Regular.

## Testing

See [TESTING.md](TESTING.md) for reproducible gameplay tests, including victory, defeat, customer behaviour and regression checks.

## Stage Comparisons

### Stage 1 → Stage 2

**New files:**

- `VIPCustomer.java` — VIP satisfaction loss and pricing.
- `CriticCustomer.java` — Critic satisfaction loss and food preference.
- `tests/inputs/stage-2-victory.txt` — Reproducible victory including VIP and Critic payments.

**Modified files:**

- `Customer.java` — Added polymorphic food selection, pricing and type identification.
- `RegularCustomer.java` — Added customer type identification.
- `GameController.java` — Added deterministic customer types and delegated ordering decisions to customer objects.
- `GameCLI.java` — Displays customer types and final order prices; improved readability.
- `GameState.java` — Simplified comments; payment and statistics logic is unchanged.
- `CustomerStatus.java` — Removed the inline `JUST_SERVED` comment; lifecycle timing remains explained in `GameController.endTurn()`.
- `README.md` — Documented Stage 2 behaviour, arrivals and stage comparison.
- `TESTING.md` — Added Stage 2 replay results and behaviour checks, retaining Stage 1 test notes.

**Deleted files:** None.

**Design impact:** Stage 2 uses inheritance and method overriding to extend customer behaviour without changing the existing order-processing flow. Stage 1 gameplay and win/lose conditions remain intact.

**Compare:** [stage-1 → stage-2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/compare/stage-1...stage-2)

### Stage 2 → Stage 3

Pending Stage 3 implementation.

## Design Documentation

The final report will include three stage-specific UML class diagrams, one sequence diagram, design discussion and individual reflections.

## AI Assistance History

Complete AI conversation histories are maintained on individual group-member branches.

**Links:** Pending.

## Video Timestamps

Pending recording.
