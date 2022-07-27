package com.mysl.api.common.lang;

import lombok.Data;

@Data
public class NoMsg {
  int code = 0;

  public NoMsg(int code) {
    this.code = code;
  }
}
