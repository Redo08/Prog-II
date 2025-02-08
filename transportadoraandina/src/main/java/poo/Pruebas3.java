package poo;

import org.json.JSONObject;

import poo.modelo.Delivery;
import poo.modelo.Envelope;
import poo.modelo.Pack;

public class Pruebas3 {

    public static void main(String[] args) throws Exception {
        // ejemplo de Reflextion
        create(Pack.class, getJsonPaquete());
        create(Envelope.class, getJsonSobre());
    }

    private static void create(Class<? extends Delivery> subclase, JSONObject json) throws Exception {
        Delivery instancia = subclase.getConstructor(JSONObject.class).newInstance(json);
        System.out.println("Type: " + instancia.getClass().getSimpleName());
        System.out.println("NumGuide: " + instancia.getNumGuide());
        System.out.println("Content: " + instancia.getContent());
        System.out.println("Sender: " + instancia.getSender().getName());
        System.out.println("Addressee: " + instancia.getAddressee().getName());
        System.out.println();
    }

    private static JSONObject getJsonSobre() {
        JSONObject json = new JSONObject("""
                              {
                  "content": "Documentos notariales",
                  "numGuide": "1BECQX7N",
                  "weight": 0,
                  "isFragile": false,
                  "sender": {
                    "city": "Manizales",
                    "address": "Mercaldas La Sultana",
                    "id": "0F7SD",
                    "phoneNumber": "3115550002",
                    "name": "David Andrés García"
                  },
                  "value": 0,
                  "isCertified": false,
                  "addressee": {
                    "city": "Manizales",
                    "address": "Edificio del parque, piso 2, Ucaldas",
                    "id": "C0001",
                    "phoneNumber": "3115551234",
                    "name": "Carlos Cuesta Iglesias"
                  },
                  "status": [{
                    "deliveryStatus": "RECIBIDO",
                    "dateTime": "2024-10-13T18:37:45"
                  }]
                }
               """);
        return json;
    }

    private static JSONObject getJsonPaquete() {
       JSONObject json = new JSONObject("""
                {
                  "content": "Componentes eléctricos",
                  "numGuide": "AFOQJW4R",
                  "weight": 1500,
                  "isFragile": true,
                  "sender": {
                    "city": "Manizales",
                    "address": "Mercaldas La Sultana",
                    "id": "0F7SD",
                    "phoneNumber": "3115550002",
                    "name": "David Andrés García"
                  },
                  "value": 200000,
                  "addressee": {
                    "city": "Manizales",
                    "address": "Edificio del parque, piso 2, Ucaldas",
                    "id": "C0001",
                    "phoneNumber": "3115551234",
                    "name": "Carlos Cuesta Iglesias"
                  },
                  "deliveryStatus": [{
                    "status": "RECIBIDO",
                    "dateTime": "2024-10-08T19:29:44"
                  }]
                }
                """);
        return json;
    }

}
