package StockTickerProgram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UHStockPriceServiceTest {

  UHStockPriceService service;

  @BeforeEach
  void init() {
    service = new UHStockPriceService();
  }

  @Test
  void getPriceForTickerGOOG() {
    assertTrue(service.getPrice("GOOG") > 0);
  }

  @Test
  void pricesAreDifferentForDifferentTickers() {
    assertTrue(service.getPrice("GOOG") != service.getPrice("AMZN"));
  }

  @Test
  void getPriceForInvalidTicker(){
    Exception exception = assertThrows(RuntimeException.class, () -> service.getPrice("INVALID"));
    assertEquals("Invalid Ticker", exception.getMessage());
  }
}
