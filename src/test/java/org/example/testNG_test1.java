package org.example;//package org.example;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.asserts.SoftAssert;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class testNG_test1 {
    static WebDriver driver;
    static String browser2;
    static List<String> outs;
//    static  List<String> names;
//    static  List<String> prices;
    BookSearchPage bookSearchPage;

    @BeforeSuite
    public void createString(){
        outs=new ArrayList<>();
        outs.add("Test at:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")));
    }


    @Parameters({"browser"})
    @BeforeTest
    public void createDriver(@Optional("Chrome") String browser){
        if(browser.equals("Chrome")){
            driver=new ChromeDriver();
            browser2="Chrome";
        }
        else if(browser.equals("Firefox")){
            driver=new FirefoxDriver();
            browser2="Firefox";
        }
        else if(browser.equals("Edge")){
            driver=new EdgeDriver();
            browser2="Edge";
        }
        else{
            throw new IllegalArgumentException("Unsupported browser");
        }
        bookSearchPage=new BookSearchPage(driver);
        driver.manage().window().maximize();
//        outs=new ArrayList<>();
//        outs.add("");
        outs.add("");
//        outs.add("Test at:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")));
    }

    @Test
    public void test1() throws Exception{

        outs.add("Browser: "+browser2);
        String[][] excelData=ExcelUtils.readExcel("C:\\Users\\2408719\\IdeaProjects\\SeleniumProblems\\src\\test\\java\\org\\example\\MiniProject.xlsx","miniProject");
        outs.add("reading from excel");

        String URL=excelData[1][3];
        String searchKey=excelData[1][0];
        int minResults=(int)Double.parseDouble(excelData[1][1]);
        int noOfResultsToShow=(int)Double.parseDouble(excelData[1][2]);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));

//        driver.get(URL);
        bookSearchPage.goToUrl(URL);
        outs.add("Opening "+ URL);

//        WebElement searchelem= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='inputbar']")));
//        searchelem.sendKeys(searchKey);
//        driver.findElement(By.xpath("//*[@id='btnTopSearch']")).click();
        bookSearchPage.performSearch(searchKey);
        outs.add("Searching for "+searchKey);

        int resultCount = bookSearchPage.getSearchResultsCount();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(resultCount > minResults, "Number of results (" + resultCount + ") is not greater than minimum expected (" + minResults + ").");
        if(resultCount > minResults){
            System.out.println("More than " + minResults + " results found");
            outs.add("More than " + minResults + " results found");
        }
        else{
            System.out.println("Less than " + minResults + " results are found");
            outs.add("Less than " + minResults + " results are found");
        }
//        List<WebElement> resultList=driver.findElements(By.xpath("//*[@class='list-view-books']"));
//        SoftAssert softAssert=new SoftAssert();
//        softAssert.assertTrue(resultList.size()>minResults);
//        if(resultList.size()>minResults){
//            System.out.println("More than 10 results found");
//            outs.add("More than 10 results found");
//        }
//        else{
//            System.out.println("Less than 10 results are found");
//            outs.add("Less than 10 results are found");
//        }


//        Select sortSelect=new Select(driver.findElement(By.id("ddlSort")));
//        sortSelect.selectByVisibleText("Price - Low to High");
//        outs.add("sorting by Price - Low to High");
            bookSearchPage.sortByVisibleText("Price - Low to High");
            outs.add("Sorting by Price - Low to High");

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='list-view-books']")));
//        List<WebElement> newList=driver.findElements(By.xpath("//*[@class='list-view-books']"));
//        outs.add("The top "+noOfResultsToShow+" results are:");
//        for(int i=0;i<Math.min(noOfResultsToShow,newList.size());i++){
//           newList=driver.findElements(By.xpath("//*[@class='list-view-books']"));
//            WebElement resultElement=newList.get(i);
//            String name=resultElement.findElement(By.xpath(".//div/div/a")).getText();
//            String price=resultElement.findElement(By.xpath(".//div[@class='sell']")).getText();
//            System.out.println(name+"\t"+price);
//            outs.add(name+"\t"+price);
//        }
        List<String> topResultsDetails = bookSearchPage.getTopNResultsDetails(noOfResultsToShow);
        outs.add("The top " + noOfResultsToShow + " results are:");

        for(String detail : topResultsDetails){
            String[] details=detail.split("\t");
            System.out.println(detail);
//            names.add(details[0]);
//            prices.add(details[1]);
            outs.add(detail);
        }

//        try{
//            ExcelUtils.writeIntoExcel(outs,"C:\\Users\\2408719\\Desktop\\MiniProject.xlsx","outs");
//            outs.add("Results written to Excel sheet 'outs'.");
//        }
//        catch(Exception e){
//            System.out.println("Unable to write into excel: " + e.getMessage());
//            outs.add("ERROR: Unable to write into excel: " + e.getMessage());
//            e.printStackTrace();
//        }
        softAssert.assertAll();
//        Thread.sleep(2000);
    }
//    @DataProvider(name = "excelData")
    public String[][] readExcel(String filename,String sheetName) throws Exception{
        XSSFWorkbook book=new XSSFWorkbook(new FileInputStream(filename));
        XSSFSheet sheet=book.getSheet(sheetName);
        int rows=sheet.getLastRowNum();
        int cols=sheet.getRow(0).getLastCellNum();
        String[][] data=new String[rows+1][cols];
        for(int i=0;i<=rows;i++){
            XSSFRow row=sheet.getRow(i);
            for(int j=0;j<cols;j++){

                data[i][j]=row.getCell(j).toString();
            }
        }
        return data;
    }

    public void writeIntoExcel(List<String> data, String path,String sheetName) throws IOException {
        try(FileInputStream stream = new FileInputStream(path);
            XSSFWorkbook book = new XSSFWorkbook(stream);){
            XSSFSheet sheet=book.getSheet(sheetName);
            if(sheet==null){
                sheet=book.createSheet(sheetName);
            }
            int lastRow= sheet.getLastRowNum()+3;
            for(int i=0;i<data.size();i++){
                XSSFRow row=sheet.createRow(lastRow+i+1);
                row.createCell(0).setCellValue(data.get(i));
            }
            stream.close();
            try(FileOutputStream ostream = new FileOutputStream(path);){
                book.write(ostream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterTest
    public void closeDriver(){
        driver.quit();
    }

    @AfterSuite
    public void writeEXcel(){
        try{
            ExcelUtils.writeIntoExcel(outs,"C:\\Users\\2408719\\IdeaProjects\\SeleniumProblems\\src\\test\\java\\org\\example\\MiniProject.xlsx","outs");
            outs.add("Results written to Excel sheet 'outs'.");
            System.out.println("Results written to Excel sheet 'outs'.");
        }
        catch(Exception e){
            System.out.println("Unable to write into excel: " + e.getMessage());
            outs.add("ERROR: Unable to write into excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


