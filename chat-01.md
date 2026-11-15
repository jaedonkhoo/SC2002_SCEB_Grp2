# SC2002 Restaurant Rush — AI History (Stage 2)
# Peng Yifan — ai-history/stage-2/chat-01.md

## 1. Stage 2 code — VIP and Critic

**My prompt:**
Check my Stage 2 code. I added VIPCustomer and CriticCustomer on top of Stage 1.

**What the code does (the assignment content):**
- Stage 2 has 6 arrivals, on turns 1, 4, 7, 10, 13, 16.
- Customer 3 is VIP. Customer 5 is Critic.
- VIPCustomer extends Customer, overrides getSatisfactionLoss() = 5, and calculatePriceCents() = (priceCents * 9 + 5) / 10, so 10% off.
- CriticCustomer extends Customer, overrides getSatisfactionLoss() = 12, and chooseMenuItem() = the most expensive item on the menu, at full price.
- RegularCustomer loses 8 satisfaction per waiting turn, at full price.
- The controller does not check customer type. It calls chooseMenuItem() and calculatePriceCents() on the Customer object, and polymorphism decides which version runs.
- I downloaded the Stage 1 branch code first, only added VIPCustomer and CriticCustomer, and changed the arrival creation in GameController. I did not change the order-processing flow. I tested a victory run and a defeat run.
- Stage 2 victory run in TESTING.md: 5 served, 4 paid, $58.20, average satisfaction 79.3. VIP Customer 3 orders Pasta $16.20 on turn 8, served on turn 10, paid on turn 12. Critic Customer 5 orders Pasta $18.00 on turn 14, served on turn 16, paid on turn 18.

## 2. Personal reflection — full text I submitted

**My prompt:**
Reformat my reflection like my teammate's, with the headings Contribution / What did you learn / How did you apply it / What challenged you / How did you solve it.

**The reflection (my own work, AI only polished the English):**

Contribution
Implemented Stage 2 of the Restaurant Rush game. Added the VIP and Critic customer types on top of the Stage 1 code.

What did you learn
I learnt about polymorphism in a real project. Before this, I did not really understand why we need it, but in this project I saw it clearly. VIP and Critic have different rules, so I put the rules in their own classes instead of writing if-else checks in the controller.

How did you apply it
I made both VIPCustomer and CriticCustomer extend the abstract Customer class and override getSatisfactionLoss(). VIP loses 5 satisfaction per waiting turn and gets 10% off the bill. Critic loses 12 and always chooses the most expensive item on the menu, at full price. They also override chooseMenuItem() and calculatePriceCents() in the Customer class, so the controller just calls these methods and does not need to check what type the customer is.

What challenged you
The hardest part for me was keeping Stage 1 working while adding Stage 2. I was afraid I would break the Stage 1 code when I added the new customer types. I also had to make sure the Stage 2 game still had 6 arrivals, on turns 1, 4, 7, 10, 13 and 16, with customer 3 as VIP and customer 5 as Critic.

How did you solve it
I downloaded the Stage 1 code from its branch first, then I only added new customer files for Stage 2, VIPCustomer and CriticCustomer, and changed the arrival creation in GameController so customer 3 is created as VIP and customer 5 as Critic. I did not change the existing order-processing flow. After that I tested a victory run and a defeat run, and both worked. I learnt that we can add new features without changing old code, and that is the main idea of object-oriented design.

## 3. Video script — full text I recorded

**My prompt:**
Write my video script. My part is the FIRST segment, game introduction and the three stages, about 2 minutes.

**The script (my own part, AI only polished the English):**

Our game is called Restaurant Rush. It is a Java command-line game where the player is a restaurant manager. The game lasts 18 turns, and we have 2 tables, 1 waiter, 1 chef, and 1 cashier. Customers arrive on turns 1, 4, 7, 10, 13, and 16. To win, we need at least 4 customers to pay, earn at least $45, and keep the average satisfaction at 60 or above.

The game has three stages. In Stage 1, there are only regular customers. The manager must choose what the waiter and the chef do each turn, for example, seating customers, taking orders, and serving food.

In Stage 2, we added VIP and Critic customers. Customer 3 is VIP. VIP customers lose satisfaction more slowly and get a 10% discount. Customer 5 is Critic. Critic customers lose satisfaction faster and always choose the most expensive item on the menu.

In Stage 3, we added a host, combo meals, and happy hour. The host can help seat customers, combo meals combine two dishes at a lower price, and happy hour gives a 20% discount for two turns.

That is the overview of our game. Next, my teammate will show you the gameplay.

## 4. Declaration on Use of GAI Assistance

**My prompt:**
I sent my signed declaration form. Only item iii is ticked.

**The declaration:**
- Student: Peng Yifan, peng0207@e.ntu.edu.sg
- Course: SC2002 Object Oriented Design & Programming, Instructor: Li Fang
- Title: SC2002 Restaurant Rush Group Assignment
- Date: 14/11/2026, Signature: Peng Yifan
- Item iii ticked: "Used GAI to refine syntax and grammar for correct language submission only."
- Items i, ii and iv are not ticked.
- Page 6 optional questions are blank — it says Optional, so I did not write them.
- I formulated and wrote the reflection and video script myself. My English is not good as an international student, so AI only polished / translated the language.
