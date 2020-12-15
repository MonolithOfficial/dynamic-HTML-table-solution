import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;

public class WebElementsTest {
    @Test
    public void executeTest() throws InterruptedException {
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

        // Printing out the LOREM VALUE of an element which has 'Apeirian9' as it's Ipsum value.
        System.out.println(
                driver.findElement(By.xpath("//div[@id=\"content\"]//following::td[text() = \"Apeirian9\"]//preceding::td[1]"))
                        .getText());


        // Printing out the text of the element that's next to the element with Ipsum value of 'Apeirian9'
        System.out.println(
                driver.findElement(By.xpath("//div[@id=\"content\"]//following::td[text() = \"Apeirian9\"]//following::td[1]"))
                        .getText());

        // Shutting down the browser.
        shutdown(driver);
    }

    private void shutdown(WebDriver driver) throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }
}
