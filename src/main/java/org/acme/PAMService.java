package org.acme;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;

@RegisterRestClient
public interface PAMService {


    @GET
    @Path("/server/queries/processes/instances/variables/correlationId")
    @Produces("application/json")
    @ClientHeaderParam(name="Authorization", value="Basic YWRtaW5Vc2VyOlJlZEhhdA==")
    String getProcess(@javax.ws.rs.QueryParam("varValue") String correlationId, @QueryParam("status") int status);



    @GET
    @Path("/server/containers/risk-analytics-orchestrator_1.0.0-SNAPSHOT/processes/instances/{processId}/variables/instances/response")
    @Produces("application/json")
    @ClientHeaderParam(name="Authorization", value="Basic YWRtaW5Vc2VyOlJlZEhhdA==")
    String getTasks(@PathParam("processId") String processId);


}