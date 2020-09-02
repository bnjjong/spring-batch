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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

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
public class ColumnRangePartitioner implements Partitioner {

  private JdbcOperations jdbcOperations;
  @Setter
  private String table;
  @Setter
  private String column;

  public void setDataSource(DataSource dataSource) {
    this.jdbcOperations = new JdbcTemplate(dataSource);
  }

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    int min = jdbcOperations.queryForObject("SELECT MIN("+column+") FROM "+ table, Integer.class);
    int max = jdbcOperations.queryForObject("SELECT MAX("+column+") FROM "+ table, Integer.class);
    int targetSize = (max - min) / gridSize + 1;

    Map<String, ExecutionContext> result = new HashMap<>();

    int number = 0;
    int start = min;
    int end = start + targetSize -1 ;

    while (start <= max) {
      ExecutionContext value = new ExecutionContext();
      result.put("partition" + number, value);

      if ( end >= max) {
        end = max;
      }

      value.putInt("minValue", start);
      value.putInt("maxValue", end);

      start += targetSize;
      end += targetSize;

      number++;
    }
    return result;
  }
}
