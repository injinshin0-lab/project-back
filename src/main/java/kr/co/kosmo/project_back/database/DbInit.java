// 서버 실행시 DB 테이블 준비하는 클래스
package kr.co.kosmo.project_back.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.kosmo.project_back.database.table.EmailAuthTable;
import kr.co.kosmo.project_back.database.table.OrderTable;
import kr.co.kosmo.project_back.database.table.UserTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class DbInit {

    @Bean
    CommandLineRunner initDatabase( DataSource dataSource ) {
        return args -> {
            try ( Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement() ) {
                
                // 유저 테이블 생성
                stmt.execute( UserTable.CREATE );
                // 주문 테이블 생성
                stmt.execute( OrderTable.CREATE );
                // 이메일 인증 테이블 생성
                stmt.execute(EmailAuthTable.CREATE);

                System.out.println( "bg_user 테이블 생성 완료" );
                System.out.println( "bg_order 테이블 생성 완료" );
                System.out.println("email_auth 테이블 생성 완료");
            }
        };
    }
}
