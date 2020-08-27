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
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
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
@Configuration
@RequiredArgsConstructor
public class StepNextJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job stepNextJob() {
    return jobBuilderFactory.get("stepNextJob")
        .start(step1())
        .next(step2())
        .next(step3())
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>>>>> This is step1");
          return RepeatStatus.FINISHED;
        }).build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>>>>> This is step2");
          return RepeatStatus.FINISHED;
        }).build();
  }

  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>>>>> This is step3");
          return RepeatStatus.FINISHED;
        }).build();
  }
}