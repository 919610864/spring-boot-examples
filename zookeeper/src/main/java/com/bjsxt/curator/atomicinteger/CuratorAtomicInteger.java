package com.bjsxt.curator.atomicinteger;

import com.bjsxt.common.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;


public class CuratorAtomicInteger {

	/** zookeeper地址 */
	static String CONNECT_ADDR ;
	/** session超时时间 */
	static final int SESSION_OUTTIME = 5000;//ms

	static {
		CONNECT_ADDR = (String) PropertiesUtil.readPropery().get("zookeeper.url");
	}
	
	public static void main(String[] args) throws Exception {
		
		//1 重试策略：初试时间为1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
		//2 通过工厂创建连接
		CuratorFramework cf = CuratorFrameworkFactory.builder()
					.connectString(CONNECT_ADDR)
					.sessionTimeoutMs(SESSION_OUTTIME)
					.retryPolicy(retryPolicy)
					.build();
		//3 开启连接
		cf.start();
		//cf.delete().forPath("/super");
		

		//4 使用DistributedAtomicInteger
		DistributedAtomicInteger atomicIntger =
				new DistributedAtomicInteger(cf, "/super", new RetryNTimes(3, 1000));
		
		AtomicValue<Integer> value = atomicIntger.add(1);
		System.out.println(value.succeeded());
		System.out.println("最新值="+value.postValue());	//最新值
		System.out.println("原始值="+value.preValue());	//原始值
		
	}
}
