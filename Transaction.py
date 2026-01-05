import mysql.connector
from datetime import datetime 
from flask import Flask, request, jsonify

def get_current_time():
    now = datetime.now()
    return now.strftime("%H:%M:%S")

def get_current_date():
    today = datetime.now()
    return today.strftime("%Y-%m-%d")

def get_connection():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="...",
        database="store_management_system"
    )


app = Flask(__name__)


class current_txn():
    def __init__ (self, t_id):
        current_txn.transaction_id = t_id
    
    # 1. Start a transaction (employee logged in)
    def start_transaction(self, employee_id):
        conn = get_connection()
        cursor = conn.cursor()

        transaction_time = get_current_time()
        transaction_date = get_current_date()
        cursor.execute(
            "INSERT INTO transaction (employee_id, transaction_date, transaction_time) VALUES(%s,%s,%s);", (employee_id, transaction_date, transaction_time,)
        ) 
        transaction_id = cursor.lastrowid
        conn.close()
        return transaction_id
    
    def get_total(self):
        conn = get_connection(dictionary=True)
        cursor = conn.cursor()

        cursor.execute(
            """ SELECT SUM(total_price) AS total 
                FROM transaction_items
                WHERE transaction_id = %s;
            """, (self.transaction_id,)  
        )
        row = cursor.fetchone() 
        conn.close() 
    
        total = row['total'] or 0
        return total   
        
    def get_transaction_id(self):
        return self.transaction_id

# 1. Display current transaction (default mode)
@app.route('transaction/current-transaction', methods=['POST'])
def display_current_transaction():
    conn = get_connection()
    cursor = conn.cursor()
    data = request.get_json
    transaction_id = data['transaction_id']
    
    cursor.execute(
        """ SELECT 
                ti.transaction_item_id,
                ti.product_id,
                p.product_name, ti.quantity, ti.selling_price, ti.total_price
            FROM transaction_items ti 
            JOIN products p ON p.product_id = ti.product_id
            WHERE ti.transaction_id = %s; 
        """, (transaction_id, )
    )
    result = cursor.fetchall()
    cursor.close()
    return jsonify(result)


# 2. Adding items
@app.route('/add-item', methods=['POST'])
def add_item():
    conn = get_connection(dictionary=True)
    cursor = conn.cursor()

    data = request.get_json()
    transaction_id = data['transaction_id']
    inventory_id = data['inventory_id']
    product_id = data['product_id']

    try:
        # check if product already in transaction items (get ti_id)
        cursor.execute(""" SELECT transaction_item_id FROM transaction_items WHERE transaction_id = %s AND product_id = %s; """, (transaction_id, product_id))
        row = cursor.fetchone()
        ti_id = row['transaction_item_id'] if row else None 
        
        
        cursor.execute(""" SELECT selling_price FROM products WHERE product_id = %s; """, (product_id,))
        row = cursor.fetchone()
        selling_price = row['selling_price']
        
        if row:
            cursor.execute(""" UPDATE transaction_items 
                        SET quantity = quantity + 1, total_price = total_price + %s 
                        WHERE transaction_item_id = %s; """, (selling_price, ti_id,))
        else:
            cursor.execute(""" INSERT INTO transaction_items VALUES(%s, %s, %s, %s, %s); """, (transaction_id, product_id, 1, selling_price, selling_price))
            ti_id = cursor.lastrowid

        # update inventory
        cursor.execute(""" UPDATE inventory SET status = 'sold' WHERE inventory_id = %s; """, (inventory_id,))

        # add into transaction_inventory
        cursor.execute(""" INSERT INTO transaction_inventory VALUES (%s,%s); """, (ti_id, inventory_id,))

        conn.commit()
        display_current_transaction(transaction_id)

    except Exception as e:
        conn.rollback()
        raise e

# on click from a specific transaction_item
@app.route('/remove-items', method=['POST'])
def remove_items():
    conn = get_connection(dictionary = True)
    cursor = conn.cursor()
   
    data = request.get_json()
    transaction_id = data['transaction_id']
    ti_id = data['transaction_item_id']
    number_to_remove = data['number_to_remove']

    try:
        # update transaction_items
        cursor.execute(
            """ UPDATE transaction_items
                SET quantity = quantity-%s
                WHERE transaction_item_id = %s
                AND quantity >= %s;
            """, (number_to_remove, ti_id, number_to_remove)
        )

        # update transaction_inventory
        cursor.execute(""" CREATE TEMPORARY TABLE to_delete
            SELECT 
                ROW_NUMBER() OVER (order by tinv.transaction_inventory_id) AS row_num,
                tinv.transaction_inventory_id,
                tinv.transaction_item_id,
                i.inventory_id,
                p.product_name
            FROM transaction_inventory tinv
            JOIN inventory i ON i.inventory_id = tinv.inventory_id  
            JOIN products p ON i.product_id = p.product_id
            WHERE tinv.transaction_item_id = %s; 
        """, (ti_id,))

        cursor.execute("""
            SELECT inventory_id FROM transaction_inventory
            WHERE transaction_inventory_id IN(
                SELECT transaction_inventory_id FROM to_delete
                WHERE row_num BETWEEN 1 AND %s
            ); """, (number_to_remove))
        inventory_id = cursor.fetchall('inventory_id')      # list of tuples [(a,), (b,), (c,)]
        inventory_id = [row[0] for row in inventory_id]     # list [a, b, c]

        ## delete from transaction_inventory              
        cursor.execute(""" DELETE FROM transaction_inventory 
            WHERE transaction_inventory_id IN(
                SELECT transaction_inventory_id FROM to_delete
                WHERE row_num BETWEEN 1 AND %s );
        """, (number_to_remove))

        # update inventory
        placeholders = ','.join(['%s'] * len(inventory_id))
        cursor.execute(f" UPDATE inventory SET status = %s WHERE inventory_id in ({placeholders})", ("available", *inventory_id)) 

        conn.commit()
        display_current_transaction(transaction_id)

    except Exception as e:
        conn.rollback()
        raise e

# 3. Payment (done in class)
@app.route('/payment-method', methods = ['POST'])
def payment():
    conn = get_connection()
    cursor = conn.cursor()  
    data = request.get_json()

    total_bill = current.get_total()
    transaction_id = data['transaction_id']
    payment_method = data['payment_method']
    
    try:
        cursor.execute(
            """ UPDATE transaction 
                SET total_bill = %s, payment_method = %s
                WHERE transaction_id = %s;
            """, (total_bill, payment_method, transaction_id)
        )
        conn.commit()

    except Exception as e:
        conn.rollback()
        raise e
    
    conn.close()
    
"""
    paths used:
    1. transaction/current-transaction   [transaction_id FROM transaction_items]                              |   display transaction_items based on transaction_id
    2. transaction/payment-method        [transaction_id, payment_method FROM transaction]                    |   update payment_method fetched from frontend
    3. transaction/add-item              [inventory_id, product_id FROM inventory]                            |   add item into transaction_items, update inventory 
    3. transaction/remove-items          [transaction_item_id, number_to_remove FROM transaction_items]       |   remove n items from transaction_items
"""   

# Main program

transaction_id = current_txn.start_transaction(employee_id)
current = current_txn(transaction_id)

add_item()
