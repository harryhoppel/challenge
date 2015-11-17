package de.number26.api;

import de.number26.api.dtos.TransactionDto;
import de.number26.api.dtos.TransactionParametersDto;
import de.number26.api.dtos.TransactionSumDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author vasiliy
 */
@Path("/transactionservice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SumService {
    @PUT
    @Path("/transaction/{transaction_id}")
    void saveTransaction(@PathParam("transaction_id") long transactionId,
                         TransactionParametersDto transactionParametersDto);

    @GET
    @Path("/transaction/{transaction_id}")
    TransactionDto getTransaction(@PathParam("transaction_id") long transactionId);

    @GET
    @Path("/types/{type}")
    long[] getIdsFromType(@PathParam("type") String type);

    @GET
    @Path("/sum/{transaction_id}")
    TransactionSumDto getTransactionSum(@PathParam("transaction_id") long transactionId);
}
