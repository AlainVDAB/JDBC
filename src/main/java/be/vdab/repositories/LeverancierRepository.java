package be.vdab.repositories;

import be.vdab.domain.Leverancier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeverancierRepository extends AbstractRepository {
    public List<String> findAllNamen() throws SQLException {
        var sql = """
                select naam
                from leveranciers
                """;
        var namen = new ArrayList<String>();
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            var result = statement.executeQuery();
            while (result.next()) {
                namen.add(result.getString("naam"));
            }
            return namen;
        }
    }

    public int findAantal() throws SQLException {
        var sql = """
                select count(*) as aantal
                from leveranciers
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            var result = statement.executeQuery();
            result.next();
            return result.getInt("aantal");
        }

    }

    public List<Leverancier> findAll() throws SQLException {
        var leveranciers = new ArrayList<Leverancier>();
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            for (var result = statement.executeQuery(); result.next(); ) {
                leveranciers.add(naarLeverancier(result));

            }
            return leveranciers;

        }
    }

    private Leverancier naarLeverancier(ResultSet result) throws SQLException {
        return new Leverancier(result.getLong("id"), result.getString("naam"),
                result.getString("adres"), result.getInt("postcode"),
                result.getString("woonplaats"), result.getObject("sinds", LocalDate.class));

    }

    public List<Leverancier> findByWoonplaats(String woonplaats) throws SQLException {
        var leveranciers = new ArrayList<Leverancier>();
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                where woonplaats = ?
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, woonplaats);
            for (var result = statement.executeQuery(); result.next(); ) {
                leveranciers.add(naarLeverancier(result));
            }
            return leveranciers;
        }
    }

    public Optional<Leverancier> findById(long id) throws SQLException {
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                where id = ?
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            return result.next() ? Optional.of(naarLeverancier(result))
                    : Optional.empty();
        }
    }

    public List<String> findNamenByWoord(String woord) throws SQLException {
        var namen = new ArrayList<String>();
        try (var connection = super.getConnection();
             var statement =
                     connection.prepareCall("{call PlantNamenMetEenWoord(?)}")) {
            statement.setString(1, '%' + woord + '%');
            for (var result = statement.executeQuery(); result.next(); ) {
                namen.add(result.getString("naam"));
            }
            return namen;
        }
    }

    public List<Leverancier> findBySinds2000() throws SQLException {
        var leveranciers = new ArrayList<Leverancier>();
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                where sinds >= {d '2000-01-01'}
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (var result = statement.executeQuery(); result.next(); ) {
                leveranciers.add(naarLeverancier(result));
            }
            connection.commit();
            return leveranciers;
        }
    }

    public List<Leverancier> findBySindsVanaf(LocalDate datum) throws SQLException {
        var leveranciers = new ArrayList<Leverancier>();
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                where sinds >= ?
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            statement.setObject(1, datum);
            for (var result = statement.executeQuery(); result.next(); ) {
                leveranciers.add(naarLeverancier(result));
            }
            connection.commit();
            return leveranciers;
        }
    }

    public List<Leverancier> findLeverancierGewordenInHetJaar2000() throws SQLException {
        var leveranciers = new ArrayList<Leverancier>();
        var sql = """
                select id, naam, adres, postcode, woonplaats, sinds
                from leveranciers
                where {fn year(sinds)} = 2000
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (var result = statement.executeQuery(); result.next(); ) {
                leveranciers.add(naarLeverancier(result));
            }
            connection.commit();
            return leveranciers;
        }
    }

}
