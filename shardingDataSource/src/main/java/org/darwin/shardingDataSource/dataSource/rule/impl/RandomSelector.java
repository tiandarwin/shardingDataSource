/**
 * org.darwin.shardingDataSource.dataSource.rule.RandomSelector.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午11:06:38
 */
package org.darwin.shardingDataSource.dataSource.rule.impl;

import java.util.Random;

import org.darwin.shardingDataSource.dataSource.rule.SlaveSelector;

/**
 * 从库的随机选择器
 * created by Tianxin on 2015年6月8日 上午11:06:38
 */
public class RandomSelector implements SlaveSelector {
	
	private RandomSelector() {
	}

	/**
	 * @param slaveCount
	 */
	public RandomSelector(int slaveCount) {
		this();
		this.slaveCount = slaveCount;
	}

	/**
	 * 从库的数量
	 */
	private int slaveCount;
	
	/**
	 * 随机选取器
	 */
	private Random random = new Random();

	public int getCurrentIndex() {
		return random.nextInt(slaveCount);
	}

}
