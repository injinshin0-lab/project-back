// package kr.co.kosmo.project_back.config;

// import com.zaxxer.hikari.HikariConfig;
// import com.zaxxer.hikari.HikariDataSource;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.sqlite.SQLiteConfig;

// import javax.sql.DataSource;
// import java.sql.Connection;
// import java.sql.SQLException;
// import java.sql.Statement;

// @Configuration
// public class DataSourceConfig {
//     private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

//     @Value("${spring.datasource.url}")
//     private String jdbcUrl;

//     /**
//      * SQLite 전용 DataSource 구성
//      * 각 연결마다 PRAGMA 설정을 적용
//      */
//     @Bean
//     @Primary
//     public DataSource dataSource(DataSourceProperties properties) {
//         // SQLite 설정
//         SQLiteConfig config = new SQLiteConfig();
//         config.setJournalMode(SQLiteConfig.JournalMode.WAL);
//         config.setBusyTimeout(30000); // 30초 타임아웃
//         config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
//         config.setLockingMode(SQLiteConfig.LockingMode.NORMAL);
//         config.enforceForeignKeys(true);
//         config.setCacheSize(-64000); // 64MB 캐시
        
//         // URL에서 파라미터 제거 (이미 config에 설정됨)
//         String cleanUrl = jdbcUrl.split("\\?")[0];
        
//         // HikariCP 설정
//         HikariConfig hikariConfig = new HikariConfig();
//         hikariConfig.setJdbcUrl(cleanUrl);
//         // SQLite 설정을 Properties로 전달
//         config.toProperties().forEach((key, value) -> {
//             String keyStr = String.valueOf(key);
//             String valueStr = String.valueOf(value);
//             if (keyStr.startsWith("journal_mode") || keyStr.startsWith("busy_timeout") || 
//                 keyStr.startsWith("synchronous") || keyStr.startsWith("locking_mode") ||
//                 keyStr.startsWith("foreign_keys") || keyStr.startsWith("cache_size")) {
//                 hikariConfig.addDataSourceProperty(keyStr, valueStr);
//             }
//         });
//         hikariConfig.setDriverClassName(properties.getDriverClassName());
//         hikariConfig.setMaximumPoolSize(1); // SQLite는 단일 연결만
//         hikariConfig.setMinimumIdle(1);
//         hikariConfig.setConnectionTimeout(30000); // 30초
//         hikariConfig.setMaxLifetime(600000); // 10분
//         hikariConfig.setIdleTimeout(300000); // 5분
//         hikariConfig.setLeakDetectionThreshold(60000); // 1분
//         hikariConfig.setAutoCommit(true);
        
//         // 연결 초기화 SQL 설정
//         hikariConfig.setConnectionInitSql(
//             "PRAGMA journal_mode=WAL; " +
//             "PRAGMA busy_timeout=30000; " +
//             "PRAGMA synchronous=NORMAL; " +
//             "PRAGMA foreign_keys=ON; " +
//             "PRAGMA cache_size=-64000;"
//         );
        
//         HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        
//         // 초기 연결 테스트
//         try (Connection conn = dataSource.getConnection();
//              Statement stmt = conn.createStatement()) {
//             stmt.execute("PRAGMA journal_mode=WAL;");
//             log.info("SQLite 데이터베이스 초기화 완료 (WAL 모드 활성화)");
//         } catch (SQLException e) {
//             log.error("데이터베이스 초기화 실패: ", e);
//         }
        
//         return dataSource;
//     }
// }

