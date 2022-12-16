package prodcons;

import java.io.IOException;

import static prodcons.utils.Print.print;

public class TestProdCons {

	static String[] fileNames = {"options-short", "options", "options-long"};
	static boolean print = true;

	static int NTESTS = 3;

	static String[] packageNames = {"prodcons.v1", "prodcons.v2", "prodcons.v3", "prodcons.v5"};

	static int[] test(String packageName, String fileName, int nTests, String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		int error = 0;
		int nErrors = 0;

		print("Testing " + packageName + " with " + nTests + " tests and " + fileName + ".xml", print);
		for (int i = 0; i < nTests; i++) {
			error += Main.main(args, fileName, false, packageName);
			if (error != 0) {
				nErrors++;
			}
			print("Test " + i + " finished with " + error + " errors", print);
		}

		if (nErrors == 0) {
			print("All tests passed", print);
			return new int[]{0, 0};
		} else {
			int errorRate = (nErrors * 100) / nTests;
			print("Error rate: " + errorRate + "%", print);
			int errorRatePerTest = error / nErrors;
			print("Error rate per test: " + errorRatePerTest, print);
			return new int[]{errorRate, errorRatePerTest};
		}
	}

	public static void test(String packageName, String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		int errorRateAvg = 0;
		int errorRatePerTestAvg = 0;
		boolean hasErrors = false;
		int coeffTest = 2;
		int nTests = NTESTS;
		if (NTESTS < fileNames.length) {
			nTests = fileNames.length;
		}
		nTests = (int) Math.pow(2, nTests);
		for (String fileName : fileNames) {
			int[] errorRate = test(packageName, fileName, nTests / coeffTest, args);
			if (errorRate[0] != 0) {
				hasErrors = true;
			}
			errorRateAvg += errorRate[0];
			errorRatePerTestAvg += errorRate[1];
			coeffTest *= 2;
			if (coeffTest > nTests) {
				coeffTest = nTests;
			}
		}

		if (hasErrors) {
			System.out.println("\n");

			System.out.println("Error rates:");
			errorRateAvg /= fileNames.length;
			System.out.println("Average error rate: " + errorRateAvg + "%");

			System.out.println("\n");

			System.out.println("Error rates per test:");
			errorRatePerTestAvg /= fileNames.length;
			System.out.println("Average error rate per test: " + errorRatePerTestAvg);
		} else {
			System.out.println("All tests passed");
		}


	}

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		test(args);
	}

	private static void displayTime(long startTimePackage, long endTimePackage) {
		int time = (int) (endTimePackage - startTimePackage);
		if (time > 60000) {
			System.out.println("Time: " + time / 60000 + " min");
		} else if (time > 1000) {
			System.out.println("Time: " + time / 1000 + " sec");
		} else {
			System.out.println("Time: " + time + " ms");
		}
	}

	private static void test(String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// begin a timer
		long startTime = System.currentTimeMillis();
		for (String packageName : packageNames) {
			System.out.println("Testing " + packageName);
			long startTimePackage = System.currentTimeMillis();
			test(packageName, args);
			long endTimePackage = System.currentTimeMillis();
			displayTime(startTimePackage, endTimePackage);
			System.out.println("\n");
		}
		// end a timer
		long endTime = System.currentTimeMillis();
		// print the time in the correct unit (min or sec or ms) and the unit itself
		displayTime(startTime, endTime);
		System.out.println("Done");
		System.exit(0);
	}
}

