package com.mysl.api.common.lang;

import lombok.Data;

@Data
public class NoData extends NoMsg {
  String msg = "";

  public NoData(int code, String msg) {
    super(code);
    this.msg = msg;
  }
}
