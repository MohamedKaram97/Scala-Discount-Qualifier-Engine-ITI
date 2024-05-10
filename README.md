# Discount-Qualifier-Using-Scala
A huge retail store wants a rule engine that qualifies orders’ transactions to discounts based on a set of qualifying rules. And automatically calculates the proper discount based on some calculation rules

# Business Requirements
As mentioned above, the retail store has many orders to specify whether they need a discount or not, then add discount to them according to a specific criteria. This criteria has some qualifying rules and each qualifying rule has a corresponding calculation rule to be applied on the orders that meet this qualifying rule. It's a hard process to be performed manually, so a Discount Engine is used to ease this process and make changing the criteria at any time a more flexible action.

# Rules:

Transactions that didn't qualify to any discount will have 0% discount
Transactions that qualified to more than one discount will get the average of the top 2 discounts
Extra qualifying and calculation rules may be added at any time
Business also needs to store the processed information into any database, I chose Oracle DBMS.

# Requirements

Scala supports many programming paradigms, Imperative Programming, Functional Programming, and OOP.

Technical requirements need to be satisfied in our code:

No mutable variables or data structures allowed
No loops allowed
All functions must be pure:
Output depends solely on input
Inputs to the functions don't get mutated
Have a predictable behavior
No side effects
Also, a log file is required for the purpose of monitoring the flow of the program and for debugging.

# Logs Engine Output

![image](https://github.com/MohamedKaram97/Discount-Qualifier-Using-Scala/assets/154819767/d886cc05-95d5-4aa2-a06c-edcd25152898)


