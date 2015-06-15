/**
 * org.darwin.shardingDataSource.dataSource.rule.SlaveSelector.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午10:58:48
 */
package org.darwin.shardingDataSource.dataSource.rule;

/**
 * 从库的选取规则
 * created by Tianxin on 2015年6月8日 上午10:58:48
 */
public interface SlaveSelector {

	/**
	 * 获取本次选取的从库的index
	 * @return
	 * created by Tianxin on 2015年6月8日 上午10:59:15
	 */
	int getCurrentIndex();
}
