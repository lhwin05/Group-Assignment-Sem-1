Refactored code for all files and integrated them into `LoginMain`, where users log in and naviagate the main menu interface. Different features are executed by instantiating non static as objects (attendance and sale), then calling their object's main menu (`Searchinformation.main_menu`, `FilterAndSort.main_menu`, etc.), based on the choice of the user. Each time a process ends, program asks user if they want to log out, loops over the selection menu if they choose not to. 
<br>
<br>
Program can be run smoothly after much testing. It is advisable to test your own part of the program in case I missed out anything :) 
