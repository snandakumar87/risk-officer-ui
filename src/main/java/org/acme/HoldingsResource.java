package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Path("/holdings")
public class HoldingsResource {

    @Inject
    KafkaController kafkaController;

    @Inject
    @RestClient
    HoldingService holdingService;



    @Inject
    @RestClient
    PAMService pamService;

    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHoldingsForAllAccounts(@PathParam Double confidence) throws Exception{
        String response = holdingService.getAllAccounts();
        List<AccountObject> holdingsResponse = new ArrayList<>();
        AccountObject account = null;
        try {
            Map<String,String> map = new ObjectMapper().readValue(response, HashMap.class);
            List<AccountObject> holdingResponse =  parseResponse(holdingsResponse, map);
            System.out.println(holdingsResponse);
            return new ObjectMapper().writeValueAsString(holdingsResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(holdingsResponse);
    }


    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHoldingsForAccountId(@PathParam String accountId) throws JsonProcessingException {
       String response = holdingService.getAccounts(accountId,null,null);
       List<AccountObject> holdingsResponse = new ArrayList<>();
       AccountObject account = null;
        try {
            Map<String,String> map = new ObjectMapper().readValue(response, HashMap.class);
            List<AccountObject> holdingResponse =  parseResponse(holdingsResponse, map);
            System.out.println(holdingsResponse);
            return new ObjectMapper().writeValueAsString(holdingsResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

            return new ObjectMapper().writeValueAsString(holdingsResponse);
    }


    @GET
    @Path("/enterprise/{enterpriseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHoldingsForEnterpriseId(@PathParam String enterpriseId) throws Exception{
        String response = holdingService.getAccounts(null,null,enterpriseId);
        List<AccountObject> holdingsResponse = new ArrayList<>();
        AccountObject account = null;
        try {
            Map<String,String> map = new ObjectMapper().readValue(response, HashMap.class);
            List<AccountObject> holdingResponse =  parseResponse(holdingsResponse, map);
            System.out.println(holdingsResponse);
            return new ObjectMapper().writeValueAsString(holdingsResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(holdingsResponse);
    }

    @GET
    @Path("/desk/{deskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHoldingsForDeskId(@PathParam String deskId) throws Exception{
        String response = holdingService.getAccounts(null,deskId,null);
        List<AccountObject> holdingsResponse = new ArrayList<>();
        AccountObject account = null;
        try {
            Map<String,String> map = new ObjectMapper().readValue(response, HashMap.class);
            System.out.println(map);
            List<AccountObject> holdingResponse =  parseResponse(holdingsResponse, map);
            System.out.println(holdingsResponse);
            return new ObjectMapper().writeValueAsString(holdingsResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(holdingsResponse);
    }

    @POST
    @Path("/confidence/{confidence}/{entity}/{entityId}/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String calculateVar(String body, @PathParam("confidence")String confidence, @PathParam("entity") String entity, @PathParam("entityId") String id, @PathParam("uuid") String uuid) throws Exception{
        VarCalculationRequest respo = getVarCalculationResponse(body, confidence, entity, id,false);
        respo.setCorrelationId(uuid);
        kafkaController.produce(uuid,new ObjectMapper().writeValueAsString(respo));

        return new ObjectMapper().writeValueAsString(uuid);

    }


    @POST
    @Path("/allaccounts/confidence/{confidence}/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String calculateVarForAllAccounts(String body, @PathParam("confidence")String confidence ,@PathParam("uuid") String uuid) throws Exception{
        VarCalculationRequest respo = getVarCalculationResponse(body, confidence, null, null,true);
        respo.setCorrelationId(uuid);
        kafkaController.produce(uuid,new ObjectMapper().writeValueAsString(respo));
        return new ObjectMapper().writeValueAsString(uuid);

    }

    @GET
    @Path("/results/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getCase(@javax.ws.rs.PathParam("uuid") String correlationId) throws JsonProcessingException, InterruptedException {
        List<VarResponse> varResponses = new ArrayList<>();
        try{
        System.out.println(correlationId);
        String resp = pamService.getProcess(correlationId,2);
        System.out.println("resp"+resp);


        Map respMap = new ObjectMapper().readValue(resp,HashMap.class);
        List respList = (ArrayList) respMap.get("process-instance");

        Map processMap = (Map)respList.get(0);

        String varResponse = pamService.getTasks(String.valueOf(processMap.get("process-instance-id")));


        Map varResMap = new ObjectMapper().readValue(varResponse,Map.class);
        List list = (List) varResMap.get("variable-instance");

        Map returnMap = (Map) list.get(0);
        System.out.println(returnMap);
        String finlMap = (String)returnMap.get("value");

        String mapString = finlMap.substring(1,finlMap.length()-2);
        String map[] = mapString.split(",");


        VarResponse varResponse1 = null;
        String stringPattern[] = null;
        for(String obj: map) {
            stringPattern = obj.split("=");
            varResponse1 = new VarResponse();
            varResponse1.setEntityId(stringPattern[0]);
            varResponse1.setVarResults(stringPattern[1]);
            varResponses.add(varResponse1);
        }

        System.out.println(new ObjectMapper().writeValueAsString(varResponses));


        }catch (Exception e) {

        }
        return new ObjectMapper().writeValueAsString(varResponses);
    }

    private List<AccountObject>  parseResponse(List<AccountObject> holdingsResponse, Map map) {
        AccountObject account;
        List accountList = Collections.singletonList(map.get("accounts"));

        for(Object str: accountList) {

            List<Map<String,List<String>>> listArray = (ArrayList) str;

            for (Map accountMap : listArray) {

                String accountNo = (String) accountMap.get("accountId");
                System.out.println(accountNo);
                System.out.println(accountMap.get("holdings").getClass());
                for (Object holding : (ArrayList)accountMap.get("holdings")) {
                    Map mp = (Map) holding;
                    account = new AccountObject();
                    account.setAccountId(accountNo);
                    account.setDescription((String)mp.get("description"));
                    account.setQuantity((Integer)mp.get("quantity"));
                    account.setSymbol((String)mp.get("symbol"));
                    holdingsResponse.add(account);

                }
            }


        }
        return holdingsResponse;
    }
    private VarCalculationRequest getVarCalculationResponse(String body, String confidence, String entity, String id, boolean allAccounts) throws JsonProcessingException {
        VarCalculationRequest varCalculationRequest = new VarCalculationRequest();
        List<Account> accountList = new ArrayList<>();
        Account account = null;
        Holding holding = null;
        List<Holding> holdingsList = null;
        List<Map> accountObjects = new ObjectMapper().readValue(body, List.class);
        String accountId = null;

        for(Map accountObject: accountObjects) {

            if(accountId== null || !accountObject.get("accountId").equals(accountId)){
                if(accountId != null) {
                    account.setHoldings(holdingsList);
                    accountList.add(account);
                }
                account = new Account();
                holdingsList = new ArrayList<>();
                account.setAccountId((String)accountObject.get("accountId"));
            }
            holding = new Holding();
            holding.setDescription((String)accountObject.get("description"));
            holding.setQuantity((Integer)accountObject.get("quantity"));
            holding.setDescription((String)accountObject.get("description"));
            holding.setSymbol((String)accountObject.get("symbol"));
            holdingsList.add(holding);
            accountId = (String) accountObject.get("accountId");

        }
        account.setHoldings(holdingsList);
        accountList.add(account);
        System.out.println(accountList);
        Accounts accounts = new Accounts();
        accounts.setAccounts(accountList);
        List<Accounts> accountsList = new ArrayList<>();
        accountsList.add(accounts);
        varCalculationRequest.setCorrelationId(String.valueOf(new Random().nextInt()));
        varCalculationRequest.setAccounts(accountList);
        varCalculationRequest.setConfidence(Double.valueOf(confidence));
        varCalculationRequest.setEntityId(id);
        varCalculationRequest.setEntityType(entity);
        varCalculationRequest.setAllAccounts(allAccounts);

//        VarCalculationResponse respo = holdingService.calculateVar(varCalculationRequest);
        return varCalculationRequest;
    }


}