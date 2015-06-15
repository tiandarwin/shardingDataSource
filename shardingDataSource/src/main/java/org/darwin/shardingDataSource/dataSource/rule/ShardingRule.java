/**
 * org.darwin.shardingDataSource.dataSource.rule.ShardingRule.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 下午12:03:55
 */
package org.darwin.shardingDataSource.dataSource.rule;

/**
 * 数据库的切片规则
 * created by Tianxin on 2015年6月8日 下午12:03:55
 */
public interface ShardingRule {

	/**
	 * 获取本次操作的数据库切片号
	 * @return
	 * created by Tianxin on 2015年6月8日 下午12:04:19
	 */
	int getCurrentShardingIndex();

	/**
	 * 获取配置的切片数量
	 * @return
	 * created by Tianxin on 2015年6月8日 下午12:23:44
	 */
	int getShardCount();
}
