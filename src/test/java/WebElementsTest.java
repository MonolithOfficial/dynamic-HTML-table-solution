import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import javax.xml.xpath.*;
import java.util.List;

public class WebElementsTest {
    @Test
    public void executeTest() throws InterruptedException, XPathExpressionException {
        // Driver path setup.
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.get("http://the-internet.herokuapp.com/add_remove_elements/");

        // Locating the 'Add element' button and making sure it actually can add an element
        WebElement addBtn = driver.findElement(By.xpath("//div[@Class='example']//child::button[text() = 'Add Element' and @onclick = 'addElement()']"));

        // Clicking the button three times.
        int i;
        for (i = 0; i < 3; i++){
            addBtn.click();
        }

        // Using last-child
        System.out.println(
                driver.findElement(By.cssSelector("div#elements button:last-child")).getText());

        // Using findElements() method and storing WebElements into a list
        // The last item in the list has the index of (the_lists_length - 1).
        List<WebElement> elementList = driver.findElements(By.cssSelector("div#elements button[class^='added']"));
        System.out.println(elementList.get(elementList.size() - 1).getText());

        // Apparently, ends-with() and matches(@att, regex) methods do not work. So what I did is write this humongous
        // xpath that checks if there are any characters BEFORE 'manually', there may or may not be some. And if there are
        // any characters AFTER 'manually', there should be none.
        // Also, using i variable again, to get the last button.


        // Here are two variants of Xpath to be used. One where it's not specified that where exactly the element is (* is used),
        // and the other (in-action) where it is specified.
        // //*[%s][string-length(substring-before(@class, 'manually')) >= 0 and string-length(substring-after(@class, 'manually')) = 0 and text()="Delete"]
        System.out.println(
                driver.findElement(By.xpath(String.format("//div[@id=\"elements\"]//child::button[%s][string-length(substring-before(@class, 'manually')) >= 0 and string-length(substring-after(@class, 'manually')) = 0 and text()=\"Delete\"]",
                        String.valueOf(i)))).getText());

        driver.get("http://the-internet.herokuapp.com/challenging_dom");

        // A dynamic solution, it works with other th and td values as well.
        // Check out the function below starting on line 74.
        locateCell(driver, "Lorem", "Apeirian9");

        // A non-dynamic solution
//        System.out.println(
//                driver.findElement(By.xpath("//div[@id=\"content\"]//following::td[text() = \"Apeirian9\"]//preceding::td[1]"))
//                        .getText());


        // Printing out the text of the element that's next to the element with Ipsum value of 'Apeirian9'
        System.out.println(
                driver.findElement(By.xpath("//div[@id=\"content\"]//following::td[text() = \"Apeirian9\"]//following::td[1]"))
                        .getText());

        // Shutting down the browser.
        shutdown(driver);
    }

    // A function that takes table header text and the text of e.g IPSUM element, finds the e.g LOREM value
    // Suppressed inlining lints
    private void locateCell(WebDriver driver, @SuppressWarnings("SameParameterValue") String thValue, @SuppressWarnings("SameParameterValue") String tdValue){
        // Determining the order number of the desired th inside thead
        List<WebElement> thsInThead = driver.findElements(By.xpath("//div[@id=\"content\"]//following::th"));
        int theadIndex = 0;
        for (int j = 0; j < thsInThead.size(); j++){
            if (thsInThead.get(j).getText().equals(thValue)){
                // Catching the index for later usage
                theadIndex = j + 1;
            }
        }

        // Determining on which row the second argument of this function is, aka the IPSUM value
        // This function also works for other arguments besides "Lorem" and "Apeirian9"
        List<WebElement> allTableRows = driver.findElements(By.xpath("//th[text() = 'Lorem']//ancestor::thead//following-sibling::tbody//child::tr"));
        int rowIndex = 0;
        for (int k = 0; k < allTableRows.size(); k++){

            // If the second argument is discovered with the HTML markup
            if (allTableRows.get(k).getAttribute("outerHTML").contains(tdValue)){
                // Catching the index for later usage
                rowIndex = k + 2;
                break;
            }
        }

        // Printing out, using the indexes from the above in xpath
        System.out.println(
                driver.findElement(By.xpath(String.format("//div[@id=\"content\"]//following::tr[%s]//child::td[%s]",
                        String.valueOf(rowIndex), String.valueOf(theadIndex)))).getText());
    }

    private void shutdown(WebDriver driver) throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }
}
