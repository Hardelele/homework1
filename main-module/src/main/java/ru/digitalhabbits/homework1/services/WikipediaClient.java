package ru.digitalhabbits.homework1.services;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.http.client.utils.URIBuilder;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WikipediaClient {
    public static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";

    @Nonnull
    public String search(@Nonnull String searchString) {
        final URI uri = prepareSearchUrl(searchString);
        HttpRequest request = prepareHttpRequest(uri);
        HttpResponse<String> response = sendRequestAndReturnJsonString(request);
        return extractContent(
                extractPageJson(response)
        );
    }

    @Nonnull
    private String extractContent(@Nonnull JsonObject page) {
        return page.getValue("extract").toString();
    }

    @Nonnull
    private JsonObject extractPageJson(@Nonnull HttpResponse<String> response) {
        JsonObject pagesJson = new JsonObject(response.body())
                .getJsonObject("query")
                .getJsonObject("pages");
        String randomKey = pagesJson
                .getMap()
                .keySet().stream()
                .findFirst()
                .orElseThrow();
        return pagesJson.getJsonObject(randomKey);
    }

    @Nonnull
    private HttpResponse<String> sendRequestAndReturnJsonString(@Nonnull HttpRequest request) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Nonnull
    private HttpRequest prepareHttpRequest(@Nonnull URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    @Nonnull
    private URI prepareSearchUrl(@Nonnull String searchString) {
        try {
            return new URIBuilder(WIKIPEDIA_SEARCH_URL)
                    .addParameter("action", "query")
                    .addParameter("format", "json")
                    .addParameter("titles", searchString)
                    .addParameter("prop", "extracts")
                    .addParameter("explaintext", "")
                    .build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
