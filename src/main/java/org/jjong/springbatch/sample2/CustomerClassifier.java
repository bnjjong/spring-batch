/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/31
 */

package org.jjong.springbatch.sample2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

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
public class CustomerClassifier implements Classifier<Customer, ItemWriter<Customer>> {

  private ItemWriter<Customer> evenItemWriter;
  private ItemWriter<Customer> oddItemWriter;

  public CustomerClassifier(
      ItemWriter<Customer> evenItemWriter,
      ItemWriter<Customer> oddItemWriter) {
    this.evenItemWriter = evenItemWriter;
    this.oddItemWriter = oddItemWriter;
  }

  @Override
  public ItemWriter<Customer> classify(Customer customer) {
    return customer.getId() % 2 == 0 ? evenItemWriter : oddItemWriter;
  }
}
