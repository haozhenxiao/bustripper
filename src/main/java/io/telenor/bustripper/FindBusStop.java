package io.telenor.bustripper;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Searches for bus stops in area provided.
 */
public class FindBusStop implements Runnable {


    private static final String SEARCH_URL = "http://reisapi.ruter.no/Place/GetPlaces/";

    private String searchTerm;

    private Client client;

    private TripsCallback listener;

    public FindBusStop(TripsCallback callback, String searchTerm) {
        this.listener = callback;
        this.searchTerm = searchTerm;
    }

    public void run() {
        ClientConfig configuration = new ClientConfig();

        client = ClientBuilder.newClient(configuration);

        Invocation.Builder invocationBuilder = client
                .target(UriBuilder.fromUri(SEARCH_URL + searchTerm))
                .request(MediaType.APPLICATION_JSON);

        final AsyncInvoker asyncInvoker = invocationBuilder.async();
        BusStopsCallBack callback = new BusStopsCallBack(listener);
        asyncInvoker.get(callback);
    }
}
