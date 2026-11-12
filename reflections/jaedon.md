Contribution:

Helped create Stage 1; Standardized Stages 2 and 3 and UML class diagrams; Managed GitHub repository.

What did you learn:

I learnt the importance of designing classes for extensibility through this project.

How did you apply it:

In the process of designing and planning Stage 1, I decided to design Customer as an abstract class rather than a concrete one to ensure adding different customer types in the future would be easier and more seamless. I also tried to ensure that there were as few hard-coded values in the code as possible, by opting to store these values (such as number of tables, number of turns etc.) in a separate GameConfig class, to reduce the number of changes required in future versions should these values change. These were helpful when I went back to edit Stages 2 and 3, since less redesigning was required (especially for Stage 2 with the Customer abstract class).

What challenged you:

The one thing that challenged me the most was figuring out how to ensure a specific error message appeared when a player makes an invalid action.

How did you solve it:

I first attempted to use booleans in the different methods (such as seatCustomer and takeOrder in Waiter), by returning true if it completed successfully, and false otherwise. But this did not allow me to express different error messages for different scenarios. For example, the waiter could fail to seat a customer because the table is already occupied, or the customer does not exist for example. Returning a boolean would not allow me to differentiate between these different scenarios. So I decided to compile a list of possible invalid actions, and after realizing there were quite a number of overlaps, created an enum called ActionResult to store these values. I then made these methods return an ActionResult instead of a boolean, so that the GameController would know exactly what went wrong (or SUCCESS if nothing went wrong) when I ran these methods. This also gives an easy way to display the error messages in the command line interface by consolidating them in a method (displayActionFailure in GameCLI), which prevents unnecessary repetition of print statements. The main thing I learnt from this is how to code defensively. By requiring different error messages for different scenarios my methods can fail, I was required to figure out exactly how the methods I defined can go wrong. This made me rethink the design of different methods to ensure they either ran successfully or all possible errors can be expressed by an ActionResult (i.e. all possible errors are caught).


