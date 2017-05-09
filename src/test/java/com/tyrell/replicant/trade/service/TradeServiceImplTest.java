package com.tyrell.replicant.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tyrell.replicant.trade.service.model.Trade;
import gherkin.deps.com.google.gson.Gson;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeServiceImplTest {

    @Autowired
    ITradeService tradeServiceImpl;

    @Test
    public void testApplyTwinvestAlgorithm() throws InterruptedException, JSONException {
        // given
        String maxMonthlyInvestment = "50.00";
        String price = "4.61";
        String ticker = EMPTY;
        Trade expected = createExpectedTrade(maxMonthlyInvestment, price, ticker);
        String payload = new Gson().toJson(expected).toString();

        // when
        Trade actual = tradeServiceImpl.applyTwinvestAlgorithm(payload);

        // then
        assertTradeObjectsMatch(expected, actual);
    }

    @Test
    public void testSendMessage() throws InterruptedException, JsonProcessingException {
        // given
        String maxMonthlyInvestment = "50.00";
        String price = "4.61";
        String ticker = EMPTY;
        Trade sent = createExpectedTrade(maxMonthlyInvestment, price, ticker);

        // when
        tradeServiceImpl.sendTrade(sent);

        // then
        // nothing to do here!!
    }

    private static void assertTradeObjectsMatch(Trade expected, Trade actual) {
        assertEquals(expected.getNumOfSharesToBuy(), actual.getNumOfSharesToBuy());
        assertEquals(expected.getBuyAdvice(), actual.getBuyAdvice());
        assertEquals(expected.getMaxMonthlyInvestment(), actual.getMaxMonthlyInvestment());
        assertEquals(expected.getTicker(), actual.getTicker());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertNotNull(actual.getDateTime());
    }

    private static Trade createExpectedTrade(String maxMonthlyInvestment, String price, String ticker) throws InterruptedException {
        BigDecimal buyAdvice = new BigDecimal("36.88");
        BigInteger numOfSharesToBuy = new BigInteger("8");
        return new Trade.TradeBuilder()
                .maxMonthlyInvestment(new BigDecimal(maxMonthlyInvestment))
                .price(new BigDecimal(price))
                .ticker(ticker)
                .buyAdvice(buyAdvice)
                .numOfSharesToBuy(numOfSharesToBuy)
                .build();
    }

}