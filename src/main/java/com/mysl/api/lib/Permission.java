package com.mysl.api.lib;

import com.mysl.api.entity.Users;

import lombok.Data;

@Data
public class Permission {

  public long id;
  public long changeTime;
  public byte[] permission;
  public byte[] key;
  public Long group;
  public String cmr;
  // public String name;

  public Permission() {
    super();
  }

  public Permission(byte[] per) {
    permission = per;
    changeTime = AesFile.time();
  }

  public Permission(Users user) {
    id = user.getId();
    permission = user.getPermission();
    changeTime = user.getChangeTime();
    key = user.getKey();
    group = user.getGroup();
    cmr = user.getCmr();
  }

  public boolean node(int bit, int node) {
    return 0 > bit || bit > permission.length ? false : ((permission[bit] & node) == node);
  }
}
