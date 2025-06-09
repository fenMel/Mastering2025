package fr.esic.mastering.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class DateHelper {

    public static LocalDateTime heureAleatoireEntre8hEt18h() {
        LocalDate jour = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(0, 6)); // entre aujourd’hui et il y a 5 jours
        int heure = ThreadLocalRandom.current().nextInt(8, 19);  // entre 8h et 18h
        int minute = ThreadLocalRandom.current().nextInt(0, 60); // minutes aléatoires
        return LocalDateTime.of(jour, LocalTime.of(heure, minute));
    }
}
