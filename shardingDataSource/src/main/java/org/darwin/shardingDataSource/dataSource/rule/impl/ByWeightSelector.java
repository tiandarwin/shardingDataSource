/**
 * org.darwin.shardingDataSource.dataSource.rule.ByWeightSelector.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午11:15:10
 */
package org.darwin.shardingDataSource.dataSource.rule.impl;

import org.darwin.shardingDataSource.dataSource.rule.SlaveSelector;

/**
 * 根据权重的选择器。这里的权重选择比较简单，目前是折合成整数后依次将每一个数据源的配额耗光以后再进行一轮消耗。
 * 如果权重配置为100,200,300，则会依次使用1次，2次，3次。但同一个数据源的几次选取是连续的
 * created by Tianxin on 2015年6月8日 上午11:15:10
 */
public class ByWeightSelector implements SlaveSelector {

	private ByWeightSelector() {
	}

	public ByWeightSelector(int[] weights) {
		this();
		this.weights = weights;
		this.slaveCount = weights.length;
		
		//获取最小公约数，避免连续过多
		int maxDevide = getMaxDevideByOujilide(weights);
		if(maxDevide != 1){
			for(int i = 0 ; i < weights.length ; i ++){
				weights[i] = weights[i] / maxDevide;
			}
		}
	}

	/**
	 * 对数据源的描述
	 */
	private int[] weights;

	/**
	 * 数据源的个数
	 */
	private int slaveCount = 0;

	/**
	 * 指针当前指向的数据源
	 */
	private int currentIndex = 0;

	/**
	 * 当前节点的权重已经消耗了多少
	 */
	private int weightCost = 0;

	public int getCurrentIndex() {

		// 当前节点的权重大小，将要达到的使用次数
		int top = weights[currentIndex];
		weightCost += 1;

		// 如果达到了top，则轮转下一个节点
		if (weightCost == top) {
			weightCost = 0;
			if (currentIndex == slaveCount) {
				currentIndex = 0;
			} else {
				currentIndex += 1;
			}
		}
		return currentIndex;
	}

	/**
	 * 获取两个数的最大公约数
	 * 
	 * @param a
	 * @param b
	 * @return created by Tianxin on 2015年6月8日 上午11:30:53
	 */
	public static int getMaxDevideByOujilide(int a, int b) {
		if (a < b) {
			int temp;
			temp = a;
			a = b;
			b = temp;
		}
		if (0 == b) {
			return a;
		}
		return getMaxDevideByOujilide(b, a % b);
	}

	/**
	 * 获取多个数的最大公约数
	 * 
	 * @param a
	 * @param b
	 * @return created by Tianxin on 2015年6月8日 上午11:30:53
	 */
	public static int getMaxDevideByOujilide(int[] is) {
		int maxDevide = 0;
		for(int i = 0; i < is.length; i ++){
			maxDevide = getMaxDevideByOujilide(maxDevide, is[i]);
		}
		return maxDevide;
	}
}
