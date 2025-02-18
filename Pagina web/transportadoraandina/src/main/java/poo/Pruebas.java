package poo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Pruebas {

    public static void main(String[] args) {
        // Intento de pasar de LocalDateTime a double
        LocalDateTime dateOfEntrance;
        LocalDateTime dateOFExit;
        // Da la fecha de hoy
        dateOfEntrance = LocalDateTime.now();
        // el LocalDateTime.of(,,,) sirve para poner una fecha exacta con tiempo
        dateOFExit = LocalDateTime.of(2023, 10, 6, 12, 30);

        System.out.println("First LocalDateTime (current): " + dateOfEntrance);
        System.out.println("Second LocalDateTime (specific): " + dateOFExit);

        // El Duration.between sirve para sacar el tiempo de duración, este despues puede ser pasado a segundos y despues a días
        Duration time = Duration.between(dateOFExit, dateOfEntrance);
        double days = time.getSeconds() / 86400;

        System.out.println(days);
    }

}
