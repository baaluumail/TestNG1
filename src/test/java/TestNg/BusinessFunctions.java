package TestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import Utils.constants;
import Utils.lib;
public class BusinessFunctions extends lib {

	public static WebDriver driver;
	
	public void InitializeBrowser()
	{
		System. setProperty("webdriver.chrome.driver", constants.ChromePath);
		// Initialize browser.
		driver=new ChromeDriver();
		driver.manage().window().maximize();
	}
	
	
}
