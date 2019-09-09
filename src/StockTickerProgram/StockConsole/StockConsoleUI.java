package StockTickerProgram.StockConsole;

import StockTickerProgram.StockPriceService;
import StockTickerProgram.StockReporter;
import StockTickerProgram.StockResults;
import StockTickerProgram.UHStockPriceService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StockConsoleUI {
  private StockReporter stockReporter;
  private StockPriceService stockPriceService;
  private StockResults stockResults;
  private List<String> tickerList;

  StockConsoleUI(String fileName){
    stockReporter = new StockReporter();
    stockPriceService = new UHStockPriceService();

    tickerList = getTickersFromFile(fileName);
    stockReporter.setStockPriceService(stockPriceService);
    stockResults = stockReporter.fetchPrices(tickerList);
  }

  public static void main(String[] args){
    String fileName;
    fileName = getFileName();

    StockConsoleUI stockConsoleUI = new StockConsoleUI(fileName);
    stockConsoleUI.outputSortedTable();
    stockConsoleUI.outputHighestTable();
    stockConsoleUI.outputLowestTable();
    stockConsoleUI.outputErrorSection();
  }

  private void outputErrorSection() {
    Map<String, String> stockErrors = stockResults.getStockErrors();
    System.out.println("Tickers Error Section:");
    stockErrors
      .entrySet()
      .forEach(entry ->
        System.out.println(entry.getKey()));
  }

  private void outputLowestTable() {
    Map<String, Integer> stockPrices = stockResults.getStockPrices();
    Map<String, Integer> lowestStocks = stockReporter.findLowestPrice(stockPrices);

    System.out.println("Tickers with the Lowest Prices:");
    System.out.println("Ticker" + "\t" + "Price");
    outputStockPrices(lowestStocks);

    System.out.println();
  }

  private void outputStockPrices(Map<String, Integer> highestStocks) {
    highestStocks
      .entrySet()
      .forEach(entry ->
        System.out.println(entry.getKey() + "\t" + cashIntToDouble(entry.getValue())));
  }

  private void outputHighestTable() {
    Map<String, Integer> stockPrices = stockResults.getStockPrices();
    Map<String, Integer> highestStocks = stockReporter.findHighestPrice(stockPrices);

    System.out.println("Tickers with the Highest Prices:");
    System.out.println("Ticker" + "\t" + "Price");
    outputStockPrices(highestStocks);

    System.out.println();
  }

  private void outputSortedTable() {
    Map<String, Integer> stockPrices = stockResults.getStockPrices();
    stockPrices = stockReporter.sortStocks(stockPrices);

    System.out.println("Sorted Ticker Table:");
    System.out.println("Ticker" + "\t" + "Price");
    outputStockPrices(stockPrices);

    System.out.println();
  }

  private String cashIntToDouble(Integer value) {
    String cash = Integer.toString(value);
    String dollars = cash.substring(0, cash.length()-2);
    String cents = cash.substring(cash.length()-2);

    return dollars + "." + cents;
  }

  private static String getFileName() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Input file name of file with ticker names: ");
    return scanner.next();
  }

  private static List<String> getTickersFromFile(String fileName) {
    List<String> tickerList = new ArrayList<>();
    try{
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while(bufferedReader.ready()){
        String fileLine = bufferedReader.readLine();
        tickerList.add(fileLine);
      }

      bufferedReader.close();
      fileReader.close();
    }catch(IOException ex){
      throw new RuntimeException("Error reading from file: " + fileName);
    }

    return tickerList;
  }
}
