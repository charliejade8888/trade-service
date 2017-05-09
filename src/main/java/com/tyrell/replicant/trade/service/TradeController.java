package com.tyrell.replicant.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tyrell.replicant.trade.service.model.Trade;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
public class TradeController {

    @Autowired
    private ITradeService tradeServiceImpl;

    @CrossOrigin(origins = "http://localhost:8080")
    @ApiOperation(value = "Puts trade on messaging queue.", notes = "Trade generated using information in payload.")
    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public ResponseEntity<Trade> postTrade(@RequestBody String body) throws InterruptedException, JSONException, JsonProcessingException {
        Trade trade = tradeServiceImpl.applyTwinvestAlgorithm(body);
        tradeServiceImpl.sendTrade(trade);
        return new ResponseEntity<>(trade, CREATED);
    }

}