package kr.co.kosmo.project_back.database.table;

public class UserTable {

    public static final String CREATE = """
        CREATE TABLE IF NOT EXISTS bg_user (
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            login_id TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            name TEXT NOT NULL,
            phone TEXT NOT NULL,
            email TEXT NOT NULL,
            role TEXT NOT NULL DEFAULT 'USER',
            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at DATETIME,
            last_login_at DATETIME
        );
    """;
}

// AutoIncrement = 삭제된 id를 재사용하지 않게 하기 위해서, 동시 가입을 위함.
// Unique = 같은 id가 2개가 생기는 것을 방지.
// role = 기본 값은 일반 사용자로 지정.
// 회원가입 시간 = 값이 없을 경우, 자동으로 현재 시간으로 저장