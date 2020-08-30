/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/31
 */

package org.jjong.springbatch.sample1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2020/08/31.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Step stepOne() {
    return stepBuilderFactory.get("stepOne")
        .tasklet(new MyTaskOne())
        .build();
  }

  @Bean
  public Step stepTwo() {
    return stepBuilderFactory.get("stepTwo")
        .tasklet(new MyTaskTwo())
        .build();
  }

  @Bean
  public Job demoJob() {
    return jobBuilderFactory.get("demoJob")
        .incrementer(new RunIdIncrementer())
        .start(stepOne())
        .next(stepTwo())
        .build();
  }


}
