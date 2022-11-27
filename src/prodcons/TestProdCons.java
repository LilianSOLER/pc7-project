package prodcons;

import java.io.IOException;

import static prodcons.utils.Print.print;

public class TestProdCons {

	static String[] fileNames = {"options-short", "options", "options-long"};
	static boolean print = true;

	static String[] packageNames = {"prodcons.solutionDirect", "prodcons.semaphores"};

	static int[] test(String packageName, String fileName, int nTests, String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		int error = 0;
		int nErrors = 0;

		print("Testing semaphores solution with " + nTests + " tests and " + fileName + ".xml", print);
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
		int coeffTest = 1;
		int nTests = 100;
		for (String fileName : fileNames) {
			int[] errorRate = test(packageName, fileName, nTests / coeffTest, args);
			if (errorRate[0] != 0) {
				hasErrors = true;
			}
			errorRateAvg += errorRate[0];
			errorRatePerTestAvg += errorRate[1];
			coeffTest *= 10;
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
		for (String packageName : packageNames) {
			System.out.println("Testing " + packageName);
			test(packageName, args);
			System.out.println("\n");
		}
		System.out.println("Done");
	}
}

