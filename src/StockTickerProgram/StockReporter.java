package StockTickerProgram;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class StockReporter {
  private StockPriceService stockPriceService;

  public void setStockPriceService(StockPriceService aService) {
    stockPriceService = aService;
  }

  public Map<String, Integer> findHighestPrice(
    Map<String, Integer> stockPrices) {
      
    int highestPrice = stockPrices.isEmpty() ? 0 :
      Collections.max(stockPrices.values());

    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == highestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }

  public Map<String, Integer> findLowestPrice(
    Map<String, Integer> stockPrices) {

    int lowestPrice = stockPrices.isEmpty() ? 0 :
      Collections.min(stockPrices.values());

    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == lowestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }

  public Map<String, Integer> sortStocks(Map<String, Integer> stockPrices) {
    return new TreeMap<>(stockPrices);
  }

  public StockResults fetchPrices(List<String> tickersList) {
    StockResults stockResults = new StockResults();

    for(int i = 0; i < tickersList.size(); i++){
      String ticker = tickersList.get(i);
      try{
        stockResults.stockPrices.put(ticker, stockPriceService.getPrice(ticker));
      }catch (Exception ex){
        stockResults.stockErrors.put(ticker, ex.getMessage());
      }
    }

    return stockResults;
  }
}