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

    static void holdReleaseElements(WebDriver driver, WebElement startEl, WebElement endEl, boolean drag) throws InterruptedException {
//        SELENIUM HAS ISSUES WITH DRAGGABLE="true" ELEMENTS
//        See issue on: https://github.com/SeleniumHQ/selenium/issues/3269

        // If the drag flag is set to on.
        if (drag){
            // SOLUTION 1: DOES NOT WORK
//            new Actions(driver).dragAndDrop(startEl, endEl).perform();

            // SOLUTION 2: DOES NOT WORK, even with an initial offset
//            new Actions(driver).clickAndHold(startEl).moveToElement(endEl).release().perform();

            // SOLUTION 3: DOES NOT WORK
//            Actions actionsObject = new Actions(driver);
//            for (int i = 0; i < 20; i++){
//                actionsObject.sendKeys(Keys.TAB);
//            }
//            actionsObject.sendKeys(Keys.SPACE);
//            actionsObject.sendKeys(Keys.ARROW_RIGHT);
//            actionsObject.sendKeys(Keys.SPACE);
//            actionsObject.perform();

            // SOLUTION 4: WORKS. Using JavaScript Executor and some JS scripts to do the dragging on the low level.
            // At this point, I've tried everything. This is the only thing that sends Box A to the right of Box B.
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("function createEvent(typeOfEvent) {\n"
                    + "var event = document.createEvent(\"CustomEvent\");\n"
                    + "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n"
                    + "data: {},\n" + "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
                    + "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n"
                    + "return event;\n" + "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
                    + "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
                    + "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
                    + "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n"
                    + "}\n" + "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
                    + "var dragStartEvent =createEvent('dragstart');\n"
                    + "dispatchEvent(element, dragStartEvent);\n" + "var dropEvent = createEvent('drop');\n"
                    + "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
                    + "var dragEndEvent = createEvent('dragend');\n"
                    + "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
                    + "var source = arguments[0];\n" + "var destination = arguments[1];\n"
                    + "simulateHTML5DragAndDrop(source,destination);", startEl, endEl);
        }
        else {
            // If the drag flag is set to off.
            new Actions(driver).clickAndHold(startEl).moveToElement(endEl).release().perform();
        }

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
