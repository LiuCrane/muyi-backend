package com.mysl.api.lib;

import lombok.Data;

@Data
public class HMU {
  public Long id;
  public Long mrid;
  public Long createTime;
  public Long endTime;
  public Long user;
  public Long media;
  public Integer read;
  public String location;
  public String type;
  public String title;
  public String img;
  public String simple;
  public Long group;
  public String infoPrivate;
}
