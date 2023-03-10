package be.vdab;

import be.vdab.repositories.PlantRepository;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        var repository = new PlantRepository();
        try {
            System.out.print(repository.verhoogPrijzenMet10Procent());
            System.out.println(" planten aangepast.");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

}