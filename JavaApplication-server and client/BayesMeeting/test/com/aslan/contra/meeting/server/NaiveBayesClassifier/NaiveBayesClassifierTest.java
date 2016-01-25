package com.aslan.contra.meeting.server.NaiveBayesClassifier;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class NaiveBayesClassifierTest {

	@Test
	public void testMean() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		double[] tempArray = { 1, 2, 3, 4, 5 };
		assertEquals("mean of {1,2,3,4,5} must be 3", 3.0, nbc.mean(tempArray),
				0.0001);
	}

	@Test
	public void testStdev() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		double[] tempArray = { 1, 2, 3, 4, 5 };
		assertEquals("standard deviation of {1,2,3,4,5} must be 1.58114",
				1.58114, nbc.stdev(tempArray), 0.0001);
	}

	@Test
	public void testSummarize() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		double[] tempArray1 = { 1, 20, 0 };
		double[] tempArray2 = { 2, 21, 1 };
		double[] tempArray3 = { 3, 22, 0 };

		ArrayList<double[]> tempArrayList = new ArrayList<double[]>();
		tempArrayList.add(tempArray1);
		tempArrayList.add(tempArray2);
		tempArrayList.add(tempArray3);

		ArrayList<double[]> testSummary = new ArrayList<double[]>();
		testSummary = nbc.summarize(tempArrayList);

		assertEquals("size of testSummary must be:  2", 2, testSummary.size());

		assertEquals("mean in summary for class{0} must be:  2.0", 2.0,
				testSummary.get(0)[0], 0.0001);
		assertEquals(
				"standard deviation in summary for class{0} must be:  1.0",
				1.0, testSummary.get(0)[1], 0.0001);

		assertEquals("mean in summary for class{1} must be:  21.0", 21.0,
				testSummary.get(1)[0], 0.0001);
		assertEquals(
				"standard deviation in summary for class{1} must be:  1.0",
				1.0, testSummary.get(1)[1], 0.0001);

	}

	@Test
	public void testSummarizeByClass() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		double[] tempArray1 = { 1, 20, 1, 1 };
		double[] tempArray2 = { 2, 21, 2, 0 };
		double[] tempArray3 = { 3, 22, 3, 1 };
		double[] tempArray4 = { 4, 22, 5, 0 };

		ArrayList<double[]> tempArrayList = new ArrayList<double[]>();
		tempArrayList.add(tempArray1);
		tempArrayList.add(tempArray2);
		tempArrayList.add(tempArray3);
		tempArrayList.add(tempArray4);

		Map<Double, ArrayList<double[]>> testSummaries = nbc
				.summarizeByClass(tempArrayList);

		ArrayList<double[]> testClass0 = testSummaries.get(0.0);
		ArrayList<double[]> testClass1 = testSummaries.get(1.0);

		System.out.println("mean value of class0: " + testClass0.get(0)[0]
				+ " stdev is: " + testClass0.get(0)[1]);
		System.out.println("mean value of class0: " + testClass0.get(1)[0]
				+ " stdev is: " + testClass0.get(1)[1]);
		System.out.println("mean value of class0: " + testClass0.get(2)[0]
				+ " stdev is: " + testClass0.get(2)[1]);
		System.out.println("*****");
		System.out.println("mean value of class1: " + testClass1.get(0)[0]
				+ " stdev is: " + testClass1.get(0)[1]);
		System.out.println("mean value of class1: " + testClass1.get(1)[0]
				+ " stdev is: " + testClass1.get(1)[1]);
		System.out.println("mean value of class1: " + testClass1.get(2)[0]
				+ " stdev is: " + testClass1.get(2)[1]);

		assertEquals("size of testClass0 must be:  3", 3, testClass0.size());
		assertEquals("size of testClass0 must be:  3", 3, testClass1.size());

		// for class{0}
		assertEquals("mean of class{0} coloumn{1} must be:  3.0", 3.0,
				testClass0.get(0)[0], 0.0001);
		assertEquals(
				"standard deviation of class{0} coloumn{1} must be:  1.414213",
				1.414213, testClass0.get(0)[1], 0.0001);

		assertEquals("mean of class{0} coloumn{2} must be:  21.5", 21.5,
				testClass0.get(1)[0], 0.0001);
		assertEquals(
				"standard deviation of class{0} coloumn{2} must be:  0.707106",
				0.707106, testClass0.get(1)[1], 0.0001);

		assertEquals("mean of class{0} coloumn{3} must be:  3.5", 3.5,
				testClass0.get(2)[0], 0.0001);
		assertEquals(
				"standard deviation of class{0} coloumn{3} must be:  2.121320",
				2.121320, testClass0.get(2)[1], 0.0001);

		// for class{1}
		assertEquals("mean of class{1} coloumn{1} must be:  2.0", 2.0,
				testClass1.get(0)[0], 0.0001);
		assertEquals(
				"standard deviation of class{1} coloumn{1} must be:  1.414213",
				1.414213, testClass1.get(0)[1], 0.0001);

		assertEquals("mean of class{1} coloumn{2} must be:  21.0", 21.0,
				testClass1.get(1)[0], 0.0001);
		assertEquals(
				"standard deviation of class{1} coloumn{2} must be:  1.414213",
				1.414213, testClass1.get(1)[1], 0.0001);

		assertEquals("mean of class{1} coloumn{3} must be:  2.0", 2.0,
				testClass1.get(2)[0], 0.0001);
		assertEquals(
				"standard deviation of class{1} coloumn{3} must be:  1.414213",
				1.414213, testClass1.get(2)[1], 0.0001);
	}

	@Test
	public void testCalculateProbability() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		double x = 71.5;
		double mean = 73;
		double stdev = 6.2;

		assertEquals("probability must be 0.0624", 0.0624,
				nbc.calculateProbability(x, mean, stdev), 0.0001);
	}

	@Test
	public void testCalculateClassProbabilities() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		Map<Double, ArrayList<double[]>> summaries = new HashMap<Double, ArrayList<double[]>>();
		double[] inputVector = { 1.1 };

		double[] testData0 = { 1.0, 0.5 };
		double[] testData1 = { 20, 5.0 };

		ArrayList<double[]> testArrayList_class0 = new ArrayList<double[]>();
		testArrayList_class0.add(testData0);

		ArrayList<double[]> testArrayList_class1 = new ArrayList<double[]>();
		testArrayList_class1.add(testData1);

		summaries.put(0.0, testArrayList_class0);
		summaries.put(1.0, testArrayList_class1);

		double[] probabilities = nbc.calculateClassProbabilities(summaries,
				inputVector);

		assertEquals(
				"length of probabilities array must be 2 (number of classes)",
				2, probabilities.length);

		assertEquals("class{0} probability must be 0.782", 0.782,
				probabilities[0], 0.0001);

		assertEquals("class{1} probability must be 0.0000629", 0.0000629,
				probabilities[1], 0.0000001);

	}

	@Test
	public void testPredict() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		Map<Double, ArrayList<double[]>> summaries = new HashMap<Double, ArrayList<double[]>>();
		double[] inputVector = { 1.1 };

		double[] testData0 = { 1.0, 0.5 };
		double[] testData1 = { 20, 5.0 };

		ArrayList<double[]> testArrayList_class0 = new ArrayList<double[]>();
		testArrayList_class0.add(testData0);

		ArrayList<double[]> testArrayList_class1 = new ArrayList<double[]>();
		testArrayList_class1.add(testData1);

		summaries.put(0.0, testArrayList_class0);
		summaries.put(1.0, testArrayList_class1);

		assertEquals("according to probability, class must be:  0", "0",
				nbc.predict(summaries, inputVector));
	}

	@Test
	public void testGetPredictions() {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		Map<Double, ArrayList<double[]>> summaries = new HashMap<Double, ArrayList<double[]>>();

		double[] testData0 = { 1.0, 0.5 };
		double[] testData1 = { 20, 5.0 };

		ArrayList<double[]> testArrayList_class0 = new ArrayList<double[]>();
		testArrayList_class0.add(testData0);

		ArrayList<double[]> testArrayList_class1 = new ArrayList<double[]>();
		testArrayList_class1.add(testData1);

		summaries.put(0.0, testArrayList_class0);
		summaries.put(1.0, testArrayList_class1);

		ArrayList<double[]> testSet = new ArrayList<double[]>();
		double[] testSetInstance1 = { 1.1 };
		double[] testSetInstance2 = { 19.1 };

		testSet.add(testSetInstance1);
		testSet.add(testSetInstance2);

		ArrayList<String> predictions = nbc.getPredictions(summaries, testSet);

		assertEquals("number of predictions must be:  2", 2, predictions.size());

		assertEquals("prediction for instance 1.1 must be:  0", "0",
				predictions.get(0));

		assertEquals("prediction for instance 19.1 must be:  1", "1",
				predictions.get(1));

	}

}
