/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/31
 */

package org.jjong.springbatch.sample2;

import java.sql.ResultSet;
import java.sql.SQLException;

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
public class CustomerRowMapper implements org.springframework.jdbc.core.RowMapper<Customer> {

  @Override
  public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Customer.builder().id(rs.getLong("id"))
        .firstName(rs.getString("firstName"))
        .lastName(rs.getString("lastName"))
        .birthDate(rs.getString("birthDate"))
        .build();
  }
}
