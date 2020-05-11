package ch.aaap.assignment;

import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

  private Model model = null;

  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();

    // use the factory method
    this.model = Model.create(politicalCommunities, postalCommunities);
  }

  /** @return model */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    // canton -> districts -> political communities -> count (inner join)

    // check for illegal arguments by trying to match the code to the list
    if (!this.model.getCantons().stream().anyMatch(canton -> canton.getCode().equals(cantonCode)))
      throw new IllegalArgumentException();

    // from all cantons
    Set<String> districtsIds =
        this.model.getCantons().stream()
            // get the requested one
            .filter(canton -> canton.getCode().equals(cantonCode))
            // map to its district ids
            .map(canton -> canton.getDistrictsIds())
            // flatten out to a stream of strings
            .flatMap(districts -> districts.stream())
            // convert it to a set of strings so we can filter afterwards
            .collect(Collectors.toSet());

    // from all districts
    return this.model.getDistricts().stream()
        // filter those which belongs to the canton
        .filter(district -> districtsIds.contains(district.getNumber()))
        // map to their political ids
        .map(district -> district.getPoliticalCommunitiesIds())
        // map and cast the size of each political community per district
        .mapToLong(communities -> communities.size())
        // sum them up to get the result
        .sum();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    // canton -> districts -> count

    // check for illegal arguments by trying to match the code to the list
    if (!this.model.getCantons().stream().anyMatch(canton -> canton.getCode().equals(cantonCode)))
      throw new IllegalArgumentException();

    // from all cantons
    return this.model.getCantons().stream()
        // get the requested canton
        .filter(canton -> canton.getCode().equals(cantonCode))
        // get its districts ids
        .map(canton -> canton.getDistrictsIds())
        // map and cast to the total size of ids
        .mapToLong(districts -> districts.size())
        // sum them up
        .sum();
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistict(String districtNumber) {
    // district -> political communities -> count

    // check for illegal arguments by trying to match the number to the list
    if (!this.model.getDistricts().stream()
        .anyMatch(district -> district.getNumber().equals(districtNumber)))
      throw new IllegalArgumentException();

    // from all districts
    return this.model.getDistricts().stream()
        // get the requested district
        .filter(district -> district.getNumber().equals(districtNumber))
        // map to the political communities ids
        .map(district -> district.getPoliticalCommunitiesIds())
        // map and cast to the total size of ids
        .mapToLong(communities -> communities.size())
        // sum them up
        .sum();
  }

  /**
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    // postal community -> political communities -> districts -> names (inner joins)

    // collect the political communities of the given postal community from all
    Set<String> politicalCommunities =
        this.model.getPostalCommunities().stream()
            // filter the requested ones for the given zip code
            .filter(postal -> postal.getZipCode().equals(zipCode))
            // get the political communities
            .map(postal -> postal.getPoliticalCommunitiesIds())
            // flatten out to a stream of strings
            .flatMap(communities -> communities.stream())
            // convert it to a set of strings so we can filter afterwards
            .collect(Collectors.toSet());

    // collect the ids of the districts for the given political communities
    Set<String> districtIds =
        this.model.getPoliticalCommunities().stream()
            // filter the requested ones communities
            .filter(political -> politicalCommunities.contains(political.getNumber()))
            // map to the ids to query districts
            .map(community -> community.getDistrictId())
            // map back to a set of strings
            .collect(Collectors.toSet());

    // finally query districts and map to their names
    return this.model.getDistricts().stream()
        // filter the requested ones as usual
        .filter(district -> districtIds.contains(district.getNumber()))
        // map to their name
        .map(district -> district.getName())
        // back to a set of strings
        .collect(Collectors.toSet());
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    // postal community -> political communities -> biggest date (inner join)

    // collect the political communities
    Set<String> politicalCommunities =
        this.model.getPostalCommunities().stream()
            // filter for the given name
            .filter(postal -> postal.getName().equals(postalCommunityName))
            // map to the political communities
            .map(postal -> postal.getPoliticalCommunitiesIds())
            // flat the ids out
            .flatMap(communities -> communities.stream())
            // and convert back to a set of strings
            .collect(Collectors.toSet());

    // join and return the biggest date
    return this.model.getPoliticalCommunities().stream()
        // filter requested one
        .filter(political -> politicalCommunities.contains(political.getNumber()))
        // compare to get max date
        .max(Comparator.comparing(political -> political.getLastUpdate()))
        // map to the actual date
        .map(political -> political.getLastUpdate())
        // or throw as it is an optional value
        .orElseThrow();
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    // cantons -> count

    // simply count all cantons
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    // postal communities -> political communities -> count mutual exclusion (outer join)

    // from all postal communities
    Set<String> politicalCommunities =
        this.model.getPostalCommunities().stream()
            // map all of them to their political communities ids
            .map(postal -> postal.getPoliticalCommunitiesIds())
            // flatten them out
            .flatMap(communities -> communities.stream())
            // convert them to set
            .collect(Collectors.toSet());

    // mutually exclude the one that have a postal counterpart
    return this.model.getPoliticalCommunities().stream()
        // negate filter to "outer join" with given ones
        .filter(political -> !politicalCommunities.contains(political.getNumber()))
        // count them
        .count();
  }
}
