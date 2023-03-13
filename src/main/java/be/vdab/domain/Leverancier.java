package be.vdab.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Leverancier {
    private final long id;
    private final String naam;
    private final String adres;
    private final int postcode;
    private final String woonplaats;
    private final LocalDate sinds;
    public Leverancier(long id, String naam, String adres, int postcode,
                       String woonplaats, LocalDate sinds) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
        this.sinds = sinds;
    }

    long getAantalJaarLeverancier(){
        return ChronoUnit.YEARS.between(sinds,LocalDate.now());
    }
    @Override
    public String toString() {
        return id + ":" + naam + " (" + woonplaats + ") " + sinds+ ": " +
                getAantalJaarLeverancier() + " jaar";
    }


}
