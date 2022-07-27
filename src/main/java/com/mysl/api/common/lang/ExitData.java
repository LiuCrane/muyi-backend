package com.mysl.api.common.lang;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExitData extends NoData {
  Object data;

  public ExitData(int code, String msg, Object data) {
    super(code, msg);
    this.data = data;
  }

}
