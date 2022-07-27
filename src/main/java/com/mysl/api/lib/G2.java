package com.mysl.api.lib;

import com.mysl.api.entity.Group;

import lombok.Data;

@Data
public class G2 {
  public long id;
  public byte[] ddd;
  public byte[] permission;
  public String name;

  public G2(Group p) {
    id = p.getId();
    permission = p.getPermission();
    name = p.getName();
    ddd = AesFile.getBytes(id);
  }
}
