package com.tyrell.replicant.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrell.replicant.trade.service.model.Trade;
import com.tyrell.replicant.trade.service.model.Trade.TradeBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.ROUND_FLOOR;

@Component
public class TradeServiceImpl implements ITradeService {

    @Autowired
    private JmsOperations jmsTemplate;

    private static final int SCALE_FOR_MONETARY_VALUES = 2;
    private static final int SCALE_FOR_QUANTITY_VALUES = 0;

    @Override
    public Trade applyTwinvestAlgorithm(String body) throws InterruptedException, JSONException {
        JSONObject json = new JSONObject(body);
        String maxMonthlyInvestmentKey = "maxMonthlyInvestment";
        String priceKey = "price";
        String tickerKey = "ticker";
        BigDecimal maxMonthlyInvestment = new BigDecimal(json.getString(maxMonthlyInvestmentKey));
        BigDecimal price = new BigDecimal(json.getString(priceKey));
        String ticker = json.getString(tickerKey);
        return runTwinvestCalculations(maxMonthlyInvestment, price, ticker);
    }

    @Override
    public void sendTrade(Trade trade) throws JsonProcessingException {
        String queueName = "myQueue";
        String tradeString = new ObjectMapper().writeValueAsString(trade);
        jmsTemplate.convertAndSend(queueName, tradeString);
    }

    private BigDecimal calculateCodingMultiplier(BigDecimal maxMonthlyInvestment) {
        BigDecimal multiplicationFactor = new BigDecimal(3);
        BigDecimal divisionFactor = new BigDecimal(4);
        return maxMonthlyInvestment
                .multiply(multiplicationFactor)
                .divide(divisionFactor)
                .setScale(SCALE_FOR_MONETARY_VALUES, ROUND_CEILING);
    }

    private BigDecimal calculateTwinvestCode(BigDecimal price, BigDecimal codingMultiplier) {
        return price.multiply(codingMultiplier).setScale(SCALE_FOR_MONETARY_VALUES, ROUND_CEILING);
    }

    private Trade runTwinvestCalculations(BigDecimal maxMonthlyInvestment, BigDecimal price, String ticker) throws InterruptedException {
        BigDecimal codingMultiplier = calculateCodingMultiplier(maxMonthlyInvestment);
        BigDecimal twinVestCode = calculateTwinvestCode(price, codingMultiplier);
        BigDecimal roughBuyAdvise = twinVestCode.divide(price, SCALE_FOR_MONETARY_VALUES, ROUND_FLOOR);
        BigDecimal numOfSharesToBuyBigDecimal = roughBuyAdvise.divide(price, SCALE_FOR_QUANTITY_VALUES, ROUND_FLOOR);
        BigInteger numOfSharesToBuyBigInteger = numOfSharesToBuyBigDecimal.toBigInteger();
        BigDecimal buyAdvise = numOfSharesToBuyBigDecimal.multiply(price).setScale(SCALE_FOR_MONETARY_VALUES, ROUND_FLOOR);
        maxMonthlyInvestment = maxMonthlyInvestment.setScale(SCALE_FOR_MONETARY_VALUES, ROUND_FLOOR);
        TradeBuilder builder = new TradeBuilder();
        return builder.maxMonthlyInvestment(maxMonthlyInvestment)
                .price(price)
                .numOfSharesToBuy(numOfSharesToBuyBigInteger)
                .buyAdvice(buyAdvise)
                .ticker(ticker)
                .build();
    }

}