package com.tyrell.replicant.trade.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Trade {

    private String ticker;
    private String dateTime;
    private BigInteger numOfSharesToBuy;
    private BigDecimal price;
    private BigDecimal buyAdvice;
    private BigDecimal maxMonthlyInvestment;

    private Trade() {
    }

    private Trade(TradeBuilder builder) throws InterruptedException {
        this.price = builder.price;
        this.numOfSharesToBuy = builder.numOfSharesToBuy;
        this.buyAdvice = builder.buyAdvice;
        this.ticker = builder.ticker;
        this.dateTime = builder.dateTime();
        this.maxMonthlyInvestment = builder.maxMonthlyInvestment;
    }

    public BigDecimal getMaxMonthlyInvestment() {
        return maxMonthlyInvestment;
    }

    public BigInteger getNumOfSharesToBuy() {
        return numOfSharesToBuy;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getBuyAdvice() {
        return buyAdvice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return dateTime != null ? dateTime.equals(trade.dateTime) : trade.dateTime == null;
    }

    @Override
    public int hashCode() {
        return dateTime != null ? dateTime.hashCode() : 0;
    }

    public static class TradeBuilder {
        public BigDecimal maxMonthlyInvestment;
        private BigDecimal price;
        private BigInteger numOfSharesToBuy;
        private BigDecimal buyAdvice;
        private String ticker;

        public TradeBuilder maxMonthlyInvestment(BigDecimal maxMonthlyInvestment) {
            this.maxMonthlyInvestment = maxMonthlyInvestment;
            return this;
        }

        public TradeBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public TradeBuilder numOfSharesToBuy(BigInteger numOfSharesToBuy) {
            this.numOfSharesToBuy = numOfSharesToBuy;
            return this;
        }

        public TradeBuilder buyAdvice(BigDecimal buyAdvice) {
            this.buyAdvice = buyAdvice;
            return this;
        }

        public TradeBuilder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public synchronized String dateTime() throws InterruptedException {
            Thread.sleep(500);
            return new DateTime().toLocalDateTime().now().toString();
        }

        public Trade build() throws InterruptedException {
            Trade trade = new Trade(this);
            return trade;
        }

    }
}