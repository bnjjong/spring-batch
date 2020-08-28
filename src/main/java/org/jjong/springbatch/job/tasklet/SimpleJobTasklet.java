/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/28
 */

package org.jjong.springbatch.job.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * create on 2020/08/28.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
@Component
@StepScope
public class SimpleJobTasklet implements Tasklet {
  @Value("#{jobParameters[requestDate]}")
  private String requestDate;

  public SimpleJobTasklet() {
    log.info(">>>>>>>>>>>>>>>>>>>> tasklet 생성");
  }


  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>>> This is step1");
    log.info(">>>>>>>>>>>>>>>>>>>> requestDate = {}", requestDate);
    return RepeatStatus.FINISHED;
  }
}
