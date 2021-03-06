package com.rockblade.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rockblade.model.Stock;

/**
 * 
 * 
 * @author Kane.Sun
 * @version Sep 9, 2013 1:00:24 PM
 * 
 */

public class StockCache {

	public final static int SH_STOCK_CACHE_NUM = 1000;
	public final static int ZH_STOCK_CACHE_NUM = 1500;

	public static Calendar lastPersistenceTime = Calendar.getInstance();

	// since every fixed time , there will be some stock data stores to DB.this
	// map stores the index of every stock.
	public static Map<String, Integer[]> allStocksPersistenceIndexer = new HashMap<>();

	public static final Map<String, List<Stock>> ALL_STOCKS_CACHE = new ConcurrentHashMap<>();

	public static final Map<String, Boolean> ALL_STOCK_PERSISTENCE_IDENTIFIER = new ConcurrentHashMap<>();

	public static final List<String> ALL_STOCK_ID = new ArrayList<>();

	public static void cleanCache() {
		allStocksPersistenceIndexer.clear();
		ALL_STOCKS_CACHE.clear();
		ALL_STOCK_PERSISTENCE_IDENTIFIER.clear();
	}

	public static Map<String, List<Stock>> getStocksInPreviousTime(Map<String, List<Stock>> allStocksCache, long timeInterval) {
		Map<String, List<Stock>> expectStocksMap = new HashMap<>();
		Calendar expectTime = Calendar.getInstance();
		expectTime.setTimeInMillis(expectTime.getTimeInMillis() - timeInterval);
		for (Map.Entry<String, List<Stock>> entry : allStocksCache.entrySet()) {
			String stockId = entry.getKey();
			List<Stock> stockList = entry.getValue();
			int lastStockIndex = stockList.size() - 1;
			List<Stock> stocksInTimeInterval = new ArrayList<>();
			if (stockList != null) {
				for (int i = lastStockIndex; i >= 0; i--) {
					if (stockList.get(i).getTime().after(expectTime)) {
						stocksInTimeInterval.add(stockList.get(i));
					}
				}

				// raising limit or limit down
				if (stocksInTimeInterval.isEmpty() && !stockList.isEmpty()) {
					stocksInTimeInterval.add(stockList.get(stockList.size() - 1));
				}

				if (!stocksInTimeInterval.isEmpty()) {
					expectStocksMap.put(stockId, stocksInTimeInterval);
				}
			}
		}

		return expectStocksMap;
	}
}
