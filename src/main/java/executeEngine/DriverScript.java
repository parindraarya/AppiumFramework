package executeEngine;

import java.awt.FlowLayout;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import config.ActionKeywords;
import utilities.CustomReport;
import utilities.ExcelUtils;
import utilities.TestCaseReport;

public class DriverScript {
	
	public static ActionKeywords actionKeywords;
	public static Method method[];
	public static String sActionKeywords;
	public static String locatorKey;
	public static String dataKey;
	public static ArrayList<String> locatorValue;
	public static String dataValue;
	public static By path;
	public static String tcName;
	public static CustomReport cr;
	public static TestCaseReport tcr;
	public static TestBase tb;
	static int max;
	static int current;
	static JFrame frame;
	static JProgressBar pb;
	static int tcCount = 1;
	String startTime = "";
	String endTime = "";
	String refer = "";
	static String status = "";
	String filepath;
	Object[][] module;
	LinkedHashMap<String, String> mod;
	Object[][] data;
	
	
	@BeforeTest
	public void beforeTest() throws IOException {

		Path path = Paths.get("./Reports");

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Path crPath = Paths.get("./Reports/Custom");

		if (!Files.exists(crPath)) {
			try {
				Files.createDirectories(crPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		tcr = new TestCaseReport();
		tcr.crBegin();

		pb = new JProgressBar();
		frame = new JFrame("Test Cases:");

		int maxDatasets = 5;
		max = max * maxDatasets;
		pb.setMinimum(0);

		pb.setMaximum(maxDatasets);
		pb.setStringPainted(true);

		frame.setLayout(new FlowLayout());
		frame.getContentPane().add(pb);

		frame.setAlwaysOnTop(true);
		frame.setLocation(700, 375);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(250, 80);
		frame.setVisible(true);

		current = 0;
		
}
	
	@AfterTest()
	public void afterTest() throws IOException {
		tcr.crEnd();
	}
	
	@Test
	public void moduleData() throws Exception {
		module = ExcelUtils.takeData(Constants.ModuleFILEPATH, "Sheet1");
		mod = CommonFunction.objToMap(module);

		String file, enable;

		for (Map.Entry<String, String> entry : mod.entrySet()) {
			file = entry.getKey();
			enable = entry.getValue();

			if (enable.equalsIgnoreCase("YY")) {
				filepath = "src//main/java//dataEngine//" + file + ".xlsx";
				System.out.println(filepath);
				userFormData(file, filepath);
			}
		}
	}
	
	public void userFormData(String file, String filepath) throws Exception {
		data = ExcelUtils.takeData(filepath, "Sheet1");
		mod = CommonFunction.objToMap(data);

		String sh, en;
		for (Map.Entry<String, String> entry : mod.entrySet()) {
			sh = entry.getKey();
			en = entry.getValue();

			if (en.equalsIgnoreCase("YY")) {
				testData(filepath, sh, en);
			}
		}

	}
	
	public void testData(String filepath, String Description, String Enable) throws Exception {
		if (Enable.equalsIgnoreCase("YY")) {
			System.out.println(Description);
			tcName = Description;

			actionKeywords = new ActionKeywords();
			method = actionKeywords.getClass().getDeclaredMethods();
			
			BasicConfigurator.configure();

			PropertyConfigurator.configure("Log.properties");

			Object testCase[][] = ExcelUtils.takeData(filepath, Description);
			Object locator[][] = ExcelUtils.takeData(Constants.LocatorFILEPATH, "Sheet1");
			Object data1[][] = ExcelUtils.takeData(Constants.DataFILEPATH, "Sheet1");

			Map<String, ArrayList<String>> locators = new HashMap<>();
			Map<String, String> data = new HashMap<>();

			for (int i = 0; i < locator.length; i++) {
				ArrayList<String> temp = new ArrayList<>();
				temp.add(locator[i][1].toString());
				temp.add(locator[i][2].toString());
				locators.put(locator[i][0].toString(), temp);
			}

			for (int i = 0; i < data1.length; i++) {
				data.put(data1[i][0].toString(), data1[i][1].toString());
			}

			startTime = actionKeywords.timeStamp();
			cr = new CustomReport();
			cr.reportBegin();

			for (int i = 0; i < testCase.length; i++) {
				sActionKeywords = testCase[i][1].toString();

				pb.setValue(++current);

				locatorKey = testCase[i][2].toString();

				dataKey = testCase[i][3].toString();

				if (!(locatorKey.equals("NA"))) {
					locatorValue = locators.get(locatorKey);

					path = MapLocator.locate(locatorValue);

				}

				if (!(dataKey.equals("NA"))) {
					dataValue = data.get(dataKey).toString();

				}
				Test_Execute();

			}
			assert endTime != null : " Invalid end time";
			endTime = actionKeywords.timeStamp();
			refer = "./Custom/TestCase" + tcCount + ".html";
			tcCount++;
			tcr.crMain(tcName, startTime, endTime, refer, status);
			cr.reportEnd();
		}
	}
	
	public static void Test_Execute() throws Exception {
		String report[] = { "", "" };
		try {

			if (locatorKey.equals("NA") && dataKey.equals("NA")) {
				Method m = ActionKeywords.class.getMethod(sActionKeywords);
				report = (String[]) m.invoke(actionKeywords);
				// System.out.println(reportData[0]+" "+reportData[1]); reportData=(String[])
			} else if (locatorKey.equals("NA") && !(dataKey.equals("NA"))) {
				Method m = ActionKeywords.class.getMethod(sActionKeywords, String.class);
				report = (String[]) m.invoke(actionKeywords, dataValue);

			} else if (!(locatorKey.equals("NA")) && dataKey.equals("NA")) {
				Method m = ActionKeywords.class.getMethod(sActionKeywords, By.class);
				report = (String[]) m.invoke(actionKeywords, path);

			} else {
				Method m = ActionKeywords.class.getMethod(sActionKeywords, By.class, String.class);
				report = (String[]) m.invoke(actionKeywords, path, dataValue);

			}
			status = report[0];
			cr.reportMain(tcName, sActionKeywords, report[0], report[1]);

		} catch (Exception e) {
			System.out.println("Method not found "+e.getCause().getClass());
		}

	}

	
}
