1. Users will be asked to enter the name of .txt which contains all JSON objects
   a. 1 JSON object has to be in 1 line.
   b. This file must be inside dblp-explorer folder.
 2. Users will be asked to enter the keyword they want to look for. 
   a. All ID will be printed out on Console.
 
 * Algorithm:
    1. The code will go through every line in JSON files and store only the Id and the line number while finding the very first tier containing keyword..
    2. Then it will recursively check if each JSON object in tier 1 has references:
        a. Yes, then increase number of tier and recursively check again for each reference.
        b. No, then terminate.
    3. This is efficient since it does not create JSON object for every single one, it only do when there is a JSON file meets the requirement (has to contain keyword or in the references)