package com.mysl.api.lib;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import lombok.Data;

@Data
public class Token {
  public int timeOut = (int) (System.currentTimeMillis() / 1000);
  public long id;
  public long key = IdWorker.getId();
  public String password;
}
