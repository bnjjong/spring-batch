/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/09/02
 */

package org.jjong.springbatch.marshaller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

/**
 * create on 2020/09/02.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
@RequiredArgsConstructor
public class MarshallerApplication {
  public static final String FILE_NAME = "settings.xml";
  private Settings settings = new Settings();
  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  public void setMarshaller(Marshaller marshaller) {
    this.marshaller = marshaller;
  }

  public void setUnmarshaller(Unmarshaller unmarshaller) {
    this.unmarshaller = unmarshaller;
  }
  public void saveSettings() throws IOException {
    try (FileOutputStream os = new FileOutputStream(FILE_NAME)) {
      this.marshaller.marshal(settings, new StreamResult(os));

    }
  }

  public void loadSettings() throws IOException {
    try (FileInputStream is = new FileInputStream(FILE_NAME)) {
//      this.settings = (Settings) this.unmarshaller.unmarshal(new StreamSource(is));
      Object result = this.unmarshaller.unmarshal(new StreamSource(is));
      log.info("result>>>>>> {}", result);
    }
  }

  public static void main(String[] args) throws IOException {
    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("applicationContext.xml");
    MarshallerApplication application = (MarshallerApplication) appContext.getBean("application");
//    application.saveSettings();
    application.loadSettings();
  }
}
