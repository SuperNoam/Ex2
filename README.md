![image](https://github.com/user-attachments/assets/9f8f40b9-9b25-4bd7-bd83-24ea496f32c0)
# SpreadSheet Project
## Preview
### In this project, we attempted to create a spreadsheet-like program in Java, similar to Excel.

## Information
One of the most useful features of a spreadsheet is the ability to create formulas for calculations.
In our program, each cell can contain one of the following three types of data:

1) A decimal number (either positive or negative).
2) Text (any string you want to input).
3) A formula (which must start with = to indicate that it is a formula).
4) A formula can be one of the following four types:

* A decimal number.
* A reference to another cell (e.g., A4, B42, etc.).
* Another formula.
* An expression in the form of formula op formula, where op can be {+, -, *, /}.

The program takes the input and determines whether it is a number, text, or formula. If it is a formula, the program calculates its value and assigns it to the corresponding cell.
After that, the program runs a method that recalculates the values of all other cells to check for dependencies. If another cell depends on the updated cell, its value will also be updated accordingly.

⚠️ DANGER ZONE ⚠️
## * Be careful when inserting formulas!
## * If a formula has incorrect syntax, the corresponding cell will display an Error Formula message.
## * Another major risk is circular dependencies. If a formula creates an infinite loop (e.g., A0 references A1, and A1 references A0), the program will detect this and insert Error Cycle in every affected cell.
