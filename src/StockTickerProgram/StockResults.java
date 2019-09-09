package StockTickerProgram;

import java.util.HashMap;
import java.util.Map;

public class StockResults {
  Map<String, Integer> stockPrices;
  Map<String, String> stockErrors;

  public StockResults(){
    stockPrices = new HashMap<>();
    stockErrors = new HashMap<>();
  }

  public Map<String, Integer> getStockPrices() { return stockPrices; }

  public Map<String, String> getStockErrors() { return stockErrors; }
}