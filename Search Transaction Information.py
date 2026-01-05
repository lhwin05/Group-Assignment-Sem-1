import mysql.connector
from flask import Flask, request, jsonify

def get_connection():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="Lhw@5@11@",
        database="store_management_system"
    )

def get_result(cursor):
    rows = cursor.fetchall()
    columns = [desc[0] for desc in cursor.description]
    result = [dict(zip(columns, row)) for row in rows]
    return result

app = Flask(__name__)

# 1. Search by filter 
@app.route('/display-transaction/filter', methods=['POST'])
def filter_transaction():
    conn = get_connection()
    cursor = conn.cursor()

    data = request.get_json()
    product_id = data.get('product_id')
    customer_name = data.get('customer_name')
    start_date = data.get('start_date')
    end_date = data.get('end_date')

    # search name
    try:
        query = """ SELECT 
            t.transaction_id, e.employee_name, c.customer_name,
            p.product_name, ti.quantity, ti.total_price, t.payment_method,
            t.transaction_date, t.transaction_time
        FROM transaction t
        JOIN transaction_items ti ON ti.transaction_id = t.transaction_id
        JOIN products p ON p.product_id = ti.product_id
        JOIN customer c ON t.customer_id = c.customer_id
        JOIN employee e ON e.employee_id = t.employee_id
        WHERE 1=1"""
        
        params = []
        if product_id:
            query += " AND p.product_id = %s"
            params.append(product_id)

        if customer_name:
            query += " AND c.customer_name ILIKE %s"
            params.append(f"%{customer_name}%")  

        if start_date and end_date:
            query += " AND t.transaction_date BETWEEN %s AND %s"
            params.extend([start_date, end_date])

        cursor.execute(query, tuple(params))
        result = get_result(cursor)
        cursor.close()
        return jsonify(result)
    
    except Exception as e:
        raise e
    
    finally:
        conn.close()
    
# 2. Click into a specific transaction (to view transaction_items) 
@app.route('/display-transaction/by-transaction-id', methods=['POST'])
def display_transaction_by_id():        
    conn = get_connection()
    cursor = conn.cursor()

    data = request.get_json()
    transaction_id = data['transaction_id']

    try:
        cursor.execute("""
        -- slightly modified from display_current_transaction
            
        SELECT 
            t.transaction_id, e.employee_name, c.customer_name,
            p.product_name, ti.quantity, ti.selling_price, ti.total_price,
            t.payment_method,
            t.transaction_date,
            t.transaction_time
        FROM transaction t 
        JOIN transaction_items ti ON t.transaction_id = ti.transaction_id
        JOIN customer c on c.customer_id = t.customer_id
        JOIN employee e ON e.employee_id = t.employee_id
        JOIN products p ON p.product_id = ti.product_id 
        WHERE t.transaction_id = %s; """,(transaction_id,)) 

        result = get_result(cursor)
        cursor.close()
        return jsonify(result)
    
    except Exception as e:
        raise e
    
    finally:
        conn.close()
