/**
 * org.darwin.shardingDataSource.dataSource.rule.ByOrderSelector.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午11:11:01
 */
package org.darwin.shardingDataSource.dataSource.rule.impl;

import org.darwin.shardingDataSource.dataSource.rule.SlaveSelector;

/**
 * 顺序选取的选取器 created by Tianxin on 2015年6月8日 上午11:11:01
 */
public class ByOrderSelector implements SlaveSelector {
	
	private ByOrderSelector() {
	}

	/**
	 * @param slaveCount
	 */
	public ByOrderSelector(int slaveCount) {
		this();
		this.slaveCount = slaveCount;
	}

	/**
	 * 从库的数量
	 */
	private int slaveCount;

	/**
	 * 上一次的index
	 */
	private int lastIndex = 0;

	/*
	 * 由于这个顺序不会影响到正确性，所以不做synchronized的限定，高并发时允许有重复
	 */
	public int getCurrentIndex() {
		if (lastIndex == Integer.MAX_VALUE) {
			lastIndex = 0;
		} else {
			lastIndex += 1;
		}
		return lastIndex % slaveCount;
	}

}
