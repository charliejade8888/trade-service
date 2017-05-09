package com.tyrell.replicant.trade.service;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ITradeService testTradeService;

    @Autowired
    TradeController controller;

    private static final String REQUEST_PATH = "/trade";

    @Before
    public void setUp() {
        String fieldToReplace = "tradeServiceImpl";
        setField(controller, fieldToReplace, testTradeService);
    }

    @Test
    public void testPostTrade() throws JSONException {
        // given
        int expectedStatusCode = 201;
        String maxMonthlyInvestmentKey = "maxMonthlyInvestment";
        String priceKey = "price";
        String tickerKey = "ticker";

        JSONObject body = new JSONObject();
        body.put(maxMonthlyInvestmentKey, BigDecimal.TEN);
        body.put(priceKey, BigDecimal.ONE);
        body.put(tickerKey, EMPTY);

        // when
        Response response = RestAssured.given().port(port).contentType(ContentType.TEXT)
                .body(body.toString())
                .when().post(REQUEST_PATH);

        // then

        assertEquals(expectedStatusCode, response.getStatusCode());
    }
}
