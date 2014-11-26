package edu.smu.os.thread;

import java.util.Random;

import edu.smu.algorithm.sort.MergeSort;
import edu.smu.exceptions.ThreadNumException;

class SortThread implements Runnable
{
	private int left;
	private int right;
	private int[] arr;

	public SortThread(int[] arr, int left, int right)
	{
		this.left = left;
		this.right = right;
		this.arr = arr;
	}

	@Override
	public void run()
	{
		MergeSort.Sort(arr, left, right);
	}
}

public class ParallelMergeSort
{
	private int[] GenerateRandomNum(int length, int max)
	{
		Random rd = new Random();
		int[] arr = new int[length];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = rd.nextInt(max);
		}
		return arr;
	}

	/**
	 * single thread function
	 * 
	 * @param arrSize
	 */
	public long SingleThread(int arrSize, boolean showResults)
	{
		int[] arr = GenerateRandomNum(arrSize, arrSize);

		// start sort
		long startTime = System.currentTimeMillis();
		MergeSort.Sort(arr, 0, arr.length - 1);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		if (showResults)
		{
			for (int i = 0; i < arr.length; i++)
			{
				System.out.println(arr[i]);
			}
			System.out.println("Single thread performance: \t" + totalTime
					+ "ms");
		}
		return totalTime;
	}

	/**
	 * multiple thread function
	 * 
	 * @param arrSize
	 * @param threadCount
	 * @throws ThreadNumException
	 */
	public long MultiThread(int arrSize, int threadCount, boolean showResults)
			throws ThreadNumException
	{
		if (threadCount < 1 || arrSize < threadCount)
		{
			throw new ThreadNumException("Num is illegal!");
		}

		int[] arr = GenerateRandomNum(arrSize, arrSize);
		Thread[] runnable = new Thread[threadCount];

		// create threads
		int left = 0, right = arrSize / threadCount - 1;
		for (int i = 0; i < runnable.length; i++)
		{
			if (threadCount - 1 == i)
				right = arrSize - 1;
			runnable[i] = new Thread(new SortThread(arr, left, right));
			left = right + 1;
			right = arrSize / threadCount * (i + 2) - 1;

		}

		// start to run each thread
		long startTime1 = System.currentTimeMillis();
		for (Thread thread : runnable)
		{
			thread.start();
		}

		// wait the sub threads to be done
		try
		{
			for (Thread thread : runnable)
			{
				thread.join();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		left = 0;
		right = arrSize / threadCount - 1;

		// merge the results
		for (int i = 0; i < runnable.length; i++)
		{
			if (threadCount - 1 == i)
				right = arrSize - 1;
			MergeSort.Merge(arr, 0, left - 1, right);
			left = right + 1;
			right = arrSize / threadCount * (i + 2) - 1;

		}

		// statistic the results
		long endTime1 = System.currentTimeMillis();

		long totalTime = endTime1 - startTime1;
		// show results
		if (showResults)
		{
			for (int i = 0; i < arr.length; i++)
			{
				System.out.println(arr[i]);
			}
			System.out.println("Multithread(" + threadCount
					+ ") performance: \t" + totalTime + "ms");
		}
		return totalTime;
	}

}
