package ch.aaap.assignment.model;

import java.util.Set;

public class Canton {

  // constructor for a new Canton entity, only way to set its data
  public Canton(String code, String name, Set<String> districtsIds) {
    this.code = code;
    this.name = name;
    this.districtsIds = districtsIds;
  }

  // CSV column GDEKT from GDE file
  private final String code;

  public String getCode() {
    return this.code;
  }

  // CSV column GDEKTNA from GDE file
  private final String name;

  public String getName() {
    return this.name;
  }

  // -> OneToMany relation to District, uses the district's number (unique per canton)
  private final Set<String> districtsIds;

  public Set<String> getDistrictsIds() {
    return this.districtsIds;
  }
}
