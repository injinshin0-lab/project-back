package kr.co.kosmo.project_back.database.table;

public class OrderTable {
    
    public static final String CREATE = """
        CREATE TABLE IF NOT EXISTS bg_order (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            address_id INTEGER NOT NULL,
            order_status TEXT NOT NULL,
            ordered_at DATETIME NOT NULL,
            payment_method TEXT NOT NULL,
            payment_status TEXT NOT NULL,
            payment_price INTEGER NOT NULL
        );
    """;
}
