package StockTickerProgram;

import java.net.*;
import java.io.*;

public class UHStockPriceService implements StockPriceService {
  public int getPrice(String ticker) {
    int tickerPrice;
    try {
      String httpUrl = "http://agile.cs.uh.edu/stock?ticker=" + ticker;
      URL url;
      URLConnection urlConnection;
      BufferedReader bufferedReader;

      url = new URL(httpUrl);
      urlConnection = url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String stringPrice = bufferedReader.readLine();
      Double tickerPriceDouble = Double.parseDouble(stringPrice) * 100;
      tickerPrice = tickerPriceDouble.intValue();
      bufferedReader.close();
    }catch (IOException ex) {
      throw new RuntimeException("Invalid Ticker");
    }

    return tickerPrice;
  }
}