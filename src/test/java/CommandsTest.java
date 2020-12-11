import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CommandsTest {

    // Test block
    @Test
    public void testExecution() throws InterruptedException, AWTException {
        // Engaging WebDriverManager tool, setting up the path to chromedriver.exe.
        WebDriverManager.chromedriver().setup();

        // Instantiating object driver of type WebDriver, maximizing the window, getting the first page.
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        Helpers.getURL(driver, "http://the-internet.herokuapp.com/context_menu");

        // Capturing the element that's to be right-clicked, performing contextClick() on it,
        // Switching to alert and accepting it.
        WebElement ctx = driver.findElement(By.id("hot-spot"));
        Actions actionsObject = new Actions(driver);
        actionsObject.contextClick(ctx).perform();
        driver.switchTo().alert().accept();
        System.out.println("Accepted.");

        // Using Robot class to press ESCAPE key and exit the context menu.
        // I am using Robot because the context menu is NOT part of the HTML code,
        // It's a part of the browser, which Selenium supposedly has no control over.
        // NOTE: the browser has to be in the FOREGROUND while executing for Robot to work.
        Robot robotObject = new Robot();
        robotObject.keyPress(KeyEvent.VK_ESCAPE);
        robotObject.keyRelease(KeyEvent.VK_ESCAPE);

        // Tip: see helper functions in test/java/Helpers.java
        Helpers.getURL(driver, "http://the-internet.herokuapp.com/dynamic_controls");

        // Catching Enable button, clicking it and waiting until "It's enabled!" text is displayed and the
        // input is enabled.
        driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form[2]/button")).click();
        Thread.sleep(5000);

        // Checking for validity.
        if (driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/form[2]/input")).isEnabled()
            || driver.findElement(By.id("message")).getText().equals("It's enabled!")){
            System.out.println("Check passed.");
        }
        else {
            System.out.println("Check failed");
        }

        // Hopping URL
        Helpers.getURL(driver, "http://uitestpractice.com/Students/Actions");

        // Catching starting and destination elements, saving initial background color for later comparison.
        WebElement startEl = driver.findElement(By.name("one"));
        WebElement endEl = driver.findElement(By.name("twelve"));
        String initialBg = Helpers.checkBackground(startEl);

        // Performing clickAndHold and release combination of actions with drag flag set to off.
        Helpers.holdReleaseElements(driver, startEl, endEl);
        String changedBg = Helpers.checkBackground(startEl);

        // Checking for validity.
        if (!initialBg.equals(changedBg)){
            System.out.println("Background changed.");
        }
        else {
            System.out.println("Background did not change.");
        }

        // Hopping URL
        Helpers.getURL(driver, "http://the-internet.herokuapp.com/drag_and_drop");

        // Catching draggable elements
        WebElement elementA = driver.findElement(By.id("column-a"));
        WebElement elementB = driver.findElement(By.id("column-b"));

        // Checking for validity
        if (elementA.getLocation().x < elementB.getLocation().x){
            System.out.println(elementA.getLocation().x);
            System.out.println(elementB.getLocation().x);
            System.out.println("Box B is positioned to the right of Box A");
        }
        else {
            System.out.println(elementA.getLocation().x);
            System.out.println(elementB.getLocation().x);
            System.out.println("Box B is not positioned to the right of Box A.");
        }

        // Closing the browser after 6 seconds from test finish.
        Helpers.tearDown(driver);
    }
}
