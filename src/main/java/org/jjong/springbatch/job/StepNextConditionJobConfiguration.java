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
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job stepNextConfitionalJob() {
    return jobBuilderFactory.get("stepNextConditionalJob")
        .start(conditionalJobStep1())
          .on("FAILED") // FAILED 일 경우
          .to(conditionalJobStep3()) // step3로 이동
          .on("*") // step3 의 결과 관계 없이
          .end() // step3로 이동하면 flow 가 종료 된다.
        .from(conditionalJobStep1()) // step1로부터
          .on("*") // FAILED외에 모든 경우
          .to(conditionalJobStep2()) // step2로 이동
          .next(conditionalJobStep3()) // step2가 정상 종료 되면 step3로 이동
          .on("*") // step3의 결과 관계 없이
          .end() // step3 로 이동하면 flow 종료
        .end() // job 종료
        .build();
  }

  @Bean
  public Step conditionalJobStep1() {
    return stepBuilderFactory.get("step1")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>> This is stepNextConditionalJob step1");
          /**
           * ExitStatus를 FAILED로 지정한다.
           * 해당 status를 보고 flow가 진행된다.
           */
//          contribution.setExitStatus(ExitStatus.FAILED);
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  private Step conditionalJobStep2() {
    return stepBuilderFactory.get("conditionalJobStep2")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>> This is stepNextConditionalJob step2");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  private Step conditionalJobStep3() {
    return stepBuilderFactory.get("conditionalJobStep3")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>>>>>> This is stepNextConditionalJob step3");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

}
