package ch.aaap.assignment.model;

import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Model {
  /**
   * Factory method that creates a new model from raw data.
   *
   * @param rawPoliticalCommunities Raw, parsed CSV dataset for political communities.
   * @param rawPostalCommunities Raw, parsed CSV dataset for postal communities.
   * @return initialized model
   */
  public static Model create(
      Set<CSVPoliticalCommunity> rawPoliticalCommunities,
      Set<CSVPostalCommunity> rawPostalCommunities) {
    // maps for the data we need from political communities
    Map<String, Canton> cantons = new HashMap<>();
    Map<String, District> districts = new HashMap<>();
    Map<String, PoliticalCommunity> politicalCommunities = new HashMap<>();
    // iterate through the political communities and extract relevant models
    for (CSVPoliticalCommunity raw : rawPoliticalCommunities) {
      Model.processCanton(raw, cantons);
      Model.processDistrict(raw, districts);
      Model.processPoliticalCommunity(raw, politicalCommunities);
    }
    // map for the data we need from the postal communities
    Map<String, PostalCommunity> postalCommunities = new HashMap<>();
    // iterate through the postal communities and extract relevant models
    for (CSVPostalCommunity raw : rawPostalCommunities) {
      Model.processPostalCommunity(raw, postalCommunities);
    }

    // create the new model converting to sets and return it
    return new Model(
        new HashSet<>(politicalCommunities.values()),
        new HashSet<>(postalCommunities.values()),
        new HashSet<>(cantons.values()),
        new HashSet<>(districts.values()));
  }

  private static void processCanton(CSVPoliticalCommunity raw, Map<String, Canton> cantons) {
    // if the cantons map already contains the canton, add the district to the relation ids
    if (cantons.containsKey(raw.getCantonCode())) {
      Canton canton = cantons.get(raw.getCantonCode());
      // use the unique district number
      canton.getDistrictsIds().add(raw.getDistrictNumber());
    }
    // otherwise create a new entity
    else {
      Set<String> districtsIds = new HashSet<>();
      districtsIds.add(raw.getDistrictNumber());
      // construct the new entity
      Canton newCanton = new Canton(raw.getCantonCode(), raw.getCantonName(), districtsIds);
      // put it in the map
      cantons.put(raw.getCantonCode(), newCanton);
    }
  }

  private static void processDistrict(CSVPoliticalCommunity raw, Map<String, District> districts) {
    // if the districts map already contains the district
    if (districts.containsKey(raw.getDistrictNumber())) {
      District district = districts.get(raw.getDistrictNumber());
      // add the unique table key / id
      district.getPoliticalCommunitiesIds().add(raw.getNumber());
    }
    // otherwise create a new entity
    else {
      Set<String> politicalCommunitiesIds = new HashSet<>();
      politicalCommunitiesIds.add(raw.getNumber());
      // construct the new entity
      District newDistrict =
          new District(
              raw.getDistrictNumber(),
              raw.getDistrictName(),
              raw.getCantonCode(),
              politicalCommunitiesIds);
      // put it in the map
      districts.put(raw.getDistrictNumber(), newDistrict);
    }
  }

  private static void processPoliticalCommunity(
      CSVPoliticalCommunity raw, Map<String, PoliticalCommunity> politicalCommunities) {
    // check that it is added one single time from the table
    if (!politicalCommunities.containsKey(raw.getNumber())) {
      // create the new entity
      PoliticalCommunity newPoliticalCommunity =
          new PoliticalCommunity(
              raw.getNumber(),
              raw.getName(),
              raw.getShortName(),
              raw.getLastUpdate(),
              raw.getDistrictNumber());
      // put it in the map
      politicalCommunities.put(raw.getNumber(), newPoliticalCommunity);
    }
  }

  private static void processPostalCommunity(
      CSVPostalCommunity raw, Map<String, PostalCommunity> postalCommunities) {
    // create the unique postal id using the zip code and the zip code addition
    String postalId = raw.getZipCode() + raw.getZipCodeAddition();
    // if the map already contains the postal community
    if (postalCommunities.containsKey(postalId)) {
      PostalCommunity postalCommunity = postalCommunities.get(postalId);
      // add the unique id
      postalCommunity.getPoliticalCommunitiesIds().add(raw.getPoliticalCommunityNumber());
    }
    // otherwise create a new entity
    else {
      Set<String> politicalCommunitiesIds = new HashSet<>();
      politicalCommunitiesIds.add(raw.getPoliticalCommunityNumber());
      // create the new entity
      PostalCommunity newPostalCommunity =
          new PostalCommunity(
              raw.getZipCode(), raw.getZipCodeAddition(), raw.getName(), politicalCommunitiesIds);
      // put it in the map
      postalCommunities.put(postalId, newPostalCommunity);
    }
  }

  // Class model

  private final Set<PoliticalCommunity> politicalCommunities;
  private final Set<PostalCommunity> postalCommunities;
  private final Set<Canton> cantons;
  private final Set<District> districts;

  public Model(
      Set<PoliticalCommunity> politicalCommunities,
      Set<PostalCommunity> postalCommunities,
      Set<Canton> cantons,
      Set<District> districts) {
    this.politicalCommunities = politicalCommunities;
    this.postalCommunities = postalCommunities;
    this.cantons = cantons;
    this.districts = districts;
  }

  public Set<PoliticalCommunity> getPoliticalCommunities() {
    return this.politicalCommunities;
  }

  public Set<PostalCommunity> getPostalCommunities() {
    return this.postalCommunities;
  }

  public Set<Canton> getCantons() {
    return this.cantons;
  }

  public Set<District> getDistricts() {
    return this.districts;
  }
}
