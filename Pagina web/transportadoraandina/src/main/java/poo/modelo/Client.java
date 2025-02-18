 package poo.modelo;

import org.json.JSONObject;

import poo.helpers.Utils;

public class Client implements Exportable {

    //Atributos
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private String city;

    // Constructor por defecto
    public Client() {
        this("", "", "", "", "");
    }

    // Constructor parametrizado
    public Client(String id, String name, String address, String phoneNumber, String city) {
        setId(id);
        setName(name);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setCity(city);
    }

    // Más constructores
    // Constructor solo Id    MERCNACIAS ID,   ENVIOS NUMGUIA
    public Client(String id) {
        this();
        setId(id);
    }

    // Constructor sin Id
    public Client(String name, String address, String phoneNumber, String city) {
        this(Utils.getRandomKey(5), name, address, phoneNumber, city);
    }

    // Constructor JSON
    public Client(JSONObject json) {
        this(json.getString("id"), json.getString("name"), json.getString("address"), json.getString("phoneNumber"), json.getString("city"));
    }

    // Constructor copia
    public Client(Client c) {
        this(c.id, c.name, c.address, c.phoneNumber, c.city);
    }

    // Accesores y mutadores
    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public final void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public final void setCity(String city) {
        this.city = city;
    }

    // Override methods
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
        return this.id.equals(((Client) obj).id);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override   
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }
    
}
