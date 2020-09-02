/*
 * COPYRIGHT (c) Enliple 2019
 * This software is the proprietary of Enliple
 *
 * @author <a href="mailto:jshan@enliple.com">jshan</a>
 * @since 2020/09/02
 */

package org.jjong.springbatch.marshaller;

import lombok.Data;

/**
 * create on 2020/09/02.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Data
public class Settings {
  private boolean fooEnabled;

  public boolean isFooEnabled() {
    return fooEnabled;
  }

  public void setFooEnabled(boolean fooEnabled) {
    this.fooEnabled = fooEnabled;
  }

}
