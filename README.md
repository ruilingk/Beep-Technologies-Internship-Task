# Beep-Technologies-Internship-Task

## Introduction
This is a Java Command Line Interface (CLI) application that is an executable. It has 5 functions for you to choose
from, which are:
1. Give 1 number and all the palindromic primes from number 1 to that number will be printed.

2. Give 2 numbers from 1 to 100,000 and all the numbers between those two which are palindromic prime after removing 
   one digit will be printed.
   
3. Give a valid text file path and the top 10 words with their individual frequency, together with the sentences where 
   they appear for the first and last time will be printed.

4. A GET request on "https://dev.beepbeep.tech/v1/sample_customer" will be performed, and the list of promotions sorted 
   by their titles in descending alphabetical order will be printed.

5. Several details will be printed when given the files inside data folder (which can be found in this repository).

## Getting Started
1. Download the `BeepTechnologies.jar` file [here](https://github.com/ruilingk/Beep-Technologies-Internship-Task/releases/tag/v0.1).
2. Place it in any folder that you wish and run the executable using `Command Prompt`.
3. Enter `java -jar BeepTechnologies.jar` and it should be running!

## User Manual
Here is what is expected from your inputs:
* For function 1:
  - User input: `1 [YOUR NUMBER]`
* For function 2:
  - User input: `2 [FIRST NUMBER] [SECOND NUMBER]`, where `[FIRST NUMBER]` is smaller than or equals to `[SECOND NUMBER]`
* For function 3:
  - User input: `3 test.txt`, if `test.txt` is in the same folder as your `JAR` executable 
* For function 4:
  - User input: `4`
* For function 5:
  - User input: `5`