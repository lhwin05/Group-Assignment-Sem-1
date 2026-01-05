import mysql.connector
from flask import Flask, request, jsonify

app = Flask(__name__)

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

@app.route('/display-stock/by-product', methods=['POST'])
def stock_count_by_product():
    conn = get_connection()
    cursor = conn.cursor()
    data = request.get_json()
    product_id = data['product_id']

    try:
        cursor.execute(""" SELECT 
            i.product_id,
            p.product_name,
            i.outlet_location,
            COUNT(*) AS available_stock
        FROM inventory i
        JOIN products p ON p.product_id = i.product_id
        WHERE STATUS = "available" AND i.product_id = %s
        GROUP BY product_id, outlet_location; """, (product_id,))

        result = get_result(cursor)
        return jsonify(result) 
    
    except Exception as e:
        raise e





