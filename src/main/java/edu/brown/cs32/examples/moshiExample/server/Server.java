package edu.brown.cs32.examples.moshiExample.server;

import static spark.Spark.after;

import edu.brown.cs32.examples.moshiExample.soup.Soup;
import edu.brown.cs32.examples.moshiExample.soup.Soup;
import spark.Filter;
import spark.Spark;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various handlers.
 *
 * We have two endpoints in this demo. They need to share state (a menu).
 * This is a great chance to use dependency injection, as we do here with the menu set. If we needed more endpoints,
 * more functionality classes, etc. we could make sure they all had the same shared state.
 */
public class Server {
    public static void main(String[] args) {
        int port = 3232;
        Set<Soup> menu = new HashSet<>();
        // TODO: Is there something wrong with this menu?
        List<String> soup_ingredient = List.of("onions", "beef broth", "thyme", "bay leaves", "French bread", "gruyere cheese");
        Soup frenchOnion = new Soup(soup_ingredient);
        // Soup frenchOnion = new Soup(false);
        frenchOnion.setSoupName("french onion");
        menu.add(frenchOnion);
        Spark.port(port);
        /*
            Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
            be able to make requests to the server.

            By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
            This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
            from any website. Instead, you should set this header to the origin of your client, or a list of origins
            that you trust.

            By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
            Again, it's generally better to be more specific here and only allow the methods you need, but for
            this demo we'll allow all methods.

            We recommend you learn more about CORS with these resources:
                - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
                - https://portswigger.net/web-security/cors
         */
        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "*");
        });

        // Setting up the handler for the GET /order and /mock endpoints
        Spark.get("order", new OrderHandler(menu));
        Spark.get("mock", new MockHandler());
        Spark.init();
        Spark.awaitInitialization();

        // Notice this link alone leads to a 404... Why is that?
        System.out.println("Server started at http://localhost:" + port);
    }
}
