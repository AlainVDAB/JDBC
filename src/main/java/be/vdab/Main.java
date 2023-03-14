package be.vdab;

import be.vdab.repositories.LeverancierRepository;
import be.vdab.repositories.PlantRepository;
import be.vdab.repositories.SoortRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var repository = new PlantRepository();
        try {
            repository.findRodePlantenEnHunLeveranciers().forEach(System.out::println);
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

    }}