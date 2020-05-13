package com.atguigu.java8;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

import org.junit.Test;

/*
@author author ZFL:
@version time:2020年2月28日下午4:20:21
*/
public class TestFJ {
	
	Instant start = Instant.now();
	
	@Test
	public void test() {//11个0   23S;  10 3604  
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Long> task = new MyForkJoinCalculate(0, 10000000000L);
		Long sum = pool.invoke(task);
		System.out.println(sum);
		Instant end = Instant.now();
		System.out.println("耗时==="+Duration.between(start, end).toMillis());
	}//3327
	
	@Test
	public void test23() {//11个0 34S ;10  3294ms
		long sum=0L;
		for (long i = 0; i < 10000000000L; i++) {
			sum+=i;
			
		}
		System.out.println(sum);//3887
		Instant end = Instant.now();
		System.out.println("耗时==="+Duration.between(start, end).toMillis());
		
	}
	
	@Test
	public void test3() {
		Instant now = Instant.now();
		LongStream.rangeClosed(0, 1000000000000L)
				.parallel()
				.reduce(0,Long::sum);
		
		Instant end = Instant.now();
		System.out.println("耗时==="+Duration.between(start, end).toMillis());//10个 1621
		
	}

}
