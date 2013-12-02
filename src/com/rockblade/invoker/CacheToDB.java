package com.rockblade.invoker;

import java.util.Calendar;
import java.util.TimerTask;

import com.rockblade.cache.StockCache;
import com.rockblade.persistence.impl.CacheToDBPersistence;
import com.rockblade.util.StockUtil;

/**
 * 
 * 
 * @author Kane.Sun
 * @version Dec 2, 2013 1:55:29 PM
 * 
 */

public class CacheToDB extends TimerTask {

	@Override
	public void run() {

		try {
			StockCache.ALL_STOCK_ID.add("002024");
			StockCache.ALL_STOCK_ID.add("600598");
			final CacheToDBPersistence cacheToDB = new CacheToDBPersistence();
			while (true) {
				if (StockUtil.isInTradingTime()) {
					cacheToDB.saveStocks(StockCache.ALL_STOCKS_CACHE);
					if (StockUtil.isInMiddayNoneTradingTime(StockCache.persisFinishedTime)) {
						StockCache.cleanCache();
					}
					Thread.sleep(10 * StockUtil.MINUTE);
				} else if (StockUtil.isInMiddayNoneTradingTime()) {
					Calendar currentTime = Calendar.getInstance();
					Thread.sleep(StockUtil.AFTERNOON_START.getTimeInMillis() - currentTime.getTimeInMillis());
				} else {
					System.out.println("finished , not trading time");
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
