package com.agodacom.step;

import com.agodacom.base.BaseTest;
import com.agodacom.helper.ElementHelper;
import com.agodacom.helper.StoreHelper;
import com.agodacom.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;
import java.time.Duration;
import java.util.*;

public class BaseSteps extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 5;

    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseSteps.class);

    //dogukan
    private Actions actions = new Actions(driver);
    private String compareText;

    public BaseSteps() {

        PropertyConfigurator
                .configure(BaseSteps.class.getClassLoader().getResource("log4j.properties"));
    }

    WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return driver.findElements(infoParam);
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }


    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }
        compareText = stringRandom + " km";
        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    @Step({"Check if element <key> exists",
            "Wait for element to load with key <key>",
            "Element var mı kontrol et <key>",
            "Elementin yüklenmesini bekle <key>"})
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait for element then click <key>",
            "Elementi bekle ve sonra tıkla <key>"})
    public void checkElementExistsThenClick(String key) {
        getElementWithKeyIfExists(key);
        clickElement(key);
        logger.info(key + " elementine tıklandı.");
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            hoverElement(element);
            waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine tıklandı.");
        }
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void ssendKeys(String text, String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            element.clear();
            element.sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }
    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Write random value to element <key>",
            "<key> elementine random değer yaz"})
    public void writeRandomValueToElement(String key) {
        findElement(key).sendKeys(randomString(5));
    }

    @Step ({"<key> li element comparetexti içeriyor mu"})
    public void checkelementtext(String key){
        logger.info("compareText: " + compareText);
        String actualText = findElement(key).getText();
        logger.info("actualText: " + actualText);
        Assert.assertTrue(actualText.contains(compareText));
        Assert.assertTrue(compareText.contains(actualText));
        //Assert.assertEquals("Text değerleri aynı değil", compareText , actualText);
        logger.info(compareText + "Texti " + actualText + " değerini içeriyor");
    }

    public void randomPick(String key) throws InterruptedException {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    @Step("<key> menu listesinden rasgele seç")
    public void chooseRandomElementFromList(String key) throws InterruptedException {
        for (int i = 0; i < 1; i++)
            randomPick(key);
    }

    @Step("<key> elementine <text> değerini js ile yaz")
    public void writeToKeyWithJavaScript(String key, String text) {
        WebElement element = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1]", element, text);
        logger.info(key + " elementine " + text + " değeri js ile yazıldı.");
    }

    @Step("<key> menu <day> tarihini seç")
    public void chooseRandomElementFromList(String key, String day) throws InterruptedException {
        List<WebElement> elements = findElements(key);
        for (int i = 0 ; i < elements.size() ; ) { //eğer güncel gün seçilirse i++ olduğu durumda fail oluyor.
            if (Objects.equals(elements.get(i).getText(), day)){
                System.out.println(elements.get(i).getText() + " günü seçildi");
                elements.get(i).click();
                break;
            }
            i++;
        }
    }

    public void highLighterMethod(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    @Step("<key> elemeninte enter tuşunu gönder")
    public void sendEnter(String key){
        WebElement element = findElement(key);
        element.sendKeys(Keys.ENTER);

    }
}