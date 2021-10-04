package org.acme;


import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;



@RegisterRestClient

public interface HoldingService {


    @GET
    @Path("/accounts")
    @Consumes("application/json")
    String getAccounts(@QueryParam("accountId") String accountId, @QueryParam("deskId") String deskId, @QueryParam("enterpriseId") String enterpriseId);


    @GET
    @Path("/accounts")
    @Consumes("application/json")
    String getAllAccounts();

    @POST
    @Path("/var")
    VarCalculationResponse calculateVar(VarCalculationRequest body);




}
