package edu.smu.os.thread.test;

import edu.smu.exceptions.ThreadNumException;
import edu.smu.os.thread.ParallelMergeSort;

public class TestParallelMergeSort
{
	// define the size to run, suggest range [1 million, 10 million]
	private final static int arrSize = 10000000;

	// the best number of thread is the number of the core of your processors
	private final static int threadCount = 4;

	// decide whether to print the merge result
	private final static boolean showSingleResults = false;
	private final static boolean showMultiResults = false;
	private final static int runTime = 9;

	public static void main(String[] args)
	{

		ParallelMergeSort pms = new ParallelMergeSort();
		long singleTotal = 0;
		long multiTotal = 0;

		for (int i = 0; i < runTime; i++)
		{
			// single thread result
			singleTotal += pms.SingleThread(arrSize, showSingleResults);

			// multiple thread result
			try
			{
				multiTotal += pms.MultiThread(arrSize, threadCount,
						showMultiResults);
			}
			catch (ThreadNumException e)
			{
				System.out.println("Thread number is illegal!");
			}
		}

		System.out.println("Running type\tTotal\tAverage");
		System.out.println("Single thread\t" + singleTotal + "ms\t"
				+ String.format("%.2f", (double) singleTotal / runTime)
				+ "ms\t");

		System.out.println("Multithread(" + threadCount + ")\t" + multiTotal
				+ "ms\t" + String.format("%.2f", (double) multiTotal / runTime)
				+ "ms\t\n");
		System.out.println(String.format("%.2f", (double) singleTotal
				/ multiTotal)
				+ " times faster!");
	}

}
