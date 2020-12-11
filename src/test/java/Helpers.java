import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


// HELPER FUNCTIONS to keep the driver code nice and tidy. All the dirt is here.
class Helpers {
    // This function gets the URL
    // Had to place the SuppressWarning tag because IntelliJ warning about inlining the URL is quite annoying.
    static void getURL(WebDriver driver, @SuppressWarnings("SameParameterValue") String URL){
        driver.get(URL);
    }

    // This function performs clickAndHold, moveToElement and release actions on start-end elements.
    static void holdReleaseElements(WebDriver driver, WebElement startEl, WebElement endEl) throws InterruptedException {
        new Actions(driver).clickAndHold(startEl).moveToElement(endEl).release().perform();
    }

    // This function gets the css value of the background property.
    static String checkBackground(WebElement element){
        return element.getCssValue("background");
    }

    static void tearDown(WebDriver driver) throws InterruptedException {
        Thread.sleep(6000);
        driver.quit();
    }

}
