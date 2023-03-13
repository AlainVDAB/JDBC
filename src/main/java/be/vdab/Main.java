package be.vdab;

import be.vdab.repositories.LeverancierRepository;
import be.vdab.repositories.PlantRepository;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        var repository = new LeverancierRepository();
        try {

            repository.findAll().forEach(System.out::println);




        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

}