package com.atguigu.java8;

@FunctionalInterface
public interface MyPredicate<T> {

	/**
	 * @param t
	 * @return
	 */
	public boolean test(T t);
	
}
