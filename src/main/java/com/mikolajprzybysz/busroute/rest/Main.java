package com.mikolajprzybysz.busroute.rest;

import com.mikolajprzybysz.busroute.logic.repository.RoutingRepository;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main class.
 *
 */
public class Main {

    private static HttpServer server;

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8088/api/";

    public Main(String path)
    {

        List<String> lines = null;

        Map<Integer, List<Integer>> routes = new HashMap<>();
        try {
            lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());

            Iterator<String> iterator = lines.iterator();

            if (!iterator.hasNext()) {
                throw new Exception("File is empty");
            }

            // skip first line
            String firstLine = iterator.next();

            while (iterator.hasNext()) {
                String line = iterator.next();

                String[] elements = line.trim().split(" ");

                if (elements.length < 3) {
                    throw new Exception("Line has less then 3 elements");
                }

                String[] stations = Arrays.copyOfRange(elements, 1, elements.length);
                Integer[] stationIds = new Integer[stations.length];
                for(int i = 0; i < stations.length; i++) {

                    stationIds[i] = Integer.parseInt(stations[i]);
                }

                int key = Integer.parseInt(elements[0]);
                routes.put(key, Arrays.asList(stationIds));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // create a resource config that scans for JAX-RS resources and providers
        // in com.mikolajprzybysz.busroute.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.mikolajprzybysz.busroute.rest");

        RoutingRepository repo = new RoutingRepository(routes);

        rc.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(repo).to(RoutingRepository.class);
            }
        });

        server = getServer(rc);
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public HttpServer getServer(ResourceConfig rc) {

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        return httpServer;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        if (args.length > 0) {
            String action = args[0];

            if ("start" == action) {
                String path = args[1];

                Main m = new Main(path);
                server.start();
            }

            if ("stop" == action && null != server) {
                server.shutdown();
            }
        }
    }
}

