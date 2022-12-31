package si.fri.rso.skupina1.analitika.api.v1.resources;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.UUID;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/analytics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalitikaResource {

    private Logger log = Logger.getLogger(AnalitikaResource.class.getName());

    @Context
    protected UriInfo uriInfo;

    private static final UUID uuid = UUID.randomUUID();

    private Client httpClient;
    private String baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = ConfigurationUtil.getInstance().get("integrations.ponudniki.baseurl").get();
        log.info("Initialized: " + AnalitikaResource.class.getName() + ", UUID: " + uuid);
    }


    private String executePOST(String body) {
        MediaType APPLICATION_GRAPHQL = new MediaType("application", "graphql");
        return httpClient
                .target(baseUrl)
                .request(APPLICATION_GRAPHQL)
                .post(Entity.entity(body, APPLICATION_GRAPHQL))
                .readEntity(String.class);
    }

    @Operation(description = "Pridobi število naročil uporabnika", summary = "Pridobi število naročil uporabnika")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Integer",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class))
            )})
    @GET
    @Path("/{uporabnikId}/orders")
    public Integer getSteviloNarocil(@Parameter(description = "Uporabnik ID.", required = true)
                                     @PathParam("uporabnikId") Integer uporabnikId) throws WebApplicationException, ProcessingException {

        String graphQLquery = """
                query MyQuery {
                  allOrders(filter: {fields: {field: "clientId", op: EQ, value: "%d"}}) {
                    result {
                      id
                    }
                  }
                }
                """.formatted(uporabnikId);
        String res = executePOST(graphQLquery);

        return new JSONObject(res)
                .getJSONObject("data")
                .getJSONObject("allOrders")
                .getJSONArray("result")
                .length();
    }



    @Operation(description = "Pridobi število dostav uporabnika", summary = "Pridobi število dostav uporabnika")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Integer",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class))
            )})
    @GET
    @Path("/{uporabnikId}/deliveries")
    public Integer getSteviloDostav(@Parameter(description = "Uporabnik ID.", required = true)
                                     @PathParam("uporabnikId") Integer uporabnikId) throws WebApplicationException, ProcessingException {

        String graphQLquery = """
                query MyQuery {
                  allOrders(filter: {fields: {field: "deliveryPersonId", op: EQ, value: "%d"}}) {
                    result {
                      id
                    }
                  }
                }
                """.formatted(uporabnikId);
        String res = executePOST(graphQLquery);

        return new JSONObject(res)
                .getJSONObject("data")
                .getJSONObject("allOrders")
                .getJSONArray("result")
                .length();
    }


    @Operation(description = "Pridobi skupne stroške uporabnika", summary = "Pridobi skupne stroške uporabnika")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Double",
                    content = @Content(
                            schema = @Schema(implementation = Double.class))
            )})
    @GET
    @Path("/{uporabnikId}/costs")
    public Double getSkupniStrosek(@Parameter(description = "Uporabnik ID.", required = true)
                                    @PathParam("uporabnikId") Integer uporabnikId) throws WebApplicationException, ProcessingException {

        String graphQLquery = """
                query MyQuery {
                  allOrders(filter: {fields: {field: "clientId", op: EQ, value: "%d"}}) {
                    result {
                      items {
                        price
                      }
                    }
                  }
                }
                """.formatted(uporabnikId);
        String res = executePOST(graphQLquery);

        Double costSum = 0.0;
        JSONArray arr = new JSONObject(res)
                .getJSONObject("data")
                .getJSONObject("allOrders")
                .getJSONArray("result");

        for (int i = 0; i < arr.length(); i++) {
            JSONArray items = arr.getJSONObject(i)
                            .getJSONArray("items");
            for (int j = 0; j < items.length(); j++) {
                costSum = Double.sum(
                        costSum,
                        items.getJSONObject(j).getDouble("price")
                );
            }
        }
        return costSum;
    }



    @Operation(description = "Pridobi skupni zaslužek ponudnika", summary = "Pridobi skupni zaslužek ponudnika")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Double",
                    content = @Content(
                            schema = @Schema(implementation = Double.class))
            )})
    @GET
    @Path("/{ponudnikId}/earnings")
    public Double getSkupniZasluzek(@Parameter(description = "Ponudnik ID.", required = true)
                                   @PathParam("ponudnikId") Integer ponudnikId) throws WebApplicationException, ProcessingException {

        String graphQLquery = """
                query MyQuery {
                  allOrders(filter: {fields: {field: "providerId", op: EQ, value: "%d"}}) {
                    result {
                      items {
                        price
                      }
                    }
                  }
                }
                """.formatted(ponudnikId);
        String res = executePOST(graphQLquery);

        Double costSum = 0.0;
        JSONArray arr = new JSONObject(res)
                .getJSONObject("data")
                .getJSONObject("allOrders")
                .getJSONArray("result");

        for (int i = 0; i < arr.length(); i++) {
            JSONArray items = arr.getJSONObject(i)
                    .getJSONArray("items");
            for (int j = 0; j < items.length(); j++) {
                costSum = Double.sum(
                        costSum,
                        items.getJSONObject(j).getDouble("price")
                );
            }
        }
        return costSum;
    }
}
