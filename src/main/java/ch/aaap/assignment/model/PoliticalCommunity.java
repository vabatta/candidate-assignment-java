package ch.aaap.assignment.model;

import java.time.LocalDate;

public class PoliticalCommunity {

  // constructor for a new PoliticalCommunity entity, only way to set its data
  public PoliticalCommunity(
      String number, String name, String shortName, LocalDate lastUpdate, String districtId) {
    this.number = number;
    this.name = name;
    this.shortName = shortName;
    this.lastUpdate = lastUpdate;
    this.districtId = districtId;
  }

  // CSV column GDENR from GDE file
  private final String number;

  public String getNumber() {
    return this.number;
  }

  // CSV column GDENAME from GDE file
  private final String name;

  public String getName() {
    return this.name;
  }

  // CSV column GDENAMK from GDE file
  private final String shortName;

  public String getShortName() {
    return this.shortName;
  }

  // CSV column GDEMUTDAT from GDE file
  private final LocalDate lastUpdate;

  public LocalDate getLastUpdate() {
    return this.lastUpdate;
  }

  // <- OneToMany relation to District, uses the district number
  private final String districtId;

  public String getDistrictId() {
    return this.districtId;
  }
}
