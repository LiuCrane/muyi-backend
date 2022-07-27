package com.mysl.api.lib;

import lombok.Data;

@Data
public class Location {
  public Double latitude;
  public Double longitude;
  public String address;
  public Long time;
}
