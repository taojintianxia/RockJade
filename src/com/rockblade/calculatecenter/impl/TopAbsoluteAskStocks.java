package com.rockblade.calculatecenter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rockblade.model.Stock;

/**
 * 
 * 
 * @author Kane.Sun
 * @version Dec 6, 2013 1:38:26 PM
 * 
 */

public class TopAbsoluteAskStocks extends AbstractTopNCalculator {

	@Override
	public List<String> getTopStocks(int n, Map<String, List<Stock>> stocksMap) {
		Map<String, Double> absoluteAskStockMap = new HashMap<>();
		List<String> topAbsoluteAskStocksId = new ArrayList<>();
		for (Map.Entry<String, List<Stock>> entry : stocksMap.entrySet()) {
			String stockId = entry.getKey();
			List<Stock> stocks = entry.getValue();
			if (!stocks.isEmpty()) {
				int stockSize = stocks.size();
				Stock lastStock = stocks.get(stockSize - 1);
				double askAmount = lastStock.getAsk1Price() * lastStock.getAsk1Volume();
				double bidAmount = lastStock.getBid1Price() * lastStock.getBid1Volume();
				absoluteAskStockMap.put(stockId, askAmount - bidAmount);
			}
		}

		topAbsoluteAskStocksId = getTopNByMapValueInRevertedSequence(topNum, absoluteAskStockMap);

		return topAbsoluteAskStocksId;
	}

	@Override
	public List<String> getTopStocks(Map<String, List<Stock>> stocksMap) {
		return getTopStocks(N, stocksMap);
	}

}
