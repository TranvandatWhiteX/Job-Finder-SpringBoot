package com.dattran.job_finder_springboot.domain.runners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLRunner implements CommandLineRunner {
    private static final String CHECK_SCHEMA_VN = "SELECT EXISTS (SELECT 1 FROM pg_namespace WHERE nspname = 'vn');";
    private static final String CHECK_TABLE_EXISTS = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'vn' AND table_name = ?);";
    private static final String CHECK_TABLE_EMPTY = "SELECT NOT EXISTS (SELECT 1 FROM vn.%s LIMIT 1);";

    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        if (!checkSchemaExists()) {
            executeSQLFile("./sql/CreateTables_vn_units.sql");
        }
        if (allTablesExist() && allTablesEmpty()) {
            executeSQLFile("./sql/ImportData_vn_units.sql");
        }
    }

    private boolean checkSchemaExists() {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(CHECK_SCHEMA_VN, Boolean.class));
    }

    private boolean allTablesEmpty() {
        String[] tables = {"administrative_regions", "administrative_units", "districts", "provinces", "wards"};
        for (String table : tables) {
            String query = String.format(CHECK_TABLE_EMPTY, table);
            if (!Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class))) {
                return false;
            }
        }
        return true;
    }

    private boolean allTablesExist() {
        String[] tables = {"administrative_regions", "administrative_units", "districts", "provinces", "wards"};
        for (String table : tables) {
            if (!Boolean.TRUE.equals(jdbcTemplate.queryForObject(CHECK_TABLE_EXISTS, Boolean.class, table))) {
                return false;
            }
        }
        return true;
    }

    private void executeSQLFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        if (resource.exists()) {
            String sql = new String(Files.readAllBytes(resource.getFile().toPath()));
            jdbcTemplate.execute(sql);
            log.info("Executed SQL file: {}", filePath);
        } else {
            log.error("File not found: {}", filePath);
        }
    }
}

