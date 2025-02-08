package poo;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import poo.helpers.Controller;
import poo.helpers.Utils;
import poo.modelo.Box;
import poo.modelo.Client;
import poo.modelo.Delivery;
import poo.modelo.DeliveryStatus;
import poo.modelo.Envelope;
import poo.modelo.Merchandise;
import poo.modelo.Pack;
import poo.modelo.Sack;
import poo.services.BoxService;
import poo.services.ClientService;
import poo.services.DeliveryService;
import poo.services.EnvelopeService;
import poo.services.MerchandiseService;
import poo.services.Service;

public final class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        int port = 7070;
        // https://javalin.io/tutorials/javalin-logging#using-any-of-those-loggers
        String message = String.format(
                "%sIniciando la API Rest de Envios y bodegaje. Use Ctrl+C para detener la ejecución%s", Utils.CYAN,
                Utils.RESET);
        LOG.info(message);

        Utils.trace = true; // no deshabilite la traza de errores hasta terminar completamente la aplicación
        int length = args.length;
        if (length > 0) {
            Utils.trace = Boolean.parseBoolean(args[0]);
            if (length >= 2) {
                port = Integer.parseInt(args[1]);
            }
        }

        if (Utils.trace) {
            // ver para tiempo de desarrollo: ./.vscode/launch.json
            LOG.info(String.format("%sHabilitada la traza de errores%s", Utils.YELLOW, Utils.RESET));
        } else {
            LOG.info(String.format("%sEnvíe un argumento true|false para habilitar|deshabilitar la traza de errores%s",
                    Utils.YELLOW, Utils.RESET));
        }

        // esencial para estandarizar el formato monetario con separador de punto
        // decimal, no con coma
        Locale.setDefault(Locale.of("es_CO"));

        Service<Client> clientService = new ClientService();
        Service<Merchandise> merchandiseService = new MerchandiseService(clientService);
        Service<Delivery> packService = new DeliveryService(Pack.class, clientService);
        Service<Delivery> envelopeService = new EnvelopeService(Envelope.class, clientService); // *-*-*- OJO - *-*-*
        Service<Delivery> boxService = new BoxService(Box.class, clientService);
        Service<Delivery> sackService = new DeliveryService(Sack.class, clientService);
        // ...........................................

        Javalin
                .create(config -> {
                    config.http.defaultContentType = "application/json";
                    // ver https://javalin.io/plugins/cors#getting-started
                    config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));

                    config.router.apiBuilder(() -> {
                        new Controller<>(clientService).info();
                        new Controller<>(merchandiseService).info();
                        new Controller<>(packService).info();
                        new Controller<>(envelopeService).info();
                        new Controller<>(boxService).info();
                        new Controller<>(sackService).info();
                    });
                })
                .start(port)
                .get("/", ctx -> ctx.json("{ \"data\": \"Welcome to the sales service\", \"message\": \"ok\" }"))
                .get("/delivery/status", ctx -> ctx.json(DeliveryStatus.getAll().toString()))
                .afterMatched(ctx -> updateClients(ctx))
                .exception(
                        Exception.class,
                        (e, ctx) -> {
                            Utils.printStackTrace(e);
                            String error = Utils.keyValueToStrJson("message", e.getMessage(), "request", ctx.fullUrl());
                            ctx.json(error).status(400);
                        });
        Runtime
                .getRuntime()
                .addShutdownHook(
                        new Thread(() -> {
                            LOG.info(String.format("%sEl servidor Jetty de Javalin ha sido detenido%s%n", Utils.RED,
                                    Utils.RESET));
                        }));
    }

    private static void updateClients(@NotNull Context ctx) throws Exception {
        if(ctx.path().contains("client") && ctx.method().toString().equals("PATCH")){
            JSONObject client = new JSONObject(ctx.result()).getJSONObject("data");

            Utils.ifFileExistsUpdateFile(client, "storer", "Merchandise");
            Utils.ifFileExistsUpdateFile(client, "sender", "Pack");
            Utils.ifFileExistsUpdateFile(client, "addressee", "Pack");
            Utils.ifFileExistsUpdateFile(client, "sender", "Sack");
            Utils.ifFileExistsUpdateFile(client, "addressee", "Sack");
            Utils.ifFileExistsUpdateFile(client, "sender", "Box");
            Utils.ifFileExistsUpdateFile(client, "addressee", "Box");
            Utils.ifFileExistsUpdateFile(client, "sender", "Envelope");
            Utils.ifFileExistsUpdateFile(client, "addressee", "Envelope");
        }
    }
   
}
