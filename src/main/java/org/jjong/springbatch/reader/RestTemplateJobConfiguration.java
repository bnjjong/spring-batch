/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/28
 */

package org.jjong.springbatch.reader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * create on 2020/08/28.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 * 참조 링크
 * <a href="https://innopc.tistory.com/31"/>
 * @author jshan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestTemplateJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;
  private static final int CHUNK_SIZE = 5;
  private boolean isCallRestAPI = false; //RestAPI 호출여부 판단

  @Bean
  public Job collectJob() {
    return jobBuilderFactory.get("restTemplateJob")
        .start(collectStep())
        .build();
  }

  @Bean
  public Step collectStep() {
    return stepBuilderFactory.get("collectStep")
        .<String, String>chunk(CHUNK_SIZE)
        .reader(restCollectReader())
        .writer(collectWriter())
        .build();
  }

  @Bean
  public ItemReader<String> restCollectReader() {
   return new ItemReader<String>() {
     @Override
     public String read()
         throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
       if (!isCallRestAPI) {
         String uri = "https://makko.co.kr/rss";
         RestTemplate restTemplate = new RestTemplate();
         restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
         String result = restTemplate.getForObject(uri, String.class);
         log.info("result : {}", result);
         isCallRestAPI = true;
         return result;
       }
       return null;
     }
   };

  }

  private ItemWriter<String> collectWriter() {
    return list -> {
      for (String str : list) {
        log.info("result in writer : {}", str);
      }
    };
  }

}
