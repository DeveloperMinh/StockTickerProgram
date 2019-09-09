package StockTickerProgram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class StockReporterTest {
  private StockReporter stockReporter;
  private StockPriceService stockPriceService;

  @BeforeEach
  void init() {
    stockReporter = new StockReporter();
    stockPriceService = Mockito.mock(StockPriceService.class);
    stockReporter.setStockPriceService(stockPriceService);
  }

  @Test
  void canary() {
    assertTrue(true);
  }

  @Test
  void getHighestPriceForEmptyData() {
    assertEquals(Map.of(),
      stockReporter.findHighestPrice(
        Map.of()));
  }

  @Test
  void getHighestPriceForDataWithOneHighPrice() {
    assertEquals(Map.of("GOOG", 100),
      stockReporter.findHighestPrice(
        Map.of("AMZN", 99, "GOOG", 100)));
  }

  @Test
  void getHighestPriceForDataWithTwoHighPrice() {
    assertEquals(Map.of("GOOG", 100, "INTC", 100),
      stockReporter.findHighestPrice(
        Map.of("GOOG", 100, "AMZN", 99, "INTC", 100)));
  }

  @Test
  void getLowestPriceForEmptyData() {
    assertEquals(Map.of(),
      stockReporter.findLowestPrice(
        Map.of()));
  }

  @Test
  void getLowestPriceForDataWithOneLowPrice() {
    assertEquals(Map.of("AMZN", 99),
      stockReporter.findLowestPrice(
        Map.of("AMZN", 99, "GOOG", 100)));
  }

  @Test
  void getLowestPriceForDataWithTwoLowPrice() {
    assertEquals(Map.of("AMZN", 99, "INTC", 99),
      stockReporter.findLowestPrice(
        Map.of("GOOG", 100, "AMZN", 99, "INTC", 99)));
  }

  @Test
  void getSortedStocksForEmptyData() {
    assertEquals(Map.of(),
      stockReporter.sortStocks(Map.of()));
  }

  @Test
  void getSortedStocksForManyData() {
    assertEquals(List.of("AMZN", "GOOG", "INTC", "MSFT"),
      stockReporter.sortStocks(
        Map.of("MSFT", 10, "GOOG", 20, "AMZN", 5, "INTC", 100))
        .keySet()
        .stream()
        .collect(toList())
    );
  }

  @Test
  void fetchPricesForNoTickers() {
    StockResults stockResults = stockReporter.fetchPrices(List.of());

    assertEquals(Map.of(), stockResults.stockPrices);
    assertEquals(Map.of(), stockResults.stockErrors);
  }

  @Test
  void fetchPricesForOneTicker() {
    when(stockPriceService.getPrice("GOOG")).thenReturn(100);

    StockResults stockResults = stockReporter.fetchPrices(List.of("GOOG"));

    assertEquals(Map.of("GOOG", 100), stockResults.stockPrices);
    assertEquals(Map.of(), stockResults.stockErrors);
  }

  @Test
  void fetchPricesForTwoTickers() {
    when(stockPriceService.getPrice("GOOG")).thenReturn(100);
    when(stockPriceService.getPrice("AMZN")).thenReturn(200);

    StockResults stockResults = stockReporter.fetchPrices(List.of("GOOG", "AMZN"));

    assertEquals(Map.of("GOOG", 100, "AMZN", 200), stockResults.stockPrices);
    assertEquals(Map.of(), stockResults.stockErrors);
  }

  @Test
  void fetchPricesForOneTickerWithError() {
    when(stockPriceService.getPrice("INVALID")).thenThrow(new RuntimeException("Invalid Ticker"));

    StockResults stockResults =
      stockReporter.fetchPrices(List.of("INVALID"));

    assertEquals(Map.of(), stockResults.stockPrices);
    assertEquals(Map.of("INVALID", "Invalid Ticker"), stockResults.stockErrors);
  }

  @Test
  void fetchPricesForThreeTickersWhenTwoTickersHaveErrors() {
    when(stockPriceService.getPrice("AMZN")).thenReturn(200);
    when(stockPriceService.getPrice("GOOG"))
      .thenThrow(new RuntimeException("Network Error"));
    when(stockPriceService.getPrice("INVALID"))
      .thenThrow(new RuntimeException("Invalid Ticker"));

    StockResults stockResults =
      stockReporter.fetchPrices(List.of("GOOG", "AMZN", "INVALID"));

    assertEquals(Map.of("AMZN", 200), stockResults.stockPrices);
    assertEquals(Map.of("GOOG", "Network Error", "INVALID", "Invalid Ticker"),
      stockResults.stockErrors);
  }
}