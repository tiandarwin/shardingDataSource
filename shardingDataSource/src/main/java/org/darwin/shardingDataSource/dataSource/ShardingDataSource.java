/**
 * org.darwin.shardingDataSource.dataSource.ShardingDataSource.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午10:13:07
 */
package org.darwin.shardingDataSource.dataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.darwin.shardingDataSource.dataSource.rule.ShardingRule;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * 数据库分shard的数据源
 * created by Tianxin on 2015年6月8日 上午10:13:07
 */
public class ShardingDataSource extends AbstractDataSource {

  public Connection getConnection() throws SQLException {
    return chooseSuitableDataSource().getConnection();
  }

  public Connection getConnection(String username, String password) throws SQLException {
    return chooseSuitableDataSource().getConnection(username, password);
  }

  /**
   * 数据源列表
   */
  private List<DataSource> shardingDataSources = null;

  private int shardCount = 0;

  public void setShardingDataSources(List<DataSource> shardingDataSources) {

    if (shardingDataSources == null || shardingDataSources.size() <= 1) {
      throw new RuntimeException("不能使用切片的数据源,数据源个数比需要大于1! it is " + (shardingDataSources == null ? "null" : shardingDataSources.size()));
    }

    this.shardingDataSources = new ArrayList<DataSource>(shardingDataSources);
    this.shardCount = shardingDataSources.size();
    checkShardingConfig();
  }

  /**
   * 选择合适的数据源,该方法确保返回不为空
   * @return
   * created by Tianxin on 2015年6月8日 上午10:28:05
   */
  private final DataSource chooseSuitableDataSource() {

    int index = shardingRule.getCurrentShardingIndex();
    if (index >= shardCount || index < 0) {
      throw new RuntimeException("shardingRule返回的当前切片index不合法：" + index);
    }
    return shardingDataSources.get(index);
  }

  /**
   * sharding的规则
   */
  private ShardingRule shardingRule = null;

  public void setShardingRule(ShardingRule shardingRule) {
    this.shardingRule = shardingRule;
    checkShardingConfig();
  }

  /**
   * 校验sharding的配置是否合法
   * created by Tianxin on 2015年6月8日 下午12:20:26
   */
  private void checkShardingConfig() {

    //可能是中间状态，还没完成初始化
    if (shardingDataSources == null || shardingRule == null) {
      return;
    }

    int ruleCount = shardingRule.getShardCount();
    if (ruleCount != shardCount) {
      throw new RuntimeException("规则配置的切片数量与数据源中的切片数量不匹配!");
    }
  }

}
