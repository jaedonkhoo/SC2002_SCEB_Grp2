# Personal Reflection — PENG YIFAN

Contribution：

Implemented Stage 2 of the Restaurant Rush game. Added VIP and Critic customer types, and the Stage 2 controller, CLI and main class, on top of the Stage 1 code.


What did you learn：

I learnt about polymorphism in a real project. Before this, I did not really understand why we need it, but in this project I saw it clearly. VIP and Critic have different rules, so I put the rules in their own classes instead of writing if-else checks in the controller.
How did you apply it：

I made both VIPCustomer and CriticCustomer extend the abstract Customer class and override getSatisfactionLoss(). VIP loses 5 satisfaction per waiting turn and gets 10% off the bill. Critic loses 12 and always orders Pasta at full price. They also implement the Stage2CustomerBehavior interface for chooseFood() and billPriceCents(). The controller just calls these methods, so it does not need to check what type the customer is.


What challenged you：

The hardest part for me was keeping Stage 1 working while adding Stage 2. I was afraid I would break the Stage 1 code when I added the new customer types. I also had to make sure the Stage 2 game still had 6 arrivals, on turns 1, 4, 7, 10, 13 and 16, with customer 2 as VIP and customer 5 as Critic.


How did you solve it：

I downloaded the Stage 1 code from its branch first, then I only added new files for Stage 2: VIPCustomer, CriticCustomer, Stage2CustomerBehavior, Stage2GameController, Stage2GameCLI and Stage2Main. I copied the Stage 1 controller into Stage2GameController instead of modifying the Stage 1 class, so the stage-1 tag still works unchanged. After that I tested a victory run and a defeat run, and both worked. I learnt that we can add new features without changing old code, and that is the main idea of object-oriented design.
