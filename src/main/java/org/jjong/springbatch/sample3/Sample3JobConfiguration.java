/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/09/01
 */

package org.jjong.springbatch.sample3;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jjong.springbatch.sample2.Customer;
import org.jjong.springbatch.sample2.CustomerRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * create on 2020/09/01.
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
@AllArgsConstructor
public class Sample3JobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;

  @Bean
  public ColumnRangePartitioner partitioner() {
    ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
    columnRangePartitioner.setColumn("id");
    columnRangePartitioner.setDataSource(dataSource);
    columnRangePartitioner.setTable("customer");

    return columnRangePartitioner;
  }

  @Bean
  @StepScope
  public JdbcPagingItemReader<Customer> pagingItemReader(
      @Value("#{stepExecutionContext['minValue']}") Long minValue,
      @Value("#{stepExecutionContext['maxValue']}") Long maxValue
  ) {
    log.info("reading {} to {}", minValue, maxValue );

    Map<String, Order> sortKeys = new HashMap<>();
    sortKeys.put("id", Order.ASCENDING);

    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("id, firstName, lastName, birthDate");
    queryProvider.setFromClause("from customer");
    queryProvider.setWhereClause("where id >= " + minValue + " and id <= " + maxValue);
    queryProvider.setSortKeys(sortKeys);

    JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
    reader.setDataSource(this.dataSource);
    reader.setFetchSize(1000);
    reader.setRowMapper(new CustomerRowMapper());
    reader.setQueryProvider(queryProvider);

    return reader;
  }

  @Bean
  @StepScope
  public JdbcBatchItemWriter<Customer> customerItemWriter() {
    JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
    itemWriter.setDataSource(dataSource);
    itemWriter.setSql("INSERT INTO new_customer VALUES (:id, :firstName, :lastName, :birthDate)");

    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    itemWriter.afterPropertiesSet();

    return itemWriter;
  }

  // Master
  @Bean
  public Step sample3Step1() {
    return stepBuilderFactory.get("sample3Step1")
        .partitioner(slaveStep().getName(), partitioner())
        .step(slaveStep())
        .gridSize(4)
        .taskExecutor(new SimpleAsyncTaskExecutor())
        .build();
  }

  // Slave step
  @Bean
  public Step slaveStep() {
    return stepBuilderFactory.get("slaveStep")
        .<Customer, Customer>chunk(1000)
        .reader(pagingItemReader(null, null))
        .writer(customerItemWriter())
        .build();
  }

  @Bean
  public Job sample3Job() {
    return jobBuilderFactory.get("sample3Job")
        .start(sample3Step1())
        .build();
  }


}
