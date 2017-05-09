package com.tyrell.replicant.trade.service.model;

import com.tyrell.replicant.trade.service.model.Trade.TradeBuilder;
import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.reflect.Modifier.isSynchronized;
import static org.junit.Assert.*;

public class TradeTest {

    @Test
    public void testGetPrice() throws InterruptedException {
        // given
        BigDecimal expected = new BigDecimal(10.25);
        Trade.TradeBuilder builder = new TradeBuilder();
        builder.price(expected);

        // when
        Trade underTest = builder.build();
        BigDecimal actual = underTest.getPrice();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDateTime() throws InterruptedException {
        // given
        Trade.TradeBuilder builder = new TradeBuilder();

        // when
        Trade underTest = builder.build();
        String actual = underTest.getDateTime();

        // then
        assertNotNull(actual);
    }

    @Test
    public void testThreadsCanOnlyUseTradeBuilderInstanceForDateTimeMethodSynchronously() throws InterruptedException, NoSuchMethodException {
        //given
        String dateTimeMethodName = "dateTime";
        Class tradeBuilderClass = TradeBuilder.class;
        Method dateTimeMethod = tradeBuilderClass.getMethod(dateTimeMethodName);

        //when
        boolean shouldBeSynchronized = isSynchronized(dateTimeMethod.getModifiers());

        //then
        assertTrue(shouldBeSynchronized);
    }

    @Test
    public void testGetNumOfSharesToBuy() throws InterruptedException {
        // given
        BigInteger expected = new BigInteger("58");
        Trade.TradeBuilder builder = new TradeBuilder();
        builder.numOfSharesToBuy(expected);

        // when
        Trade underTest = builder.build();
        BigInteger actual = underTest.getNumOfSharesToBuy();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBuyAdvice() throws InterruptedException {
        // given
        BigDecimal expected = new BigDecimal(500.25);
        Trade.TradeBuilder builder = new TradeBuilder();
        builder.buyAdvice(expected);

        // when
        Trade underTest = builder.build();
        BigDecimal actual = underTest.getBuyAdvice();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxMonthlyInvestment() throws InterruptedException {
        // given
        BigDecimal expected = new BigDecimal(500.25);
        Trade.TradeBuilder builder = new TradeBuilder();
        builder.maxMonthlyInvestment(expected);

        // when
        Trade underTest = builder.build();
        BigDecimal actual = underTest.getMaxMonthlyInvestment();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTicker() throws InterruptedException {
        // given
        String expected = "INTC";
        Trade.TradeBuilder builder = new TradeBuilder();
        builder.ticker(expected);

        // when
        Trade underTest = builder.build();
        String actual = underTest.getTicker();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_ShouldPass() throws InterruptedException {
        // given
        Trade.TradeBuilder builder = new TradeBuilder();
        Trade trade1 = builder.build();
        Trade trade2 = trade1;

        // when
        boolean shouldBeTrue = trade1.equals(trade2);

        // then
        assertTrue(shouldBeTrue);
    }

    @Test
    public void testEquals_ShouldFail() throws InterruptedException {
        // given
        Trade.TradeBuilder builder = new TradeBuilder();
        Trade trade1 = builder.build();
        Trade trade2 = builder.build();

        // when
        boolean shouldBeFalse = trade1.equals(trade2);

        // then
        assertFalse(shouldBeFalse);
    }

    @Test
    public void testHashcode_WhenObjectsAreTheSame() throws InterruptedException {
        // given
        Trade.TradeBuilder builder = new TradeBuilder();
        Trade trade1 = builder.build();
        Trade trade2 = trade1;

        // when
        int hashcode1 = trade1.hashCode();
        int hashcode2 = trade2.hashCode();

        // then
        assertTrue(hashcode1 == hashcode2);
    }

    @Test
    public void testHashcode_WhenObjectsAreDifferent() throws InterruptedException {
        // given
        Trade.TradeBuilder builder = new TradeBuilder();
        Trade trade1 = builder.build();
        Trade trade2 = builder.build();

        // when
        int hashcode1 = trade1.hashCode();
        int hashcode2 = trade2.hashCode();

        // then
        assertFalse(hashcode1 == hashcode2);
    }

}