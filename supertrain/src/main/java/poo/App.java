package poo;

import java.util.ArrayList;

import poo.model.*;

public class App {

    private static ArrayList<Train> trains; // Variable global

    public static void main(String[] args) {
        // Creación instancias de tren
        trains = new ArrayList<>();
        Train t1 = new Train();
        t1.setId("1000");
        t1.getRailVehicle().add(new Locomotive("999", "Camilo", 8, CouplerType.AUTOMATIC, MotorType.DIESEL));
        t1.getRailVehicle().add(new GoodWagon("1111", "Juan", 7, CouplerType.AUTOMATIC, VelocityType.A, ComercialGood.ABIERTO));
        t1.getRailVehicle().add(new PassengerWagon("1113", "Mario", 10, CouplerType.AUTOMATIC, VelocityType.B, 15, 4));
        trains.add(t1);

        Train t2 = new Train();
        t2.setId("1001");
        t2.getRailVehicle().add(new Locomotive("1999", "Cam", 9, CouplerType.SEMIPERMANENT, MotorType.ELECTRIC));
        t2.getRailVehicle().add(new GoodWagon("1211", "Juda", 10, CouplerType.MANUAL, VelocityType.D, ComercialGood.TOLVA));
        t2.getRailVehicle().add(new PassengerWagon("33232", "Caiman", 12, CouplerType.MANUAL, VelocityType.N, 12, 3));
        trains.add(t2);

        Train t3 = new Train();
        t3.setId("1002");
        t3.getRailVehicle().add(new Locomotive("12345", "Bombardier", 16, CouplerType.AUTOMATIC, MotorType.DIESEL));
        t3.getRailVehicle().add(new GoodWagon("54321", "GE", 8, CouplerType.MANUAL, null, ComercialGood.JAULA));
        t3.getRailVehicle().add(new PassengerWagon("0986", "Toyota", 8, CouplerType.SEMIPERMANENT, VelocityType.N, 8, 5));
        t3.getRailVehicle().add(new PassengerWagon("75768", "Jimin", 6, CouplerType.SEMIPERMANENT, VelocityType.A, 12,4));
        trains.add(t3);

        Train t4 = new Train();
        t4.setId("1003");
        t4.getRailVehicle().add(new Locomotive("54321", "GE", 16, CouplerType.AUTOMATIC, MotorType.ELECTRIC));
        t4.getRailVehicle().add(new GoodWagon("12345", "Bombardier", 8, CouplerType.MANUAL, VelocityType.B, ComercialGood.CISTERNA));
        t4.getRailVehicle().add(new GoodWagon("923k", "Stadler", 10, CouplerType.MANUAL, VelocityType.D, ComercialGood.CISTERNA));
        t4.getRailVehicle().add(new Locomotive("63545", "Bombardier", 12, CouplerType.AUTOMATIC, MotorType.ELECTRIC));
        t4.getRailVehicle().add(new GoodWagon("j876", "Aistom", 8, CouplerType.SEMIPERMANENT, VelocityType.A, ComercialGood.JAULA));
        trains.add(t4);

        Train t5 = new Train();
        t5.setId("1004");
        t5.getRailVehicle().add(new Locomotive("343433", "JA", 12, CouplerType.MANUAL, MotorType.ELECTRIC));
        t5.getRailVehicle().add(new Locomotive("343444", "JE", 14, CouplerType.AUTOMATIC, MotorType.ELECTRIC));
        t5.getRailVehicle().add(new GoodWagon("34355", "Bombardero", 10, CouplerType.AUTOMATIC, VelocityType.A, ComercialGood.ABIERTO));
        t5.getRailVehicle().add(new GoodWagon("9647k", "Stadio", 7, CouplerType.SEMIPERMANENT, VelocityType.B, ComercialGood.CISTERNA));
        t5.getRailVehicle().add(new Locomotive("12356", "Bambi", 20, CouplerType.SEMIPERMANENT, MotorType.ELECTRIC));
        t5.getRailVehicle().add(new GoodWagon("h47657", "Aston Villa", 15, CouplerType.AUTOMATIC, VelocityType.D, ComercialGood.JAULA));
        t5.getRailVehicle().add(new PassengerWagon("h4337", "Aston mina", 17, CouplerType.MANUAL, VelocityType.N, 12,4));
        trains.add(t5);

        Train t6 = new Train();
        t6.setId("1005");
        t6.getRailVehicle().add(new Locomotive("123456", "HI", 10, CouplerType.SEMIPERMANENT, MotorType.ELECTRIC));
        t6.getRailVehicle().add(new Locomotive("654321", "PI", 12, CouplerType.MANUAL, MotorType.ELECTRIC));
        t6.getRailVehicle().add(new GoodWagon("09876", "Camionero", 14, CouplerType.MANUAL, VelocityType.N, ComercialGood.CISTERNA));
        t6.getRailVehicle().add(new GoodWagon("6789k", "Programador", 8, CouplerType.AUTOMATIC, VelocityType.B, ComercialGood.CISTERNA));
        t6.getRailVehicle().add(new Locomotive("76767", "Matematico", 16, CouplerType.AUTOMATIC, MotorType.ELECTRIC));
        t6.getRailVehicle().add(new GoodWagon("h43454", "Volador", 18, CouplerType.MANUAL, VelocityType.D, ComercialGood.JAULA));
        // t6.getRailVehicle().add(new PassengerWagon("75768", "Jimin", 6, CouplerType.SEMIPERMANENT, VelocityType.A, 12,4));
        trains.add(t6);

        // Impresión instancias de tren
        int count = 0;
        System.out.println("Info de todos los trenes: ");
        for (Train train : trains) {
            System.out.println(train);
            count++;
        }
        System.out.println("-".repeat(50));
        System.out.println("Cantidad total de trenes: " + count);
        System.out.println("-".repeat(50));
        
        // Impresión Instancias filtradas
        getFilterTrain();
    }
    // Función de los trenes que aplican con las restricciones
    public static ArrayList<Train> getFilterTrain(){
        ArrayList<Train> listRestrictions = new ArrayList<>();
        int count = 0;
        System.out.println("Info de los trenes que aplican el filtro: ");
        for (Train x : trains) {
            if(x.trainList()){
                System.out.println(x);
                listRestrictions.add(x);
                count++;
            }
        }
        System.out.println("-".repeat(50));
        System.out.println("Cantidad de trenes que cumplen: " + count);
        System.out.println("-".repeat(50));
        return listRestrictions;
    }
}
