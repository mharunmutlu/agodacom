package com.agodacom.step;

import com.thoughtworks.gauge.Step;

import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;


public class StepImplementation extends BaseSteps {
private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory.getLogger(BaseSteps.class);

  @Step("<int> saniye bekle.")
      public void waitSec(int seconds) throws InterruptedException {
      try{
          logger.info(seconds + "saniye beklendi");
          Thread.sleep(seconds * 1000);
        } catch (InterruptedException e){
          e.printStackTrace();
      }
  }

      @Step("<text> textini <key> elementine yaz.")
      public void sendKeysText(String text,String key){
          if(!key.equals("")){
              findElement(key).sendKeys(text);
              logger.info(key + "islem basarili...." + text + "yazildi....");

          }

      }

}
