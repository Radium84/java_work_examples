package realwork;

import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

/**
 * Функционал метода translateData использовался для трансляции данных из одной бд в другую,
 * на тестовый фреймворк не аффектит. Пример батчевой записи.
 */
public class DataTranslator {

    public void translateData() {
        String h2Url = "jdbc:h2:C:\\Users\\20729510\\idmtests\\easupDATA;MV_STORE=false";
        String h2User = "sa";
        String h2Password = "sa";

        String pgUrl = "jdbc:postgresql://10.55.19.58:5433/sidecar";
        String pgUser = "pgautotest";
        String pgPassword = "test";

        try (Connection h2Conn = DriverManager.getConnection(h2Url, h2User, h2Password);
             Connection pgConn = DriverManager.getConnection(pgUrl, pgUser, pgPassword)) {

            // Получаем все таблицы в базе данных H2
            DatabaseMetaData h2MetaData = h2Conn.getMetaData();
            ResultSet h2Tables = h2MetaData.getTables(null, null, "GROU%", new String[]{"TABLE"});

            while (h2Tables.next()) {
                String tableName = h2Tables.getString("TABLE_NAME");
                System.out.println("Transferring table: " + tableName);

                // Получаем данные из таблицы H2
                Statement h2Statement = h2Conn.createStatement();
                ResultSet h2ResultSet = h2Statement.executeQuery("SELECT * FROM PUBLIC." + tableName);

                // Получаем метаданные для формирования запроса на вставку в PostgreSQL
                ResultSetMetaData resultSetMetaData = h2ResultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                StringBuilder insertQuery = new StringBuilder();
                insertQuery.append("INSERT INTO fullname.").append(tableName).append(" VALUES (");

                for (int i = 1; i <= columnCount; i++) {
                    insertQuery.append("?");
                    if (i < columnCount) insertQuery.append(",");
                }
                insertQuery.append(")");

                // Подготавливаем вставку данных в PostgreSQL
                PreparedStatement pgPrepStatement = pgConn.prepareStatement(insertQuery.toString());
                pgConn.setAutoCommit(false); // Отключаем автокоммит для батчинга

                while (h2ResultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        pgPrepStatement.setObject(i, h2ResultSet.getObject(i));
                    }
                    pgPrepStatement.addBatch();
                }

                // Выполняем батчинг в PostgreSQL
                pgPrepStatement.executeBatch();
                pgConn.commit();

                // Закрываем ресурсы для текущей таблицы
                pgPrepStatement.close();
                h2ResultSet.close();
                h2Statement.close();
            }

            h2Tables.close();
            System.out.println("Data transfer completed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}