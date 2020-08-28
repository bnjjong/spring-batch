/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/08/28
 */

package org.jjong.springbatch.reader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:22");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long amount;
  private String txName;
  private LocalDateTime txDateTime;

  public Pay(Long amount, String txName, String txDateTime) {
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
  }

  public Pay(Long id,  Long amount, String txName, String txDateTime) {
    this.id = id;
    this.amount = amount;
    this.txName = txName;
    this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
  }
}
