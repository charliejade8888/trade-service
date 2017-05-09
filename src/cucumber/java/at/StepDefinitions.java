package at;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.tyrell.replicant.trade.service.Application;
import com.tyrell.replicant.trade.service.ITradeService;
import com.tyrell.replicant.trade.service.model.Trade;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class, CucumberTestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefinitions {

    private static final String REQUEST_PATH = "/trade";
    private static Response lastResponse;

    @LocalServerPort
    private int port;

    @Autowired
    ITradeService tradeServiceImpl;

    private static Trade trade;
    private static Trade onQueue;

    @Given("^a maximum monthly investment of \"([^\"]*)\" USD and a stock \"([^\"]*)\" with a price of \"([^\"]*)\" USD$")
    public void a_maximum_monthly_investment_of_USD_and_a_stock_with_a_price_of_USD(String maxMonthlyInvestment, String ticker, String price) throws Throwable {
        String payload = generatePayload(maxMonthlyInvestment, ticker, price);
        trade = tradeServiceImpl.applyTwinvestAlgorithm(payload);
    }

    @When("^a request is made to make a trade with this information$")
    public void a_request_is_made_to_make_a_trade_with_this_information() throws Throwable {
        tradeServiceImpl.sendTrade(trade);
        lastResponse = RestAssured.given().port(port).body(trade)
                .when().post(REQUEST_PATH);
    }

    @Then("^the following trade should be generated and put on the messaging queue:$")
    public void the_following_trade_should_be_generated_and_put_on_the_messaging_queue(List<Trade> expected) throws Throwable {
        int indexOfFirstTradeInTable = 0;
        assertTradeObjectsMatch(expected.get(indexOfFirstTradeInTable), onQueue);
    }

    private static void assertTradeObjectsMatch(Trade expected, Trade actual) {
        assertBigDecimalsHaveSameValue(expected.getBuyAdvice(), actual.getBuyAdvice());
        assertBigDecimalsHaveSameValue(expected.getMaxMonthlyInvestment(), actual.getMaxMonthlyInvestment());
        assertBigDecimalsHaveSameValue(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getNumOfSharesToBuy(), actual.getNumOfSharesToBuy());
        assertEquals(expected.getTicker(), actual.getTicker());
        assertNotNull(actual.getDateTime());
    }

    private static void assertBigDecimalsHaveSameValue(BigDecimal expected, BigDecimal actual) {
        assertEquals(expected.compareTo(actual), BigInteger.ZERO.intValue());
    }

    private String generatePayload(String maxMonthlyInvestment, String ticker, String price) throws JSONException {
        JSONObject json = new JSONObject();
        String maxMonthlyInvestmentKey = "maxMonthlyInvestment";
        String priceKey = "price";
        String tickerKey = "ticker";
        json.put(maxMonthlyInvestmentKey, maxMonthlyInvestment);
        json.put(priceKey, price);
        json.put(tickerKey, ticker);
        return json.toString();
    }

}