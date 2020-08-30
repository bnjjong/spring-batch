/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/31
 */

package org.jjong.springbatch.sample2;

import com.fasterxml.jackson.databind.ObjectMapper;

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
public class CustomLineAggregator implements
    org.springframework.batch.item.file.transform.LineAggregator<Customer> {

  private ObjectMapper objectMapper = new ObjectMapper();


  @Override
  public String aggregate(Customer item) {
    try {
      return objectMapper.writeValueAsString(item);
    } catch (Exception e) {
      throw new RuntimeException("Unable to serialize Customer", e);
    }
  }
}
