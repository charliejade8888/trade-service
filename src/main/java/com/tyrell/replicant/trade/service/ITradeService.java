package com.tyrell.replicant.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tyrell.replicant.trade.service.model.Trade;
import org.json.JSONException;

import java.math.BigDecimal;

public interface ITradeService {

    Trade applyTwinvestAlgorithm(String body) throws InterruptedException, JSONException;
    void sendTrade(Trade trade) throws JsonProcessingException;

}
