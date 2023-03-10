package be.vdab.repositories;

import java.sql.SQLException;

public class PlantRepository extends AbstractRepository{
    public int verhoogPrijzenMet10Procent() throws SQLException{
     var sql= """
             update planten
             set prijs= prijs*1.1
             """;
     try(var connection = super.getConnection();
     var statement = connection.prepareStatement(sql)){
         return statement.executeUpdate();
     }
    }
}
