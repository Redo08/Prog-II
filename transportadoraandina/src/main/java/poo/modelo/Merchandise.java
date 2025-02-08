package poo.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

import org.json.JSONObject;

import poo.helpers.Utils;

public class Merchandise implements Service, Exportable {

    // Atributos
    private String id;
    private String content;
    private double width;
    private double height;
    private double lenght;
    private LocalDateTime dateOfArrival;
    private LocalDateTime dateOfDeparture;
    private String warehouse;
    private Client storer;

    // Constructor por defecto
    public Merchandise() {
        this("id", "content", 0, 0, 0, LocalDateTime.now(), LocalDateTime.now(), "warehouse", new Client());
    }

    // Constructor parametrizado
    public Merchandise(String id, String content, double width, double height, double lenght, LocalDateTime dateOfArrival, LocalDateTime dateOfDeparture, String warehouse, Client storer) {
        setId(id);
        setContent(content);
        setWidth(width);
        setHeight(height);
        setLenght(lenght);
        setDateOfArrival(dateOfArrival);
        setDateOfDeparture(dateOfDeparture);
        setWarehouse(warehouse);
        setStorer(storer);
    }

    // Constuctor Solo Id
    public Merchandise(String id) {
        this();
        setId(id);
    }

    // Constructor sin Id
    public Merchandise(String content, double width, double height, double lenght, LocalDateTime dateOfArrival, LocalDateTime dateOfDeparture, String warehouse, Client storer) {
        this(Utils.getRandomKey(5), content, width, height, lenght, dateOfArrival, dateOfDeparture, warehouse, storer);
    }

    // Constructor JSON       
    // Convertir un String JSON a un LocalDateTime
    // LocalDateTime.parse(json.getString(key))
    // Obtener un objeto Java a partir de un JSON
    // new Client(json.getJSONObject("client"))                           
    public Merchandise(JSONObject json) {
        this(
                json.getString("id"),
                json.getString("content"),
                json.getDouble("width"),
                json.getDouble("height"),
                json.getDouble("lenght"),
                LocalDateTime.parse(json.getString("dateOfArrival")),
                LocalDateTime.parse(json.getString("dateOfDeparture")),
                json.getString("warehouse"),
                new Client(json.getJSONObject("storer"))
        );
    }

    // Constructor copia
    public Merchandise(Merchandise m) {
        this(
                m.id,
                m.content,
                m.width,
                m.height,
                m.lenght,
                m.dateOfArrival,
                m.dateOfDeparture,
                m.warehouse,
                m.storer
        );
    }

    // Accesores y Mutadores
    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public final void setContent(String content) {
        this.content = content;
    }

    public double getWidth() {
        return width;
    }

    public final void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public final void setHeight(double height) {
        this.height = height;
    }

    public double getLenght() {
        return lenght;
    }

    public final void setLenght(double lenght) {
        this.lenght = lenght;
    }

    public LocalDateTime getDateOfArrival() {
        return dateOfArrival;
    }

    public final void setDateOfArrival(LocalDateTime dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public LocalDateTime getDateOfDeparture() {
        return dateOfDeparture;
    }

    public final void setDateOfDeparture(LocalDateTime dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public final void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Client getStorer() {
        return storer;
    }

    public final void setStorer(Client storer) {
        this.storer = storer;
    }
    
    public double getVolume(){
        return width*height*lenght;
    }

    // Override Methods
    @Override
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }
    // to getDays
    public double getDays() {
        Duration time = Duration.between(this.dateOfArrival, this.dateOfDeparture);
        // 86400 Es la cantidad de segundos en un día
        return time.getSeconds() / 86400;
        
    }

    @Override
    // Retorna el pago de bodegas, devuelve el precio dado por el numero de días multiplicado por el volumen.
    public double getPayment() {
        double volume = getVolume();

        double days = getDays();

        return days * (volume * 5000);
    }

    @Override
    public String toJSON() {
        return this.toJSONObject().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return this.id.equals(((Merchandise) obj).id);
    }

}
