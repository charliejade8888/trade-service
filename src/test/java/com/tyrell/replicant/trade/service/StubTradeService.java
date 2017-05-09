package com.tyrell.replicant.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tyrell.replicant.trade.service.model.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component("testTradeService")
public class StubTradeService implements ITradeService {

    @Override
    public Trade applyTwinvestAlgorithm(String payload) throws InterruptedException {
        Trade.TradeBuilder builder = new Trade.TradeBuilder();
        return builder.maxMonthlyInvestment(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .numOfSharesToBuy(BigInteger.TEN)
                .buyAdvice(BigDecimal.TEN)
                .ticker(EMPTY)
                .build();
    }

    @Override
    public void sendTrade(Trade trade) throws JsonProcessingException {
        // nothing to do here!!
    }
}
