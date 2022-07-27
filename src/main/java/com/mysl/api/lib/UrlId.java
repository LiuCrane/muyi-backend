package com.mysl.api.lib;

import lombok.Data;

@Data
public class UrlId {
  public long id;
  public String url;

  public UrlId(long id, String url) {
    this.id = id;
    this.url = url;
  }
}
