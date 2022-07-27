package com.mysl.api.lib;

import java.util.ArrayList;
import java.util.List;

import com.mysl.api.entity.Address;
import com.mysl.api.entity.Group;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.Users2;

import lombok.Data;

@Data
public class GlobalData {
  public List<Address> address = new ArrayList<>();
  public List<Media> media = new ArrayList<>();
  public List<Group> group = new ArrayList<>();
  public List<Users2> users = new ArrayList<>();
  public Users2 me = new Users2();
}
