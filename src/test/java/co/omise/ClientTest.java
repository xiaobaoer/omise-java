package co.omise;

import co.omise.models.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class ClientTest extends OmiseTest {
    private static final String LIVETEST_PKEY = "pkey_test_replaceme";
    private static final String LIVETEST_SKEY = "skey_test_replaceme";

    @Test
    public void testCtor() throws ClientException {
        try {
            new Client(null);
            fail("exception expected");
        } catch (NullPointerException e) {
        }
    }

    @Test
    @Ignore("only hit the network when we need to.")
    public void testLiveErrorVault() throws ClientException, IOException {
        try {
            Token.Create creation = new Token.Create().card(new Card.Create()
                    .name("Omise Co., Ltd.")
                    .number("4242424242424242")
                    .expiration(10, 2020)
                    .securityCode("123")
            );

            new Client("pkey_test_123", "skey_test_123").tokens().create(creation);
        } catch (OmiseException e) {
            assertEquals("authentication_failure", e.getCode());
        }
    }

    @Test
    @Ignore("only hit the network when we need to.")
    public void testLiveError() throws ClientException, IOException {
        try {
            new Client("skey_test_123").account().get();
        } catch (OmiseException e) {
            assertEquals("authentication_failure", e.getCode());
        }
    }

    @Test
    @Ignore("only hit the network when we need to.")
    public void testLiveCard() throws ClientException, IOException, OmiseException {
        ScopedList<Card> list = liveTestClient()
                .customer("cust_test_5665swqhhb3mioax1y7")
                .cards()
                .list();

        for (Card card : list.getData()) {
            System.out.println(card.getId() + " : " + card.getLastDigits());
        }
    }

    @Test
    @Ignore("only hit the network when we need to.")
    public void testLiveCharge() throws ClientException, IOException, OmiseException {
        Client client = liveTestClient();
        Token token = client.tokens().create(new Token.Create()
                .card(new Card.Create()
                        .name("Omise Co., Ltd. - testLiveCharge")
                        .number("4242424242424242")
                        .securityCode("123")
                        .expiration(10, 2020)));

        Charge charge = client.charges().create(new Charge.Create()
                .amount(200000) // 2,000 THB
                .currency("thb")
                .description("omise-java test")
                .card(token.getId()));

        System.out.println("created charge: " + charge.getId());
    }

    @Test
    @Ignore("only hit the network when we need to.")
    public void testLiveFetch() throws ClientException, IOException, OmiseException {
        Balance balance = liveTestClient().balance().get();
        assertTrue(balance.getTotal() > 100000);

        System.out.println("current balance: " + Long.toString(balance.getTotal()));
    }

    private Client liveTestClient() throws ClientException {
        return new Client(LIVETEST_PKEY, LIVETEST_SKEY);
    }
}
