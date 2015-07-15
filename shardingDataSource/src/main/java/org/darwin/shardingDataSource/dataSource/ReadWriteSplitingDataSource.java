/**
 * org.darwin.shardingDataSource.dataSource.ReadWriteSplitingDataSource.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午10:13:58
 */
package org.darwin.shardingDataSource.dataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.darwin.common.utils.ThreadContext;
import org.darwin.shardingDataSource.dataSource.rule.SlaveSelector;
import org.darwin.shardingDataSource.dataSource.rule.impl.ByOrderSelector;
import org.darwin.shardingDataSource.dataSource.rule.impl.ByWeightSelector;
import org.darwin.shardingDataSource.dataSource.rule.impl.RandomSelector;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * 数据库作为读写分离的数据源
 * created by Tianxin on 2015年6月8日 上午10:13:58
 */
public class ReadWriteSplitingDataSource extends AbstractDataSource{

	public Connection getConnection() throws SQLException {
		return chooseSuitableDataSource().getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return chooseSuitableDataSource().getConnection(username, password);
	}

	/**
	 * 选择合适的数据源,该方法确保返回不为空
	 * @return
	 * created by Tianxin on 2015年6月8日 上午10:28:05
	 */
	private final DataSource chooseSuitableDataSource(){
		boolean isWrite = (slaveDataSources == null || isWriteOperation());
		if(isWrite){
			return masterDataSource;
		}
		
		//如果是读操作，则获取相应的读库
		return getOneSlaveDataSource();
	}
	

	/**
	 * 获取一个读库.程序里面基本都不会关心用哪个从库，只是有时候需要按照权重进行分离，所以这里考虑到权重的问题即可.
	 * @return
	 * created by Tianxin on 2015年6月8日 上午10:49:22
	 */
	private DataSource getOneSlaveDataSource() {
		
		int currentIndex = getCurrentIndex();
		return slaveDataSources.get(currentIndex);
	}


	/**
	 * 获取本次要使用的数据源的index
	 * @return
	 * created by Tianxin on 2015年6月8日 上午10:52:46
	 */
	private int getCurrentIndex() {
		if(selector == null){
			
			switch (slaveRoundType) {
			case RANDOM:
				selector = new RandomSelector(slaveDataSources.size());
				break;
			case BY_ORDER:
				selector = new ByOrderSelector(slaveDataSources.size());
				break;
			case BY_WEIGHT:
				selector = new ByWeightSelector(weights);
				break;
			default:
				selector = new RandomSelector(slaveDataSources.size());
				break;
			}
			
		}
		return selector.getCurrentIndex();
	}
	
	/**
	 * 主库的数据源
	 */
	private DataSource masterDataSource;
	
	/**
	 * 从库的数据源列表。
	 */
	private List<DataSource> slaveDataSources;
	
	/**
	 * 从库的选取策略
	 */
	private int slaveRoundType;
	
	/**
	 * 权重的数组
	 */
	private int[] weights;
	
	/**
	 * 从库的选取策略
	 */
	private SlaveSelector selector = null;
	
	public void setMasterDataSource(DataSource masterDataSource) {
		this.masterDataSource = masterDataSource;
	}
	
	public void setSlaveDataSources(List<DataSource> slaveDataSources) {
		this.slaveDataSources = new ArrayList<DataSource>(slaveDataSources);
	}
	
	/**
	 * 判断当前是否是写操作
	 * @return
	 * created by Tianxin on 2015年6月8日 上午10:41:02
	 */
	private final static boolean isWriteOperation(){
		String optType = ThreadContext.get(DataSourceConstants.DB_OPT_TYPE);
		
		//无type时，默认走主库
		if(optType == null){
			return true;
		}
		
		//根据上下文中的类型判断是读库还是写库
		return DataSourceConstants.WRITE.equals(optType.toLowerCase());
	}

	/**
	 * 设置从库的轮转策略.三种轮转策略：0为随机，1为顺序，2为按权重
	 * @param type
	 * created by Tianxin on 2015年6月8日 上午11:41:46
	 */
	public void setSlaveRoundType(int type){
		this.slaveRoundType = type;
	}
	
	/**
	 * 设置从库的权重。以逗号进行分隔，如:1,2,3
	 * @param type
	 * created by Tianxin on 2015年6月8日 上午11:41:46
	 */
	public void setSlaveWeights(String weights){
		
		if(weights == null || weights.length() == 0){
			return;
		}
		
		weights = weights.replace('，', ',');
		String[] sArray = weights.split(",");

		//将其转换为权重数组
		int [] iArray = new int[sArray.length];
		for(int i = 0 ; i < sArray.length ; i ++){
			iArray[i] = Integer.parseInt(sArray[i].trim());
		}
		this.weights = iArray;
	}
	
	private final static int RANDOM = 0;
	private final static int BY_ORDER = 1;
	private final static int BY_WEIGHT = 2;
}
