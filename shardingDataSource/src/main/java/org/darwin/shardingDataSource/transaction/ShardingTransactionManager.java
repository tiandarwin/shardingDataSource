/**
 * org.darwin.shardingDataSource.transaction.ShardingTransactionManger.java
 * created by Tianxin(tianjige@163.com) on 2015年6月15日 下午1:39:43
 */
package org.darwin.shardingDataSource.transaction;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 
 * created by Tianxin on 2015年6月15日 下午1:39:43
 */
public class ShardingTransactionManager extends DataSourceTransactionManager {
  /**
   * 
   * created by Tianxin on 2015年6月15日 下午1:41:36
   */
  private static final long serialVersionUID = 1L;
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(ShardingTransactionManager.class);
  
  @Override
  public void setDataSource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }
}
