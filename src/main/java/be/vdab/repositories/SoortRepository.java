package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SoortRepository extends AbstractRepository {
    public long create(String naam) throws SQLException {
        var sql = """
                insert into soorten(naam)
                values (?)
                """;
        try (var connection = super.getConnection();
             var statementInsert = connection.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statementInsert.setString(1, naam);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try {
                statementInsert.executeUpdate();
                var result = statementInsert.getGeneratedKeys();
                result.next();
                var nieuweId = result.getLong(1);
                connection.commit();
                return nieuweId;
            } catch (SQLException ex) {
                var sqlSelect = """ 
                        select id
                        from soorten
                        where naam = ?
                        """;
                try (var statementSelect = connection.prepareStatement(sqlSelect)) {
                    statementSelect.setString(1, naam);
                    if (statementSelect.executeQuery().next()) {
                        connection.rollback();
                        throw new IllegalArgumentException("Soort bestaat al.");
                    }
                    connection.rollback();
                    throw ex;
                }
            }
        }
    }
}