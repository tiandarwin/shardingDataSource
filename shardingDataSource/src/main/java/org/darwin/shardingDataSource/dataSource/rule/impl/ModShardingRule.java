/**
 * org.darwin.shardingDataSource.dataSource.rule.impl.ModShardingRule.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 下午12:09:31
 */
package org.darwin.shardingDataSource.dataSource.rule.impl;

import org.darwin.common.utils.ThreadContext;
import org.darwin.shardingDataSource.dataSource.rule.ShardingRule;

/**
 * 根据shardKey取模的拆库规则
 * created by Tianxin on 2015年6月8日 下午12:09:31
 */
public class ModShardingRule implements ShardingRule {

  public int getCurrentShardingIndex() {

    if (shardCount <= 1) {
      throw new RuntimeException("切片数量小于2，不能使用切片数据源");
    }

    int iShardingKey = 0;
    if (shardingKey == null) {
      iShardingKey = ThreadContext.getShardingKey();
    } else {
      iShardingKey = ThreadContext.get(shardingKey);
    }

    return iShardingKey % shardCount;
  }

  /**
   * 切片key在上下文中的存储的key
   */
  private String shardingKey = "shardingKey";

  /**
   * 数据库的切片数量
   */
  private int shardCount = 0;

  public void setShardingKey(String shardingKey) {
    this.shardingKey = shardingKey;
  }

  public void setShardCount(int shardCount) {
    this.shardCount = shardCount;
  }

  public int getShardCount() {
    return shardCount;
  }

}
