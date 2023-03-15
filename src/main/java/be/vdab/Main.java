package be.vdab;

import be.vdab.repositories.LeverancierRepository;
import be.vdab.repositories.PlantRepository;
import be.vdab.repositories.SoortRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var namen = new ArrayList<String>();
        var scanner = new Scanner(System.in);
        System.out.print("Naam (stop met STOP):");
        for (String naam; ! "STOP".equals(naam = scanner.nextLine()) ;) {
            namen.add(naam);
        }
        var repository = new SoortRepository();
        try {
            repository.create(namen);
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

    }}