package org.jabref.logic.importer.fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.jabref.logic.importer.FulltextFetcher;
import org.jabref.logic.util.io.GsonJerseyProvider;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.jabref.model.entry.identifier.DOI;

import com.google.gson.JsonObject;
import org.glassfish.jersey.client.ClientConfig;



/**
 * A fulltext fetcher that uses <a href="https://oadoi.org/">oaDOI</a>.
 *
 * @implSpec API is documented at https://oadoi.org/api/v2
 */
public class OpenAccessDoi implements FulltextFetcher {
    private static String API_URL = "https://api.oadoi.org/v2/";


    private final Client client = ClientBuilder.newClient(new ClientConfig(GsonJerseyProvider.class));

    @Override
    public Optional<URL> findFullText(BibEntry entry) throws IOException {
        Objects.requireNonNull(entry);

        Optional<DOI> doi = entry.getField(FieldName.DOI)
                .flatMap(DOI::parse);
        if (doi.isPresent()) {

                return findFullText(doi.get());

        } else {
            return Optional.empty();
        }
    }

    public Optional<URL> findFullText(DOI doi) throws MalformedURLException {

        JsonObject root = client.target(API_URL + doi.getDOI() + "?email=developers@jabref.org").request().accept(MediaType.APPLICATION_JSON).get(JsonObject.class);

        Optional<String> url = Optional.ofNullable(root.get("best_oa_location").getAsJsonObject())
                .map(location -> location.get("url").getAsString());
        if (url.isPresent()) {
            return Optional.of(new URL(url.get()));
        } else {
            return Optional.empty();
        }
    }
}
