package com.rockblade.calculatecenter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.rockblade.model.Stock;
import com.rockblade.util.StockUtil;

/**
 * 
 * 
 * @author Kane.Sun
 * @version Nov 28, 2013 3:21:17 PM
 * 
 */

public class TopPercentGrowthStocks extends AbstractTopNCalculator {

	@Override
	public List<Stock> getTopStocks(int n, Map<String, List<Stock>> stocksMap) {
		Map<String, Double> stockPercentDifferenceMap = new LinkedHashMap<>();
		List<String> topStockIdsList = new ArrayList<>(n);
		List<Stock> topStocksList = new ArrayList<>(n);
		for (Entry<String, List<Stock>> entry : stocksMap.entrySet()) {
			String stockId = entry.getKey();
			List<Stock> stockList = entry.getValue();
			int stockSize = stockList.size();
			Double totalPercentDifference = -10.0;
			if (stockSize > 1) {
				for (int i = 1; i < stockSize; i++) {
					totalPercentDifference += stockList.get(i).getPercent() - stockList.get(i - 1).getPercent();
				}
				stockPercentDifferenceMap.put(stockId, totalPercentDifference);
			} else if (!stockList.isEmpty()) {
				stockPercentDifferenceMap.put(stockId, stockList.get(0).getPercent());
			}
		}

		topStockIdsList = getTopNByMapValueInRevertedSequence(topNum, stockPercentDifferenceMap);

		if (topStockIdsList.size() != n) {
			System.err.println("What the hell ?!");
		}
		for (int i = 0; i < n; i++) {
			int size = stocksMap.get(topStockIdsList.get(i)).size();
			topStocksList.add(stocksMap.get(topStockIdsList.get(i)).get(size - 1));
		}

		return topStocksList;
	}

	@Override
	public List<Stock> getTopStocks(Map<String, List<Stock>> stocksMap) {
		return getTopStocks(N, stocksMap);
	}

	public static void main(String... args) {
		TopPercentGrowthStocks calculator = new TopPercentGrowthStocks();
		Map<String, List<Stock>> testMap = new HashMap<>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			String id = random.nextInt(700000) + "";
			List<Stock> tempList = new ArrayList<>();
			for (int j = 0; j < random.nextInt(3) + 2; j++) {
				Stock stock = StockUtil.getInitializedBlankStock();
				stock.setPercent(random.nextInt(10));
				if (random.nextBoolean()) {
					stock.setPercent(stock.getPercent() - 10);
				}
				stock.setStockId(id);
				tempList.add(stock);
			}
			testMap.put(id, tempList);
		}

		System.out.println("the test stock are as followings: ");

		for (Map.Entry<String, List<Stock>> entry : testMap.entrySet()) {
			List<Stock> stocksList = entry.getValue();
			System.out.print(" the stock " + entry.getKey() + " values are : ");
			for (Stock stock : stocksList) {
				System.out.print(stock.getPercent() + " , ");
			}
			System.out.print(" the differ is : " + (stocksList.get(stocksList.size() - 1).getPercent() - stocksList.get(0).getPercent()));
			System.out.println();
		}

		List<Stock> targetStockList = new ArrayList<>();
		targetStockList = calculator.getTopStocks(5, testMap);
		System.out.println("the top 5 are : ");
		for (Stock stock : targetStockList) {
			System.out.print(stock.getStockId() + " , ");
		}

	}
}