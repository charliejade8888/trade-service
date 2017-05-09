Feature: Trade service

  Scenario: Trades can be generated using TWINVEST algorithm and placed on the messaging queue:
    Given a maximum monthly investment of "50.00" USD and a stock "AAPL" with a price of "4.61" USD
    When a request is made to make a trade with this information
    Then the following trade should be generated and put on the messaging queue:
      | dateTime                | price  | buyAdvice | ticker | numOfSharesToBuy | maxMonthlyInvestment |
      | 2016-12-13T14:59:59.999 | 4.61   | 36.88     | AAPL   | 8                | 50.00                |


