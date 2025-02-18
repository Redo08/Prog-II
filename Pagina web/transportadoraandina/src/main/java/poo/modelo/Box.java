package poo.modelo;

import java.util.ArrayList;

import org.json.JSONObject;


public class Box extends Delivery {

    // Atributos
    private double width;
    private double height;
    private double lenght;

    // Constructor por defecto
    public Box() {
        super();
    }

    // Constructor parametrizado
    public Box(String numGuide, double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status, double width, double height, double lenght) {
        super(numGuide, weight, isFragile, content, value, sender, addressee, new ArrayList<>());
        setWidth(width);
        setHeight(height);
        setLenght(lenght);
    }

    // Constructor numGuide
    public Box(String numGuide) {
        super(numGuide);
    }

    // Constructor sin numGuide
    public Box(double weight, boolean isFragile, String content, double value, Client sender, Client addressee, ArrayList<Status> status, double width, double height, double lenght) {
        super(weight, isFragile, content, value, sender, addressee, status);
        setWidth(width);
        setHeight(height);
        setLenght(lenght);
    }

    // Constructor JSON
    public Box(JSONObject json) {
        super(json);
        setWidth(json.getDouble("width"));
        setHeight(json.getDouble("height"));
        setLenght(json.getDouble("lenght"));
        // 1 - Asginar los "estados" en una instancia de JSONArray, llamada jsonEstados
        // 2 - Recorrer el jsonEstados del paso 1 y en cada pasada asignar a this.estados 
        // lo que devuelva jsonEstados.getJSONObject(i) siendo i el indice del for pero convertido a una instancia de estado
    }

    // Constructor Copia
    public Box(Box b) {
        super(b);
        setWidth(b.width);
        setHeight(b.height);
        setLenght(b.lenght);
    }

    // Accesores y Mutadores
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

    public double getVolume() {
        return height * lenght * width;
    }

    // Override methods
    @Override
    public double getPayment() {
        double volume = getVolume();
        double constantPrice = 500 * weight;
        if (volume <= 0) {
            throw new IllegalArgumentException("El volumen no puede ser negativo ni 0");
        }
        if (volume <= 0.5) {
            return 10000 + constantPrice;
        } else if (volume <= 1) {
            return 12000 + constantPrice;
        } else if (volume <= 3) {
            return 15000 + constantPrice;
        } else if (volume <= 6) {
            return 25000 + constantPrice;
        } else if (volume <= 10) {
            return 30000 + constantPrice;
        } else {
            return 10000 * (volume / 10) + constantPrice;
        }
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
        return this.numGuide.equals(((Box) obj).numGuide);
    }

}
