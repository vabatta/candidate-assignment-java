package ch.aaap.assignment.raw;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CSVPoliticalCommunity {

  // GDENR
  private String number;

  // GDENAME
  private String name;

  // GDENAMK
  private String shortName;

  // GDEKT
  private String cantonCode;

  // GDEKTNA
  private String cantonName;

  // GDEBZNR
  private String districtNumber;

  // GDEBZNA
  private String districtName;

  // GDEMUTDAT
  private LocalDate lastUpdate;
}
