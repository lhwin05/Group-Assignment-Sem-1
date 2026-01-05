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

# 3. Transaction & Cumulative_sales by date 
@app.route('display-transaction/by-date', methods=['POST'])
def display_transactions_by_date():
    conn = get_connection()
    cursor = conn.cursor()

    data = request.get_json()
    start_date = data.get('start_date')
    end_date = data.get('end_date')
    params = [start_date, end_date]

    # "ASC", "DESC"
    customer_name = data['customer_name']
    total_bill = data['total_bill']
    transaction_date = data['transaction_date']
    
    try:
        query = """
                SELECT t.transaction_id, e.employee_name, c.customer_name, t.total_bill, t.payment_method, t.transaction_date, t.transaction_time
                FROM transaction t
                JOIN customer c ON c.customer_id = t.customer_id
                JOIN employee e ON e.employee_id = t.employee_id 
                WHERE transaction_date BETWEEN %s AND %s 
                ORDER BY 1=1 """ 
    
        order_by = []
        if customer_name:
            order_by.append(f" c.customer_name {customer_name}")
        if total_bill:
            order_by.append(f" c.customer_name {customer_name}")
        if transaction_date:
            order_by.append(f" c.customer_name {customer_name}")
        if order_by:
            query += ', '.join(order_by)

        cursor.execute(query, tuple(params))
        result = get_result(cursor)
        return jsonify(result)

    except Exception as e:
        raise e
    finally:
        cursor.close()
    

@app.route('display-transaction/daily-cumulative-sales', methods=['POST'])
def display_daily_cumulative_sales():
    conn = get_connection()
    cursor = conn.cursor()

    data = request.get_json()
    start_date = data.get('start_date')
    end_date = data.get('end_date')

    try:
        cursor.execute(
            """ SELECT transaction_date, SUM(total_bill) as cumulative_sales FROM transaction 
                WHERE transaction_date BETWEEN %s AND %s
                GROUP BY transaction_date;
            """ , (start_date, end_date,))
        result = get_result(cursor)
        cursor.close()
        return jsonify(result)
    
    except Exception as e:
        raise e
    finally:
        cursor.close()

@app.route('display-transaction/cumulative-sales', methods=['POST'])
def display_cumulative_sales():
    conn = get_connection()
    cursor = conn.cursor()

    data = request.get_json()
    start_date = data.get('start_date')
    end_date = data.get('end_date')

    try:
        cursor.execute(
            """ SELECT SUM(total_bill) as cumulative_sales FROM transaction 
                WHERE transaction_date BETWEEN %s AND %s;
            """ , (start_date, end_date,))
        
        result = get_result(cursor)
        return jsonify(result)
    
    except Exception as e:
        raise e
    finally:
        cursor.close()



