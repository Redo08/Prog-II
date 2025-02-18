package poo.services;

import org.json.JSONObject;

import poo.helpers.Utils;
import poo.modelo.Client;
import poo.modelo.Delivery;

public class BoxService extends DeliveryService {

    public BoxService(Class<? extends Delivery> subclass, Service<Client> clients) throws Exception {
        super(subclass, clients);
    }
    @Override
    public JSONObject add(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        if (json.has("width")) {
            Utils.doubleOk("width", 0.1, 2.44, json);
        }
        if (json.has("height")) {
            Utils.doubleOk("height", 0.1, 2.59, json);
        }
        if (json.has("lenght")) {
            Utils.doubleOk("lenght", 0.1, 12.19, json);
        }

        return super.add(json.toString());
    }
}
