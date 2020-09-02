/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/31
 */

package org.jjong.springbatch.sample2;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.xstream.XStreamMarshaller;

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
public class JobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;

  @Bean
  public JdbcPagingItemReader<Customer> customerJdbcPagingItemReader() {
    // reading database records using JDBC in a paging fashion

    JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
    reader.setDataSource(dataSource);
    reader.setFetchSize(1000);
    reader.setRowMapper(new BeanPropertyRowMapper<>(Customer.class));

    // sort keys
    Map<String, Order> sortkeys = new HashMap<>();
    sortkeys.put("id", Order.ASCENDING);

    // MySQL implementation of a pagingQueryProvider using database specific features.
    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("id, firstName, lastName, birthDate");
    queryProvider.setFromClause("from customer");
    queryProvider.setSortKeys(sortkeys);
    reader.setQueryProvider(queryProvider);

    return reader;
  }



  @Bean
  public FlatFileItemWriter<Customer> jsonItemWriter() throws Exception {

    String customerOutputPath = File.createTempFile("customerOutput_json", ".out", new File("/Users/bnjjong/temp")).getAbsolutePath();
    log.info(">>>>>>>>>>> Output path : {}", customerOutputPath);
    FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
    writer.setLineAggregator(new CustomLineAggregator());
    writer.setResource(new FileSystemResource(customerOutputPath));
    writer.afterPropertiesSet();
    return writer;
  }


  @Bean
  public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {

    String customerOutputPath = File.createTempFile("customerOutput_xml", ".out", new File("/Users/bnjjong/temp")).getAbsolutePath();
    log.info(">>>>>>>>>>> Output path : {}", customerOutputPath);
    Map<String, Class> aliases = new HashMap<>();
    aliases.put("customer", Customer.class);
    XStreamMarshaller marshaller = new XStreamMarshaller();
    marshaller.setAliases(aliases);

    // StAX and Marshaller for serializing object to XML.
    StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();
    writer.setRootTagName("customers");
    writer.setMarshaller(marshaller);
    writer.setResource(new FileSystemResource(customerOutputPath));
    writer.afterPropertiesSet();
    return writer;
  }

  @Bean
  public ClassifierCompositeItemWriter<Customer> classifierCustomerCompositeItemWriter() throws Exception {
    ClassifierCompositeItemWriter<Customer> compositeItemWriter = new ClassifierCompositeItemWriter<>();
    compositeItemWriter.setClassifier(new CustomerClassifier(xmlItemWriter(), jsonItemWriter()));
    return compositeItemWriter;
  }

  @Bean
  public Step sample2Step1() throws Exception {
    return stepBuilderFactory.get("step1-sample2")
        .<Customer, Customer>chunk(10)
        .reader(customerJdbcPagingItemReader())
        .writer(classifierCustomerCompositeItemWriter())
        .stream(xmlItemWriter())
        .stream(jsonItemWriter())
        .build();
  }

  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory.get("job_sample2")
        .start(sample2Step1())
        .build();
  }


}
