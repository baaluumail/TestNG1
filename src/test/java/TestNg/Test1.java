package TestNg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import Utils.constants;
import Utils.lib;

public class Test1 extends BusinessFunctions {

	public static String TestCase,Query;
	@Test
	public void GoogleTest1() throws Exception {
		try{
			String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			TestDataRead(methodName);
			InitializeBrowser();
			driver.get("https://www.google.com");
			lib.setText(driver, orep.searchbox, Query );
			lib.GetObject(driver, orep.clickSearch).click();


		}
		catch(Exception e)
		{
			lib.captureScreenwithErrorMsg(e);

			lib.Report(driver, lib.getStackTrace(e), constants.FAIL);
		}
	}

	@Test
	public void GoogleTest2() throws Exception {
		try{
			String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			TestDataRead(methodName);
			InitializeBrowser();
			driver.get("https://www.google.com");
			lib.setText(driver, orep.searchbox, Query );
			lib.GetObject(driver, orep.clickSearch).click();


		}
		catch(Exception e)
		{

			lib.captureScreenwithErrorMsg(e);

			lib.Report(driver, lib.getStackTrace(e), constants.FAIL);
		}
	}


	public void TestDataRead(String TestCase) throws FilloException
	{

		Fillo fillo=new Fillo();
		Connection connection=fillo.getConnection(System.getProperty("user.dir").toString().replace("\\", "\\\\")+"\\src\\test\\resources\\TESTDATA\\"+lib.PointerData.getProperty("APPLICATION")+".xlsx");
		String strQuery="Select * from Test1 where TestCase='"+TestCase+"'";
		Recordset recordset=connection.executeQuery(strQuery);

		while(recordset.next()){

			Query=recordset.getField("Query");

		}

		recordset.close();
		connection.close();
	}


	@BeforeMethod
	public void beforeMethod(Method method) throws Exception
	{
		/*lib.failuresCountByTest.put((this.getClass().getName()+"."+method.getName()), 0);
		List<String> arrList=new ArrayList<String>();
		lib.testAndValidationResultsMap.put((this.getClass().getName()+"."+method.getName()), arrList);*/
		String methodName=method.getName();
		lib.beforeMethod(methodName);

	}
	@AfterMethod
	public void afterMethod(Method method) throws Exception
	{
		driver.quit();
		String methodName=this.getClass().getName()+"."+method.getName();
		lib.afterMethod(methodName);
	}
	@BeforeClass
	public void beforeClass() throws Exception
	{
		//lib.classLevelResultsList.clear();
	}

	@AfterClass(alwaysRun=true)
	public void afterClass() throws Exception
	{
		String className=this.getClass().getName();
		lib.afterClass(className);
	}
	@BeforeTest
	public void beforeTest() 
	{

	}
	@AfterTest
	public void afterTest()
	{

	}

	@BeforeSuite
	public void beforeSuite() throws Exception
	{
		lib.ReadPointerData();
		lib.ReadConfigData();
	}

	@AfterSuite
	public void afterSuite(ITestContext ctx) throws Exception
	{
		String suiteName=ctx.getSuite().getName();
		lib.afterSuite(suiteName);

	}
}
