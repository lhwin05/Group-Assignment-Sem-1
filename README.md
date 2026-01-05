# Group-Assignment-Sem-1
sem1 project yipieee !!!    (read in code form. idk how to make the preview look pretty)

We are building a store management system with a Java frontend (JavaFX, Swing) and a Python backend (Flask). To fulfill the project specifications, a database named "store_management_system" was created in HeidiSQL, a MySQL-based DBMS, wherein queries are written and tested. A total of 11 tables have been created with the following attributes (important ones):

1. employee 
- employee_id
- name
- role
- user_id
- password (encrypted)

2. shift
- shift_id
- shift
- clock_in_by

3. employee_shift
- employee_shift_id
- day
- shift_id
- employee_id
- outlet_location

4. attendance
- clock_in_id
- employee_shift_id
- clock_in_time
- clock_in_day (Monday, Tuesday... Sunday)
- clock_in_date (yyyy-mm-dd)
- outlet_location

5. customer
- customer_id
- name

6. products
- product_id
- product_name
- category_id
- selling_price

7. product_category
- category_id
- category_name
  
8. transaction ------> displays only general information of transaction, total is summed from transaction_items 
- transaction_id
- employee_id
- customer_id
- total_bill (calculated during payment)
- payment_method (determined during payment)
- transaction_date
- transaction_time
- outlet_location

9. transaction_items ------> displays items of a transaction, including their total
- transaction_item_id
- transaction_id
- product_id

10. transaction_inventory -----> map each transaction item to the inventory, updating their state (available --> transacting --> sold)
- transaction_inventory_id
- transaction_item_id
- inventory_id

11. inventory
- inventory_id
- product_id
- status
- outlet_location

Below are the project specifications:

Feature                          Function
1. Login / logout      
2. Attendance log    
3. Stock management
  - stock count                  during morning + evening shift
  - stock in                     key in received stock
  - stock out                    key in sent out stock
    
4. Sales
   - transaction                 start transaction
   - add items                   display updated transaction items
   - remove items                display updated transaction items
   - payment                     choose payment method

5. Search information
   - stock                       display stock number of a specific product by location
   - transaction                 filter transaction ---> display desired transaction (with items)
  
6. Edit information              edit payment method, customer name (not applicable for others, referential integrity enforced)
   
7. Storage system                database
8. Data load                     use of objects (whenever applicable)
9. GUI
10. Auto-email                   email daily transaction (.txt) to HQ before 10pm
  
11. Sales history         
    - 2 specific dates
    - sort                       by date, total bill, customer name

To create GUI, first identify the user experience of the system (i.e. how should users interact with it through buttons and input fields)
Frontend only handles user clicks and sends to backend. Don't worry about what happens in backend, only display what the user sees.
Send data in JSON format
Test GUI rendering in small chunks, like displaying a table with JSON data that you made up
