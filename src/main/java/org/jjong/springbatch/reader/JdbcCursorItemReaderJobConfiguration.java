/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/28
 */

package org.jjong.springbatch.reader;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

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
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource; // DataSource DI

  private static final int chunkSize = 10;
  
  @Bean
  public Job jdbcCursorItemReaderJob() {
    return jobBuilderFactory.get("jdbcCursorItemReaderJob")
        .start(jdbcCursorItemReaderStep())
        .build();
  }

  @Bean
  public Step jdbcCursorItemReaderStep() {
    return stepBuilderFactory.get("jdbcCursorItemReaderStep")
        .<Pay, Pay>chunk(chunkSize) // <Pay, Pay> 첫 번째 Reader 에서 반환할 타입, 두번째는 Writer 에서 파라미터로 넘어올 타입, chunkSize 트랜잭션 범위
        .reader(jdbcCursorItemReader())
        .writer(jdbcCursorItemWriter())
        .build();
  }

  @Bean
  public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
    return new JdbcCursorItemReaderBuilder<Pay>()
        .fetchSize(chunkSize) // Database에서 한번에 가져올 데이터 양을 나타냄. Cursor의 경우에는 fetch size 만큼 가져와 read 를 통해서 하나씩 가져온다.
        .dataSource(dataSource)
        .rowMapper(new BeanPropertyRowMapper<>(Pay.class)) // 매퍼 클래스 spring 에서 공식 지원하는 클래스 -> BeanPropertyRowMapper
        .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
        .name("jdbcCursorItemReader") // spring batch의 ExecutionContext 에서 저장되어질 이름
        .build();
  }

  private ItemWriter<Pay> jdbcCursorItemWriter() {
    return list -> {
      for (Pay pay : list) {
        log.info("Current pay = {}", pay);
      }
    };
  }


}
