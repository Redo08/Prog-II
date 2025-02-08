package poo.model;

public class PassengerWagon extends RailroadCar {

    // Atribute
    private int rows;
    private int seatsPerRow;

    // Constructor por defecto
    public PassengerWagon() {
        this("Undefined", "Undfined", 0, CouplerType.AUTOMATIC, VelocityType.B, 0, 0);
    }

    // Constructor parametrizado
    public PassengerWagon(String id, String producerName, int wheelAmount, CouplerType acoplatorType, VelocityType velocityType, int rows, int seatsPerRow) {
        super(id, producerName, wheelAmount, acoplatorType, velocityType);
        setRows(rows);
        setSeatsPerRow(seatsPerRow);
    }

    // Accesores y Mutadores
    public int getRows() {
        return rows;
    }

    public final void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public final void setSeatsPerRow(int seatsPerRow) {
        if (seatsPerRow < 2 || seatsPerRow > 5) {
            throw new IllegalArgumentException("La cantidad de asientos por fila no es la permitida");
        } else {
            this.seatsPerRow = seatsPerRow;
        }
    }

    public int getPassengerSeats() {
        return seatsPerRow * rows;
    }

}
