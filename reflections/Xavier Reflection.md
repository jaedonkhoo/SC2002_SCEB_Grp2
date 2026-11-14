What did you learn:

That encapsulation decides where a rule can live. Customer.satisfaction is private with no setter, so when Happy Hour required waiting customers to recover 15 points, the only clean option was a recoverSatisfaction() method inside Customer, which also enforces the cap of 100 and limits recovery to WAITING or SEATED customers.

How did you apply it:

I made ComboMeal a subclass of MenuItem that computes its own name, price (90% of the pair, in integer cents) and preparation units, so Order, Chef and the customers’ food-choice rule accepted it unchanged. I kept every Happy Hour rule in one HappyHour object and applied its discount after the VIP discount, just before the Order locks the price (VIP Burger: $15 × 0.9 × 0.8 = $10.80).

What challenged you:

Deciding how much Stage 1 and 2 code I could modify, since Stage 3 had to extend the saved Stage 2 version, and getting Happy Hour’s timing right, which is activation at turn start, recovery on both active turns, and the counter ticking at turn end.

How did you solve it:

Host and ComboMeal needed no changes to earlier files but the recovery did, so I limited the edits and documented each reason in README. I saved Stage 1 and 2 console output before my changes and compared it after: identical, matching TESTING.md. For the timing I drew the turn as a flowchart, placed recovery in both activateHappyHour() and startTurn(), wrote 32 checks in Stage3Checks, and replayed the victory run with Happy Hour on different turns to see the revenue–satisfaction trade-off.
