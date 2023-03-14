package be.vdab.repositories;

import be.vdab.dto.PlantNaamEnLeveranciersNaam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlantRepository extends AbstractRepository {
    public int verhoogPrijzenMet10Procent() throws SQLException {
        var sql = """
                update planten
                set prijs= prijs*1.1
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate();
        }
    }

    public void verlaagPrijs(long id, BigDecimal nieuwePrijs) throws SQLException {
        var sqlSelect = """
                select prijs
                from planten
                where id = ?
                for update 
                """;
        try (var connection = super.getConnection();
             var statementSelect = connection.prepareStatement(sqlSelect)) {
            statementSelect.setLong(1, id);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            var result = statementSelect.executeQuery();
            if (result.next()) {
                var oudePrijs = result.getBigDecimal("prijs");
                var minimumNieuwePrijs = oudePrijs.divide(BigDecimal.valueOf(2),
                        2, RoundingMode.HALF_UP);
                if (nieuwePrijs.compareTo(minimumNieuwePrijs) >= 0) {
                    var sqlUpdate = """
                            update planten
                            set prijs = ?
                            where id = ?
                            """;

                    try (var statementUpdate = connection.prepareStatement(sqlUpdate)) {
                        statementUpdate.setBigDecimal(1, nieuwePrijs);
                        statementUpdate.setLong(2, id);
                        statementUpdate.executeUpdate();
                        connection.commit();
                        return;
                    }
                }
                connection.rollback();
                throw new IllegalArgumentException("Prijs te laag.");
            }
            connection.rollback();
            throw new IllegalArgumentException("Plant niet gevonden.");
        }
    }

    public List<String> findNamenByIds(Set<Long> ids) throws SQLException {
        if (ids.isEmpty()) {
            return List.of();
        }
        var namen = new ArrayList<String>();
        var sql = """ 
                select naam
                from planten
                where id in (
                """
                + "?,".repeat(ids.size() - 1)
                + "?)";
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            var index = 1;
            for (var id : ids) {
                statement.setLong(index++, id);
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (var result = statement.executeQuery(); result.next(); ) {
                namen.add(result.getString("naam"));
            }
            connection.commit();
            return namen;
        }
    }

    public List<PlantNaamEnLeveranciersNaam> findRodePlantenEnHunLeveranciers()
            throws SQLException {
        var list = new ArrayList<PlantNaamEnLeveranciersNaam>();
        var sql = """
                select planten.naam as plantnaam, leveranciers.naam as leveranciersnaam
                from planten inner join leveranciers
                on planten.leverancierid = leveranciers.id
                where kleur = 'rood'
                """;
        try (var connection = super.getConnection();
             var statement = connection.prepareStatement(sql)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (var result = statement.executeQuery(); result.next(); ) {
                list.add(new PlantNaamEnLeveranciersNaam(result.getString("plantnaam"),
                        result.getString("leveranciersnaam")));
            }
            connection.commit();
            return list;
        }
    }

}
