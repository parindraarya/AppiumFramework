package executeEngine;

import java.util.ArrayList;

import org.openqa.selenium.By;

public class MapLocator {
	
	static String locator;
	static String value;
	public static By DriverPath;
	public static ArrayList<By> al = new ArrayList<By>();

	/**
	 * 
	 * @param lo
	 * @return
	 */
	public static By locate(ArrayList<String> lo) {

		locator = lo.get(0);
		value = lo.get(1);

		if (locator.equals("id")) {
			DriverPath = By.id(value);
		} else if (locator.equals("xpath")) {
			DriverPath = By.xpath(value);
		} else if (locator.equals("className")) {
			DriverPath = By.className(value);
		} else if (locator.equals("cssSelector")) {
			DriverPath = By.cssSelector(value);
		} else if (locator.equals("linkText")) {
			DriverPath = By.linkText(value);
		} else if (locator.equals("name")) {
			DriverPath = By.name(value);
		} else if (locator.equals("tagName")) {
			DriverPath = By.tagName(value);
		} else if (locator.equals("partialLinkText")) {
			DriverPath = By.partialLinkText(value);
		}

		return DriverPath;

	}

}
