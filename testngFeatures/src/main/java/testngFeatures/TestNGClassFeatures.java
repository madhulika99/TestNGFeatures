package testngFeatures;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestNGClassFeatures {
	WebDriver driver;
	public ExtentHtmlReporter htmlreporter;
	public ExtentTest logger;
	public ExtentReports extent;

	@BeforeClass
	public void setUP() {

		String Datenam = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String currentDir = System.getProperty("user.dir");
		htmlreporter = new ExtentHtmlReporter(currentDir + "\\test-output\\" + Datenam + ".html");
		htmlreporter.config().setDocumentTitle("Autoamtion report");
		htmlreporter.config().setReportName("functional testing");
		htmlreporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(htmlreporter);
		extent.setSystemInfo("Hostname", "LocalHost");
		extent.setSystemInfo("tester", "Madhulika");
		System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
		System.setProperty("webdriver.chrome.driver", "C:\\SELENIUM TUTORIAL\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// driver.get("https://www.amazon.in/");

	}

	@Test(priority = 1, description = "title of Page test" , invocationCount = 2, groups = "firstTest")
	// invocationCount attribute is used in testNG (test next generation) to trigger
	// same method 2 times)
	public void titleTest() {
		logger = extent.createTest("Title Test");
		driver.get("https://www.amazon.in/");
		String Title = driver.getTitle();
		System.out.println("title of the page is " + Title);
		Assert.assertEquals(Title,
				"Online Shopping site in India: Shop Online for Mobiles12, Books, Watches, Shoes and More - Amazon.in",
				"title not correct");

	}

	@Test(priority = 2, description = "check characters", groups = "firstTest")
	public void logoTest() {
		logger = extent.createTest("dispaly Test");
		boolean dis = driver.findElement(By.xpath("//span[@class='icp-nav-flag icp-nav-flag-in']")).isDisplayed();
		Assert.assertEquals(dis, true);
	}

	@Test(priority = 3, description = "check characters", groups = "firstTest")
	public void languageTest() {
		logger = extent.createTest("navigation Test");
		WebElement nav = driver.findElement(By.xpath("//span[@class='icp-nav-link-inner']"));
		nav.click();
		boolean f = false;
		Assert.assertTrue(f, "assert not true");

	}

	@Test(priority = 4, description = "depends on methods check", groups = "firstTest", dependsOnMethods = "languageTest")
	public void dependTest() {
		logger = extent.createTest("depends test");
		System.out.println("hey beautiful");
	}

	public String screenshotpath(WebDriver driver, String Screenshotname) throws IOException {
		String date = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
		File Source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "\\screenshot\\" + Screenshotname + date + ".png";
		File Destin = new File(destination);
		FileUtils.copyFile(Source, Destin);
		return destination;
	}

	@AfterMethod //@AfterTest annotation was not able to produce report saying only parametere can be injected
	public void tearDown(ITestResult result) throws IOException {
		String screenshotpath1 = screenshotpath(driver, result.getName());
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL, "Test case failed is " + result.getName()); // to log method name which failed
			logger.log(Status.FAIL, "Test Case failed is " + result.getThrowable()); // to log all exceptions and errors
			logger.addScreenCaptureFromPath(screenshotpath1); // add screenshot in the extentreport

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP, "TestCase skipped is " + result.getName());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS, "TestCase passed is " + result.getName());
		}
		extent.flush();

	}

	/*@AfterTest
	public void extentClose() {
		extent.flush();
	}*/

}
