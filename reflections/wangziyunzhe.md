# Personal Reflection — WANG ZIYUNZHE

**Contribution:**

I worked on the UML diagrams for the Restaurant Rush project, mainly the Stage 3 sequence diagram. My task was to show how the different classes interact during a game turn, including VIP customers, Happy Hour discounts and staff actions. I also checked that the diagrams were consistent with the game logic.

**What did you learn:**

I learnt how UML diagrams can help us understand object-oriented design. Before this project, I mainly focused on writing code, but I realised that diagrams are useful for showing how different objects communicate with each other. I also learnt more about inheritance and how sequence diagrams represent method calls.

**How did you apply it:**

I created a sequence diagram showing how GameController interacts with GameState, Host, Waiter, Chef and VIPCustomer. For example, I showed how a VIP customer receives a 10% discount, followed by an additional 20% Happy Hour discount. I also represented the order preparation process and how the customer's state changes during the turn.

**What challenged you:**

The hardest part was making sure the sequence diagram matched the actual game logic. Some actions depend on previous actions, and the customer's status can change during the game. It was easy to miss a method call or show an incorrect state.

**How did you solve it:**

I traced the method calls step by step and checked the state changes after each action. I also separated the sequence diagram into different parts to make it easier to understand. When I found inconsistencies, I went back to check the game logic and adjusted the diagram. This helped me understand why UML modelling is important for designing and explaining software.
