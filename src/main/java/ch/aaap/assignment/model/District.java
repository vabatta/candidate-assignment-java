package ch.aaap.assignment.model;

import java.util.Set;

public class District {

  // constructor for a new District entity, only way to set its data
  public District(
      String number, String name, String cantonId, Set<String> politicalCommunitiesIds) {
    this.number = number;
    this.name = name;
    this.cantonId = cantonId;
    this.politicalCommunitiesIds = politicalCommunitiesIds;
  }

  // CSV column GDEBZNR from GDE file
  private final String number;

  public String getNumber() {
    return this.number;
  }

  // CSV column GDEBZNA from GDE file
  private final String name;

  public String getName() {
    return this.name;
  }

  // <- OneToMany relation to Canton, uses the canton code (unique per canton)
  private final String cantonId;

  public String getCantonId() {
    return this.cantonId;
  }

  // -> OneToMany relation to PoliticalCommunity, uses the table id (autoincrement
  // unique in the table)
  private final Set<String> politicalCommunitiesIds;

  public Set<String> getPoliticalCommunitiesIds() {
    return this.politicalCommunitiesIds;
  }
}
