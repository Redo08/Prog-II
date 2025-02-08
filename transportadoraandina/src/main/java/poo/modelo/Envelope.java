package poo.modelo;

import java.util.ArrayList;

import org.json.JSONObject;

public class Envelope extends Delivery {

    // Atributos
    private boolean isCertified;
    // private double smlv;

    // Constructor por defecto
    public Envelope() {
        super();
    }

    // Constructor parametrizado
    public Envelope(String numGuide, double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status, boolean isCertified) {
        super(numGuide, 0, isFragile, content, value, sender, addressee, status);
        setIsCertified(isCertified);
    }

    // Constructor solo numGuide
    public Envelope(String numGuide) {
        super(numGuide);
    }

    // Constructor sin numGuide
    public Envelope(double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status, boolean isCertified) {
        super(weight, isFragile, content, value, sender, addressee, status);
        setIsCertified(isCertified);
    }

    // Constructor JSON
    public Envelope(JSONObject json) {
        super(json);
        setIsCertified(json.getBoolean("isCertified"));
    }

    // Constructor copia
    public Envelope(Envelope e) {
        super(e);
        setIsCertified(e.isCertified);
    }

    // Accesores y Mutadores
    public boolean getIsCertified() {
        return isCertified;
    }

    public final void setIsCertified(boolean isCertified) {
        this.isCertified = isCertified;
    }

    // Aqui hubo un intento pensado en que como el salario minimo es fluctuante, uno pueda manipular el valor
    // Y cambiarlo para su uso posterior.
   /*  public double getSmlv() {
        return smlv;
    }

    public final void setSmlv(double smlv) {
        this.smlv = smlv;
    } */

    // Override Methods
    @Override
    public double getPayment() {
        int price;
        price = (int) (1_300_000 / 1000) * 2;
        if (isCertified) {
            return (int) price + (price * 0.1);
        } else {
            return price;
        }
        // Math.ceil es la función ceiling, que acerca al entero más cercano
        // return Math.ceil((smlv / 1000) * 2);
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
        return this.numGuide.equals(((Envelope) obj).numGuide);
    }


}
