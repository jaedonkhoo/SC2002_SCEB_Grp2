Contribution: 

I implemented the baseline code across four core packages: domain(customer, menu, menuitem, order, table) enums(chefaction, customerstatus, orderstatus, waiteraction), staff(cashier, chef, staff, waiter), and controller(gamecontroller) (excluding the CLI UI)

What did you learn?

better understanding on inheritance, encapsulation, polymorphism
how to use package to organize the program

How did you apply it?

write abstract base class Staff, with Waiter, Chef, and Cashier overriding specific behaviors
use CustomerStatus and OrderStatus enum types to limit the state values, and prevent illegal state edit.

What challenged you?

how to correctly build a structure to start the program
some functions are omitted
how to make sure that one action per staff per turn is triggered correctly

How did you solve it?

Divide the code into four packages: domain, enums, staff, and controller

For the one action per staff per turn, I first add a boolean flag isBusyThisTurn inside the parent class staff, and then no matter what staff is, the first thing is to check the flag. and if thy are true (busy), they don’t do the work, which return false. Once they finish, the set the flag true. Finally, for the beginning of the loop, the flag is reset to flase, at the start of each turn. 


