package org.example;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class testNG_test1 {
    static WebDriver driver;
    static Properties props;


//    @BeforeSuite
    public void loadProps(){
        try {
            InputStream stream = new FileInputStream("/org/example/config.properties");
            props.load(stream);
            stream.close();
        } catch (Exception e) {
            System.err.println("Failed to load config.properties: " + e.getMessage());
            e.printStackTrace();
            // You might want to throw an exception here to stop tests if config fails to load
            Assert.fail("Failed to load configuration properties.");
        }
    }
    @Parameters({"browser"})
    @BeforeTest
    public void createDriver(@Optional("Chrome") String browser){
        if(browser.equals("Chrome")){
            driver=new ChromeDriver();
        }
        else if(browser.equals("Firefox")){
            driver=new FirefoxDriver();
        }
        else if(browser.equals("Edge")){
            driver=new EdgeDriver();
        }
        else{
            throw new IllegalArgumentException("Unsupported browser");
//            return;
        }
    }


    @Test
    public void test1() throws Exception{



        String[][] excelData=readExcel("C:\\Users\\2408719\\Downloads\\MiniProject.xlsx","miniProject");
//        String[][] excelData=readExcel(props.getProperty("excel.path"), props.getProperty("excel.sheet"));
        String URL=excelData[1][4];
        String searchKey=excelData[1][0];
        int minResults=(int)Double.parseDouble(excelData[1][1]);
        int noOfResultsToShow=(int)Double.parseDouble(excelData[1][2]);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(URL);
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//*[@id='inputbar']")).sendKeys(searchKey);
        driver.findElement(By.xpath("//*[@id='btnTopSearch']")).click();
//        driver.findElement(By.xpath(props.getProperty("app.searchbar"))).sendKeys(searchKey);
//        driver.findElement(By.xpath(props.getProperty("app.btnTopSearch"))).click();
//        List<WebElement> resultList=driver.findElements(By.xpath(props.getProperty("app.results")));
        List<WebElement> resultList=driver.findElements(By.xpath("//*[@class='list-view-books']"));
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertTrue(resultList.size()>minResults);
        if(resultList.size()>minResults){
            System.out.println("More than 10 results found");
        }
        else{
            System.out.println("Less than 10 results are found");
        }

        Select sortSelect=new Select(driver.findElement(By.id("ddlSort")));
        sortSelect.selectByVisibleText("Discount - Low to High");
        Thread.sleep(2000);
//        List<WebElement> newList=driver.findElements(By.xpath(props.getProperty("app.results")));
        List<WebElement> newList=driver.findElements(By.xpath("//*[@class='list-view-books']"));
        for(int i=0;i<Math.min(noOfResultsToShow,newList.size());i++){
            WebElement resultElement=newList.get(i);
//            String name=resultElement.findElement(By.xpath(props.getProperty("results.links"))).getText();
//            String price=resultElement.findElement(By.xpath(props.getProperty("results.price"))).getText();
            String name=resultElement.findElement(By.xpath(".//div/div/a")).getText();
            String price=resultElement.findElement(By.xpath(".//div[@class='sell']")).getText();
            System.out.println(name+"\t"+price);
//            System.out.println(price);
        }

        softAssert.assertAll();
//        Thread.sleep(2000);

    }
//    @DataProvider(name = "excelData")
    public String[][] readExcel(String filename,String sheetName) throws Exception{
        XSSFWorkbook book=new XSSFWorkbook(new FileInputStream(new File(filename)));
        XSSFSheet sheet=book.getSheet(sheetName);
        int rows=sheet.getLastRowNum();
        int cols=sheet.getRow(0).getLastCellNum();
        String[][] data=new String[rows+1][cols];
        for(int i=0;i<=rows;i++){
            XSSFRow row=sheet.getRow(i);
            for(int j=0;j<cols;j++){
                XSSFCell cell=row.getCell(j);

                data[i][j]=row.getCell(j).toString();
            }
        }
        return data;
    }
    @AfterTest
    public void closeDriver(){
        driver.quit();
    }

}
