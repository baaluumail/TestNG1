package Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class lib {

	public static Logger logger=LogManager.getLogger(lib.class.getName());
public static ConcurrentHashMap<String, List<String>> testAndValidationResultsMap=new ConcurrentHashMap<String, List<String>>();	
public static ConcurrentHashMap<String, Integer> failuresCountByTest=new ConcurrentHashMap<String, Integer>();
public static TreeSet<String> classLevelResultsList=new TreeSet<String>();
public static Properties configData=new Properties();
public static Properties PointerData=new Properties();
public static WebDriver driver;

public static ExtentReports htmlreport;
public static ExtentTest test;
public static DateFormat dateFormat=new SimpleDateFormat("MMddyyHHmm");
public static Calendar cal=Calendar.getInstance();
public static Boolean TakeScreenshot=false;

public static void beforeMethod(String methodName)
{
	test=htmlreport.startTest(methodName);
	test.assignAuthor(lib.PointerData.getProperty("ENVIRONMENT"));
	test.assignCategory("Regression");
}
public static void afterMethod(String methodName)
{
	htmlreport.endTest(test);
	htmlreport.flush();
}
	
public static void afterClass(String className)
{
	
}
public void afterTest()
{
	
}
public static void afterSuite(String suiteName) {
	if(!(driver==null))
	{
		driver.quit();
	}
	
}

public static void Report(WebDriver driver, String e, String StepStatus) throws Exception
{
	if(StepStatus.toLowerCase().contains("pass"))
	{
		test.log(LogStatus.PASS, "Test has passed without Exception" , constants.PASS);
		if(lib.PointerData.getProperty("ScreenShot_Pass").equals("Y"))
		{
			lib.TakeScreenshot(driver);
		}
	}
	if(StepStatus.toLowerCase().contains("fail"))
	{
		test.log(LogStatus.FAIL, e, constants.FAIL);
		if(lib.PointerData.getProperty("ScreenShot_Fail").equals("Y"))
		{
			lib.TakeScreenshot(driver);
		}
	}
}
private static void TakeScreenshot(WebDriver driver) throws IOException {
	// TODO Auto-generated method stub
	
	File screen_img=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	
	File screen_file=new File("C:\\Users\\balasubramaniang\\Documents\\Selenium\\screenshot\\"+System.currentTimeMillis()+".png");
	String img_path=screen_file.getPath();
	FileUtils.copyFile(screen_img, screen_file);
	test.log(LogStatus.INFO, "Snapshot below: "+test.addScreenCapture(img_path));
	htmlreport.endTest(test);
	htmlreport.flush();
	
	
	
}
public static void ReadPointerData() throws InvalidPropertiesFormatException, IOException {
	File file=new File((System.getProperty("user.dir").toString().replace("\\", "\\\\")+"\\src\\test\\resources\\"+"Pointer.xml"));
	FileInputStream fileInput=new FileInputStream(file);
	PointerData.loadFromXML(fileInput);
	fileInput.close();
}
public static void ReadConfigData() throws InvalidPropertiesFormatException, IOException {
	File file=new File((System.getProperty("user.dir").toString().replace("\\", "\\\\")+"\\src\\test\\resources\\"+PointerData.getProperty("ENVIRONMENT")+"\\"+PointerData.getProperty("APPLICATION")+".xml"));
	FileInputStream fileInput=new FileInputStream(file);
	configData.loadFromXML(fileInput);
	fileInput.close();
	
	htmlreport=new ExtentReports(System.getProperty("user.dir").toString().replace("\\", "/")+"/test-output/reports/testreport"+dateFormat.format(cal.getTime())+".html");
	
}

public static WebElement GetObject(WebDriver driver, String objName) throws Exception, NoSuchElementException
{
	String identifier;
	String description;
	WebElement element=null;
	
	By searchBy=null;
	if(objName.equals(null)||objName.trim().equals("")){
		throw new Exception("Object Description must be provided");
	}
	else
	{
		identifier=objName.split("~")[0].trim();
		description=objName.split("~")[1].trim();
		if(identifier.equals("xpath")){
			searchBy=By.xpath(description);
		}
		else if(identifier.equals("id")){
			searchBy=By.id(description);
		}
		else if(identifier.equals("name")){
			searchBy=By.name(description);
		}
		else
		{
			throw new Exception("Object Description/Type is invalid");
		}
		
	
	element=driver.findElement(searchBy);
	
	
	return element;
}
}

public static void setText(WebDriver driver, String objName, String Text) throws Exception, NoSuchElementException
{
	GetObject(driver, objName).sendKeys(Text);
}
public static String  getStackTrace(Exception e) {
	// TODO Auto-generated method stub
	StringWriter sw=new StringWriter();
	PrintWriter pw=new PrintWriter(sw);
	e.printStackTrace(pw);
	String errormsg=e.toString();
	return errormsg;
}
public static void captureScreenwithErrorMsg(Exception e) {
	// TODO Auto-generated method stub
	StringWriter sw=new StringWriter();
	PrintWriter pw=new PrintWriter(sw);
	e.printStackTrace(pw);
	String errormsg=e.toString();
	CaptureScreenshot(errormsg); 
}
private static void CaptureScreenshot(String errormsg) {
	// TODO Auto-generated method stub
	Date date=new Date();
	
	SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd-yyyy");
	StackTraceElement[] stackTraceElement=Thread.currentThread().getStackTrace();
	String testClassMethodName=null;
	for(int i=0; i<=stackTraceElement.length-1; i++)
	{
		if(stackTraceElement[i].getClassName().contains("TestNg"))
		{
			testClassMethodName=stackTraceElement[i].getMethodName();
			break;
		}
	}
	try{
		Thread.sleep(3000);
		BufferedImage image=new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		
		File f=new File("./test-output/screenshots/"+dateFormat.format(date));
		
		if(f.exists())
		{
			
		}
		else
		{
			f.mkdir();
			Thread.sleep(4000);
		}
		
		Date date1=new Date();
		SimpleDateFormat dateFormat1=new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		String filepath=dateFormat1.format(date1).replaceAll(":", "_");
		ImageIO.write(image, "jpg", new File(f+"/"+testClassMethodName+"_"+filepath+".jpg"));
		
		File file=new File(f+"/"+testClassMethodName+"_"+filepath+".jpg");
		if(file.exists())
		{
			String[] ErrorMsg=errormsg.split("\n");
			final BufferedImage image1=ImageIO.read(file);
			
			Graphics g=image1.getGraphics();
			
			g.setColor(Color.lightGray);
			g.fillRect(0,60, image1.getWidth(),50);
			
			
			g.setFont(new Font("Arial", Font.BOLD, 15));
			
			g.setColor(Color.BLACK);
			
			g.drawString("Error", 0, 80);
			
			g.drawString(ErrorMsg[0], 0, 90);
			
			g.dispose();
			
			ImageIO.write(image1, "jpg", file);
			
			
			
			
			
		}
	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
	
}

}
