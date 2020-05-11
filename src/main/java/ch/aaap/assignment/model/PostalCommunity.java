package ch.aaap.assignment.model;

import java.util.Set;

public class PostalCommunity {

  // constructor for a new PostalCommunity entity, only way to set its data
  public PostalCommunity(
      String zipCode, String zipCodeAddition, String name, Set<String> politicalCommunitiesIds) {
    this.zipCode = zipCode;
    this.zipCodeAddition = zipCodeAddition;
    this.name = name;
    this.politicalCommunitiesIds = politicalCommunitiesIds;
  }

  // CSV column PLZ4 from PLZ6 file
  private final String zipCode;

  public String getZipCode() {
    return this.zipCode;
  }

  // CSV column PLZZ from PLZ6 file
  private final String zipCodeAddition;

  public String getZipCodeAddition() {
    return this.zipCodeAddition;
  }

  // CSV column PLZNAMK from PLZ6 file
  private final String name;

  public String getName() {
    return this.name;
  }

  // ManyToMany relation to PoliticalCommunity, uses the community number (unique)
  private final Set<String> politicalCommunitiesIds;

  public Set<String> getPoliticalCommunitiesIds() {
    return this.politicalCommunitiesIds;
  }
}
