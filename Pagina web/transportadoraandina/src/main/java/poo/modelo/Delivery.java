package poo.modelo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import poo.helpers.Utils;

public abstract class Delivery implements Service, Exportable {

    // Atributos
    protected String numGuide;
    protected double weight;
    protected boolean isFragile;
    protected String content;
    protected double value;
    protected Client sender;
    protected Client addressee;
    protected ArrayList<Status> status;
    protected String type;

    // Constructor por defecto
    public Delivery() {
        this("numGuide", 0, true, "content", 0, new Client(), new Client(), new ArrayList<>());
    }

    // Constructor parametrizado
    public Delivery(String numGuide, double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status) {
        setNumGuide(numGuide);
        setWeight(weight);
        setIsFragile(isFragile);
        setContent(content);
        setValue(value);
        setSender(sender);
        setAddressee(addressee);
        setStatus(status);
    }

    // Constructor solo NumGuide
    public Delivery(String numGuide) {
        this();
        setNumGuide(numGuide);
    }

    // Constructor sin NumGuide
    public Delivery(double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status) {
        this(Utils.getRandomKey(5), weight, isFragile, content, value, sender, addressee, status);
    }

    // Constructor JSON
    public Delivery(JSONObject json) {
        this(
                json.getString("numGuide"),
                json.getDouble("weight"),
                json.getBoolean("isFragile"),
                json.getString("content"),
                json.getDouble("value"),
                new Client(json.getJSONObject("sender")),
                new Client(json.getJSONObject("addressee")),
                new ArrayList<>()
        );
        JSONArray jsonEstados = json.getJSONArray("status");
        for (int i = 0; i < jsonEstados.length(); i++) {
            this.status.add(new Status(jsonEstados.getJSONObject(i)));
        }
        // 1 - Asginar los "estados" en una instancia de JSONArray, llamada jsonEstados
        // 2 - Recorrer el jsonEstados del paso 1 y en cada pasada asignar a this.estados 
        // lo que devuelva jsonEstados.getJSONObject(i) siendo i el indice del for pero convertido a una instancia de estado
    }

    // Constructor copia
    public Delivery(Delivery d) {
        this(d.numGuide, d.weight, d.isFragile, d.content, d.value, d.sender, d.addressee, d.status);
    }

    // Accesores y mutadores
    public ArrayList<Status> getStatus() {
        return status;
    }

    public final void setStatus(ArrayList<Status> status) {
        this.status = status;
    }

    public String getNumGuide() {
        return numGuide;
    }

    public final void setNumGuide(String numGuide) {
        this.numGuide = numGuide;
    }

    public double getWeight() {
        return weight;
    }

    public final void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean getIsFragile() {
        return isFragile;
    }

    public final void setIsFragile(boolean isFragile) {
        this.isFragile = isFragile; 
    }

    public String getContent() {
        return content;
    }

    public final void setContent(String content) {
        this.content = content;
    }

    public double getValue() {
        return value;
    }

    public final void setValue(double value) {
        this.value = value;
    }

    public Client getSender() {
        return sender;
    }

    public final void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getAddressee() {
        return addressee;
    }

    public final void setAddressee(Client addressee) {
        this.addressee = addressee;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    // Methods
    public String getId() {
        return numGuide;
    }

    // Override Methods
    @Override
    public JSONObject toJSONObject() {
        return new JSONObject(this);
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
        return this.numGuide.equals(((Delivery) obj).numGuide);
    }

    @Override
    public double getPayment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPayment'");
    }

    @Override
    public String toJSON() {
        return this.toJSONObject().toString();
    }

}
