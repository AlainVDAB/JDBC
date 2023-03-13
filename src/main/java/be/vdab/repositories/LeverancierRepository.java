package be.vdab.repositories;

import be.vdab.domain.Leverancier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
