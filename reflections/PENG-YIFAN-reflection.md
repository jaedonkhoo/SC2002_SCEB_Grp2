# Personal Reflection — PENG YIFAN

In Stage 2, I worked on adding VIP and Critic customers. I learned about polymorphism. Before this, I did not really understand why we need it, but in this project I saw it clearly. VIP and Critic have different rules, so I put the rules in their own classes. VIP loses 5 satisfaction and gets 10% off. Critic loses 12 and always orders Pasta.

I made both classes extend Customer and override `getSatisfactionLoss()`. They also use the `Stage2CustomerBehavior` interface for choosing food and calculating the bill. The controller just calls these methods, so it does not need to check what type the customer is.

The hardest part for me was keeping Stage 1 working. I was afraid I would break it when I added Stage 2. So I copied the Stage 1 controller and made a new one for Stage 2, and I only added new files. After that I tested a victory run and a defeat run, and both worked. I learned that we can add new features without changing old code, and that is the main idea of object-oriented design.
