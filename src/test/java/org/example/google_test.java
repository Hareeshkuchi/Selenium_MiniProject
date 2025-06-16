package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class google_test {
    static WebDriver driver;
    @BeforeClass
    public void createDriver(){
         driver=new ChromeDriver();
    }
    @Test
    public void test1() throws  Exception{
        driver.get("https://www.google.com/");
        Thread.sleep(2000);
        WebElement searchArea=driver.findElement(By.id("APjFqb"));
        String search="Amazon";
        for(char ch:search.toCharArray()){
            searchArea.sendKeys(ch+"");
            Thread.sleep(1000);
        }
//        driver.findElement(By.xpath("//*[@id=\"tsf\"]/div[1]/div[1]/div[2]/button/div/span/svg")).click();
        Actions act=new Actions(driver);
        act.keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
    }
}
