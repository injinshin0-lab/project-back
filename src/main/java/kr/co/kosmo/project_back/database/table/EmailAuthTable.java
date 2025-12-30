package kr.co.kosmo.project_back.database.table;

public class EmailAuthTable {
    public static final String CREATE = """
        CREATE TABLE IF NOT EXISTS email_auth (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email TEXT NOT NULL,
        auth_code TEXT NOT NULL,
        expired_at DATETIME NOT NULL,
        verified INTEGER NOT NULL DEFAULT 0
        );
    """;
    
}
