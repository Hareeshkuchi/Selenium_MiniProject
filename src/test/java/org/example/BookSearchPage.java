package org.example;// Adjust package to fit your project

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory; // Import PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// This class represents the Book Search Page and its elements/actions
public class BookSearchPage {

    protected WebDriver driver; // WebDriver instance for this page object
    protected WebDriverWait wait; // WebDriverWait instance for explicit waits

    // Constructor to initialize WebDriver, WebDriverWait, and elements via PageFactory
    public BookSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Initialize WebDriverWait
        PageFactory.initElements(driver, this); // Initialize WebElements
    }

    // Locators for elements on the Book Search Page
    @FindBy(xpath = "//*[@id='inputbar']")
    private WebElement searchInput;

    @FindBy(xpath = "//*[@id='btnTopSearch']")
    private WebElement searchButton;

    @FindBy(xpath = "//*[@class='list-view-books']")
    private List<WebElement> searchResultsList; // List of all book result elements

    @FindBy(id = "ddlSort")
    private WebElement sortDropdown;



    // Common method to open a URL (moved from BasePage)
    public void openUrl(String url) {
        driver.get(url);
    }

    // Common method to click a WebElement, waiting for it to be clickable (moved from BasePage)
    public void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    // Common method to send keys to a WebElement, waiting for it to be visible (moved from BasePage)
    public void sendKeys(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    // Common method to get text from a WebElement, waiting for it to be visible (moved from BasePage)
    public String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    // Common method to get page title (moved from BasePage)
    public String getPageTitle() {
        return driver.getTitle();
    }

    // Method to navigate to the base URL of the application
    public void goToUrl(String url) {
        openUrl(url); // Use the moved common method
    }

    // Method to perform a search
    public void performSearch(String searchKey) {
        sendKeys(searchInput, searchKey);
        click(searchButton);
    }

    // Method to get the number of search results displayed
    public int getSearchResultsCount() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResultsList));
        return searchResultsList.size();
    }

    // Method to sort results by visible text
    public void sortByVisibleText(String optionText) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(optionText);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='list-view-books']")));
    }

    /**
     * Extracts the name and price from a single book result WebElement.
     * This is a helper method to be used internally by other methods in this page object.
     * @param resultElement The WebElement representing a single book listing.
     * @return A String containing the book name and price, e.g., "Book Title   $10.00".
     */
    private String getBookDetailsFromElement(WebElement resultElement) {
        String name = resultElement.findElement(By.xpath(".//div/div/a")).getText();
        String price = resultElement.findElement(By.xpath(".//div[@class='sell']")).getText();
        return name + "\t" + price;
    }

    // Method to get details of a specified number of top results
    public List<String> getTopNResultsDetails(int numberOfResults) {
        List<String> details = new ArrayList<>();
        // Re-find elements to ensure they are fresh, especially after sorting
        List<WebElement> currentResults = driver.findElements(By.xpath("//*[@class='list-view-books']"));

        for (int i = 0; i < Math.min(numberOfResults, currentResults.size()); i++) {
            try {
                details.add(getBookDetailsFromElement(currentResults.get(i)));
            }
            catch (Exception e){
                currentResults = driver.findElements(By.xpath("//*[@class='list-view-books']"));
                details.add(getBookDetailsFromElement(currentResults.get(i)));
            }
        }
        return details;
    }
}
