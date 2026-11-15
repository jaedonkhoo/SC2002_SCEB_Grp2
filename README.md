# SC2002 Restaurant Rush

**Repository:** [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

**Contents:**

- [Build and Run](#build-and-run)
- [Customer Behaviour](#customer-behaviour)
- [Stage 3 Features](#stage-3-features)
- [Testing](#testing)
- [Stage Comparisons](#stage-comparisons)
- [AI Assistance History](#ai-assistance-history)
- [Video Timestamps](#video-timestamps)

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

The default food-selection rule is `(customerId - 1) % menu.size()`.

- **Stage 1 and Stage 2:** Menu items are ordered Salad, Burger and Pasta.
- **Stage 3:** The menu adds Salad + Burger Combo, Burger + Pasta Combo and Salad + Pasta Combo, in that order.

Critics select the first item in the event of a price tie. Prices are stored in integer cents and rounded to the nearest cent when discounted.

**Stage 2 and Stage 3 arrivals:** Customer 3 (turn 7) is VIP, Customer 5 (turn 13) is Critic, and all remaining customers are Regular.

## Stage 3 Features

**Host:** The Host can seat one customer per turn independently of the Waiter. The Waiter retains all original actions, and the Chef can only act after the Waiter has finished.

**ComboMeal:** Combines two distinct base menu items at 90% of their combined price, with preparation units equal to their sum. Identical items and nested combos are not allowed.

**Happy Hour:** Can be activated once per game at the start of a turn and lasts for that turn and the next. It provides a 20% discount on newly placed orders and restores 15 satisfaction to customers waiting for a table or seated but not yet served at the start of each active turn, capped at 100. Customers who have left receive no bonus.

VIP discounts are applied before Happy Hour discounts, and order prices remain fixed after placement.

## Testing

See [TESTING.md](TESTING.md) for reproducible gameplay tests, including victory, defeat, customer behaviour, Stage 3 features and regression checks.

Jump to the test notes for [Stage 1](TESTING.md#stage-1), [Stage 2](TESTING.md#stage-2) or [Stage 3](TESTING.md#stage-3).

## Stage Comparisons

**Stage versions:** [Stage 1](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/tree/stage-1) · [Stage 2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/tree/stage-2) · [Stage 3](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/tree/stage-3)

File links below point to the destination stage's published tagged version.

### Stage 1 → Stage 2

**New files:**

- [VIPCustomer.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/VIPCustomer.java) — VIP satisfaction loss and pricing.
- [CriticCustomer.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/CriticCustomer.java) — Critic satisfaction loss and food preference.
- [tests/inputs/stage-2-victory.txt](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/tests/inputs/stage-2-victory.txt) — Reproducible victory including VIP and Critic payments.

**Modified files:**

- [Customer.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/Customer.java) — Added polymorphic food selection, pricing and type identification.
- [RegularCustomer.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/RegularCustomer.java) — Added customer type identification.
- [GameController.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/GameController.java) — Added deterministic customer types and delegated ordering decisions to customer objects.
- [GameCLI.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/GameCLI.java) — Displays customer types and final order prices; improved readability.
- [GameState.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/GameState.java) — Simplified comments; payment and statistics logic is unchanged.
- [CustomerStatus.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/src/CustomerStatus.java) — Removed the inline `JUST_SERVED` comment; lifecycle timing remains explained in `GameController.endTurn()`.
- [README.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/README.md) — Documented Stage 2 behaviour, arrivals and stage comparison.
- [TESTING.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-2/TESTING.md) — Added Stage 2 replay results and behaviour checks, retaining Stage 1 test notes.

**Deleted files:** None.

**Design impact:** Stage 2 uses inheritance and method overriding to extend customer behaviour without changing the existing order-processing flow. Stage 1 gameplay and win/lose conditions remain intact.

**Compare:** [stage-1 → stage-2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/compare/stage-1...stage-2)

### Stage 2 → Stage 3

**New files:**

- [ControllableStaff.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/ControllableStaff.java) — Introduced a shared superclass for staff action tracking, waiting and turn resets, reducing duplicated logic.
- [SeatingStaff.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/SeatingStaff.java) — Extracted common seating logic for the Waiter and Host.
- [Host.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/Host.java) — Added an additional staff member capable of seating one customer per turn.
- [StaffRole.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/StaffRole.java) — Added an enum to identify controllable staff and support shared controller operations.
- [ComboMeal.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/ComboMeal.java) — Added a `MenuItem` subclass that combines two base dishes, calculates discounted pricing and preparation units, and validates its components.
- [tests/inputs/stage-3-victory.txt](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/tests/inputs/stage-3-victory.txt) — Reproducible victory demonstrating Host, ComboMeal and Happy Hour.
- [tests/inputs/stage-3-defeat.txt](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/tests/inputs/stage-3-defeat.txt) — Reproducible defeat demonstrating the full 18-turn game without serving customers.

**Modified files:**

- [ActionResult.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/ActionResult.java) — Added `HOST_ALREADY_ACTED` and `WAITER_NOT_FINISHED` for the new staff restrictions.
- [Waiter.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/Waiter.java) — Now extends `SeatingStaff`, reusing seating and action-tracking logic while retaining order-taking and serving.
- [Chef.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/Chef.java) — Now extends `ControllableStaff`, reusing staff action tracking and turn resets.
- [GameController.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/GameController.java) — Added Host coordination, shared staff operations, ComboMeal integration, Happy Hour effects and turn-order safeguards.
- [GameState.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/GameState.java) — Added Happy Hour activation tracking and remaining-duration calculation.
- [Customer.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/Customer.java) — Added satisfaction recovery capped at 100 for Happy Hour, with negative increases rejected.
- [GameConfig.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/GameConfig.java) — Moved starting-menu creation into a method and added the three ComboMeal combinations.
- [GameCLI.java](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/src/GameCLI.java) — Added staff selection, Host actions, automatic waiting, Happy Hour controls and improved combo-name formatting.
- [README.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/README.md) — Documented Stage 3 features, design changes and testing evidence.
- [TESTING.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/stage-3/TESTING.md) — Added Stage 3 victory and defeat runs with feature-specific checkpoints.

**Deleted files:** None.

**Design impact:** Stage 3 introduces shared staff superclasses to reduce duplication while preserving role-specific behaviour. ComboMeal demonstrates inheritance and composition, while Happy Hour is implemented through the existing controller and game state without introducing an unnecessary class.

**Regression:** Earlier customer behaviour, order preparation, payment timing and victory conditions remain intact. The Stage 3 victory and defeat runs both complete all 18 turns.

**Compare:** [stage-2 → stage-3](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/compare/stage-2...stage-3).

## AI Assistance History

Available AI conversation records are linked below on each contributor's branch.

| Contributor | Branch | Conversation files |
|---|---|---|
| Wang Ziyunzhe | [ai-history-kiyaya0829](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/tree/ai-history-kiyaya0829) | [chat-01.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/ai-history-kiyaya0829/chat-01.md), [chat-02.md](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/ai-history-kiyaya0829/chat-02.md) |
| Ye Diwen | [YEDiwen](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/tree/YEDiwen) | [YeDiwen-aichat-history](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2/blob/YEDiwen/YeDiwen-aichat-history) |

The remaining contributors declared no AI use.

## Video Timestamps

| Segment | Timestamp |
|---|---|
| Game launching from the submitted project | 01:22 |
| Meaningful manager choices and staff constraints | 03:28 |
| READY → SERVED → payment | 02:02 |
| VIP or Critic behaviour | 02:49 (VIP); 03:43 (Critic) |
| Host | 01:47 |
| Victory and defeat | 05:02 (victory); 05:22 (defeat) |
| Stage comparisons and UML diagrams | 05:35 (Stage 1 UML); 07:21 (Stage 2 UML); 08:10 (Stage 1 → 2 changes); 08:25 (Stage 3 UML); 09:09 (Stage 2 → 3 changes) |
| Design decisions | 09:22 |
| Stage 3 sequence diagram | 09:49 | 
| Individual reflections | 13:09–14:29 |