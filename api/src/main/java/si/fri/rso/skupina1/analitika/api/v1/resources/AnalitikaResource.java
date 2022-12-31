package si.fri.rso.skupina1.analitika.api.v1.resources;


import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.IOException;
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


    @GET
    public String getSteviloNarocil() throws WebApplicationException, ProcessingException {

        String graphQLquery = "query MyQuery {\n" +
                "  allPonudniki {\n" +
                "    result {\n" +
                "      ime\n" +
                "      mesto\n" +
                "      ponudnikId\n" +
                "      postnaSt\n" +
                "      ulica\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String res = executePOST(graphQLquery);
        JSONObject jsonObject = new JSONObject(res);
        return res;
    }

    
//    @Operation(description = "Pridobi podatke o ponudniku", summary = "Pridobi podatke o ponudniku")
//    @APIResponses({
//            @APIResponse(responseCode = "200",
//                    description = "Ponudnik",
//                    content = @Content(
//                            schema = @Schema(implementation = Ponudnik.class))
//            )})
//    @GET
//    @Path("/{ponudnikId}")
//    public Response getPonudnik(@Parameter(description = "Podnudnik ID.", required = true)
//                                     @PathParam("ponudnikId") Integer ponudnikId) {
//
//        Ponudnik ponudnik = ponudnikBean.getPonudnik(ponudnikId);
//
//        if (ponudnik == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        return Response.status(Response.Status.OK).entity(ponudnik).build();
//    }
//
//
//
//    @Operation(description = "Dodaj ponudnika.", summary = "Dodaj ponudnika")
//    @APIResponses({
//            @APIResponse(responseCode = "201",
//                    description = "Ponudnik successfuly added."
//            ),
//            @APIResponse(responseCode = "405", description = "Validation error.")
//    })
//    @POST
//    public Response createPonudnik(@RequestBody(
//            description = "DTO objekt s ponudnikom.",
//            required = true, content = @Content(
//            schema = @Schema(implementation = Ponudnik.class))) Ponudnik ponudnik) {
//
//        if ((ponudnik.getIme() == null || ponudnik.getMesto() == null || ponudnik.getPostnaSt() == null) || ponudnik.getUlica() == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        else {
//            ponudnik = ponudnikBean.createPonudnik(ponudnik);
//        }
//
//        return Response.status(Response.Status.CONFLICT).entity(ponudnik).build();
//
//    }
//
//
//    @Operation(description = "Posodobi ponudnika.", summary = "Posodobi ponudnika")
//    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    description = "Ponudnik successfully updated."
//            )
//    })
//    @PUT
//    @Path("{ponudnikId}")
//    public Response putPonudnik(@Parameter(description = "Ponudnik ID.", required = true)
//                                     @PathParam("ponudnikId") Integer ponudnikId,
//                                     @RequestBody(
//                                             description = "DTO objekt s ponudnikom",
//                                             required = true, content = @Content(
//                                             schema = @Schema(implementation = Ponudnik.class)))
//                                             Ponudnik ponudnik){
//
//        ponudnik = ponudnikBean.putPonudnik(ponudnikId, ponudnik);
//
//        if (ponudnik == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        return Response.status(Response.Status.NOT_MODIFIED).build();
//
//    }
//
//    @Operation(description = "Izbriši ponudnika.", summary = "Izbriši ponudnika")
//    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    description = "Ponudnik successfully deleted."
//            ),
//            @APIResponse(
//                    responseCode = "404",
//                    description = "Not found."
//            )
//    })
//    @DELETE
//    @Path("{ponudnikId}")
//    public Response deletePonudnik(@Parameter(description = "Ponudnik ID.", required = true)
//                                        @PathParam("ponudnikId") Integer ponudnikId){
//
//        boolean deleted = ponudnikBean.deletePonudnik(ponudnikId);
//
//        if (deleted) {
//            return Response.status(Response.Status.NO_CONTENT).build();
//        }
//        else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }


}
