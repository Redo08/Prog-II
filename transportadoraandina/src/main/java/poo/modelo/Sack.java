package poo.modelo;

import java.util.ArrayList;

import org.json.JSONObject;

public class Sack extends Delivery {

    // Constructor por defecto
    public Sack() {
        super();
    }

    // Constructor parametrizado
    public Sack(String numGuide, double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status) {
        super(numGuide, weight, isFragile, content, value, sender, addressee, status);
    }

    // Constructor solo numGuide
    public Sack(String numGuide) {
        super(numGuide);
    }

    // Constructor sin NumGuide
    public Sack(double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status) {
        super(weight, isFragile, content, value, sender, addressee, status);
    }

    // Constructor JSON
    public Sack(JSONObject json) {
        super(json);
        // 1 - Asginar los "estados" en una instancia de JSONArray, llamada jsonEstados
        // 2 - Recorrer el jsonEstados del paso 1 y en cada pasada asignar a this.estados 
        // lo que devuelva jsonEstados.getJSONObject(i) siendo i el indice del for pero convertido a una instancia de estado
    }

    // Constructor Copia
    public Sack(Sack s) {
        super(s);
    }

    // Override Methods
    @Override
    public double getPayment() {
        return 1000 * weight;
    }

    @Override
    public String toJSON() {
        return this.toJSONObject().toString();
    }

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
        return this.numGuide.equals(((Sack) obj).numGuide);
    }
}
