package com.atguigu.java8;

import java.util.Iterator;
import java.util.concurrent.RecursiveTask;

/*
@author author ZFL:
@version time:2020年2月28日下午4:07:00
*/
public class MyForkJoinCalculate extends RecursiveTask<Long>{
	
	
	
	public MyForkJoinCalculate(long start, long end) {
		super();
		this.start = start;
		this.end = end;
	}

	public MyForkJoinCalculate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 15607855052L;
	
	private long start;
	private long end;
	
	private static final long THRESHOLD=10000;
	
	@Override
	protected Long compute() {
		long length=end-start;
		if(length<=THRESHOLD) {
			long sum=0;
			for (long i = start; i <=end; i++) {
				sum+=i;
			}
			return sum;
		}else {
			long middle=(start+end)/2;
			MyForkJoinCalculate left = new MyForkJoinCalculate(start,middle);
			left.fork();  //拆封子任务，同时压入线程队列
			
			MyForkJoinCalculate right = new MyForkJoinCalculate(middle+1,end);
			right.fork();
			
			return left.join()+right.join();
			
		}
	}
	

}
