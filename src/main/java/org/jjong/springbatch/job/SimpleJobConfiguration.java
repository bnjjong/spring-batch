/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/27
 */

package org.jjong.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jjong.springbatch.job.tasklet.SimpleJobTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2020/08/27.
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
@Configuration
public class SimpleJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final SimpleJobTasklet tasklet;

  @Bean
  public Job simpleJob() {
    return jobBuilderFactory.get("simpleJob")
        .start(simpleStep1())
        .next(simpleStep2(null))
        .build();
  }

//  @Bean
//  @JobScope
  public  Step simpleStep1() {
    return stepBuilderFactory.get("simpleStep1")
        .tasklet(tasklet)
        .build();
  }


  @Bean
  @JobScope
  public  Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
    return stepBuilderFactory.get("simpleStep2")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>> This is step2");
          log.info(">>>>>>>>>>> request date : {}", requestDate);
          return RepeatStatus.FINISHED;
        })
        .build();
  }

}
