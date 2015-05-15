package com.codemineral.scheduler;

import java.util.BitSet;

public class BloomFilter implements Filter {
	private static final int DEFAULT_SIZE = 2 << 24;
	/* 不同哈希函数的种子，一般应取质数,seeds数据共有7个值，则代表采用7种不同的HASH算法 */
	private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };

	private BitSet bits = new BitSet(DEFAULT_SIZE);
	/* 哈希函数对象 */
	private SimpleHash[] func = new SimpleHash[seeds.length];

	public BloomFilter() {
		this(DEFAULT_SIZE);
	}

	public BloomFilter(int capacity) {
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(capacity, seeds[i]);
		}
	}

	// 将字符串标记到bits中，即设置字符串的7个hash值函数为1
	public void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
	}

	//判断字符串是否已经被bits标记
	public boolean contains(String value) {
		//确保传入的不是空值
		if (value == null) {
			return false;
		}
		boolean ret = true;
		//计算7种hash算法下各自对应的hash值，并判断
		for (SimpleHash f : func) {
			//&&是boolen运算符，只要有一个为0，则为0。即需要所有的位都为1，才代表包含在里面。
			//f.hash(value)返回hash对应的位数值
			//bits.get函数返回bitset中对应position的值。即返回hash值是否为0或1。
			ret = ret && bits.get(f.hash(value));
		}
		return ret;
	}

	/* 哈希函数类 */
	public static class SimpleHash {
		//cap为DEFAULT_SIZE的值，即用于结果的最大的字符串长度。
		//seed为计算hash值的一个给定key，具体对应上面定义的seeds数组
		private int capacity;
		private int seed;

		public SimpleHash(int capacity, int seed) {
			this.capacity = capacity;
			this.seed = seed;
		}

		//计算hash值的具体算法,hash函数，采用简单的加权和hash
		public int hash(String value) {
			//int的范围最大是2的31次方减1，或超过值则用负数来表示
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				//数字和字符串相加，字符串转换成为ASCII码
				result = seed * result + value.charAt(i);
			}

			//&是java中的位逻辑运算，用于过滤负数（负数与进算转换成反码进行）。
			return (capacity - 1) & result;
		}
	}
}
