package de.number26;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import de.number26.api.dtos.TransactionParametersDto;
import de.number26.api.dtos.TransactionSumDto;
import de.number26.configuration.CommandLineConfig;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;

/**
 * A primitive test to make sure that the original example works
 * (serialization/deserialization/conversion-to-from/trivial logic inside services etc...)
 *
 * @author vasiliy
 */
public class OriginalTest extends TestCase {
    private Challenge application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = new Challenge();
        application.start(new CommandLineConfig());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        application.stop();
    }

    public void testOriginalCase() throws Exception {
        Client client = Client.create();

        saveTransaction(client, 10, null, "cars", 5000);
        saveTransaction(client, 11, 10L, "shopping", 10000);

        long[] transactionsByType = getTransactionsByTypes(client, "cars");

        assertTrue(Arrays.equals(new long[]{10}, transactionsByType));

        assertEquals(15000.0, getSumForTransaction(client, 10).getSum());
        assertEquals(10000.0, getSumForTransaction(client, 11).getSum());
    }

    private TransactionSumDto getSumForTransaction(Client client, final int transactionId) throws IOException {
        WebResource webResource = client.resource("http://localhost:8080/transactionservice/sum/" + transactionId);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            fail();
        }
        return new ObjectMapper().readValue(response.getEntityInputStream(), TransactionSumDto.class);
    }

    private long[] getTransactionsByTypes(Client client, final String cars) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WebResource webResource = client.resource("http://localhost:8080/transactionservice/types/" + cars);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            fail();
        }
        return mapper.readValue(response.getEntityInputStream(), (new long[]{}).getClass());
    }

    private void saveTransaction(Client client, long transactionId, Long parentId, String type, int amount)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WebResource webResource = client.resource(
                "http://localhost:8080/transactionservice/transaction/" + transactionId);
        webResource.type(MediaType.APPLICATION_JSON_TYPE)
                .put(mapper.writeValueAsString(new TransactionParametersDto(parentId, type, amount)));
    }
}
