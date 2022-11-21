import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

class Main {
  static int n;
  // resident preferences # suggestion: change it to hashmap of hashmaps,
  // (ranking)
  static ArrayList<int[]> resident_prefs = new ArrayList<int[]>();
  // hospital preferences
  static ArrayList<ArrayList<Integer>> hosp_prefs = new ArrayList<ArrayList<Integer>>();
  // forbidden matches
  static HashSet<ArrayList<Integer>> forbidden_matches = new HashSet<ArrayList<Integer>>();
  // final_hosp_matches[i] will contain the resident hospital i is finally matched
  // to
  static String[] final_hosp_matches = new String[2000];

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    n = Integer.parseInt(br.readLine());
    // reading in hospital preferences
    for (int i = 0; i < n; i++) {
      hosp_prefs.add(new ArrayList<Integer>());
      String[] line = br.readLine().split(" ");
      for (int j = 0; j < n; j++) {
        int next_pref = Integer.parseInt(line[j]);
        hosp_prefs.get(i).add(next_pref);
      }
    }

    // reading in resident preferences
    for (int i = 0; i < n; i++) {
      resident_prefs.add(new int[n]);
      // resident_prefs.add(new HashMap<Integer, Integer>());
      String[] line = br.readLine().split(" ");
      for (int j = 0; j < n; j++) {
        int next_pref = Integer.parseInt(line[j]);
        resident_prefs.get(i)[next_pref] = j;
      }
    }
    int line_number = Integer.parseInt(br.readLine());
    // reading in forbidden matches
    for (int i = 0; i < line_number; i++) {
      String[] line = br.readLine().split(" ");
      ArrayList<Integer> pair = new ArrayList<Integer>();
      pair.add(Integer.parseInt(line[0]));
      pair.add(Integer.parseInt(line[1]));
      forbidden_matches.add(pair);
    }
    br.close();
    // map of matched pairs. key: resident, value: hospital
    HashMap<Integer, Integer> resident_hosp_matches = new HashMap<Integer, Integer>();
    // map of matched pairs. key: hosptial, value: resident
    HashMap<Integer, String> hosp_matches = new HashMap<Integer, String>();
    // currently matched hospitals
    HashSet<Integer> matched_hosps = new HashSet<Integer>();

    // next_proposal[i] is the the next index in the preference list that hospital i
    // is going to propose to
    int[] next_proposal = new int[n];
    for (int i = 0; i < n; i++) {
      next_proposal[i] = 0;
    }

    // repeating G-S loop until all hospitals are matched
    while (matched_hosps.size() < n) {
      for (int i = 0; i < n; i++) {
        if (!matched_hosps.contains(i)) {
          // resident the hospital wants to propose to in this round
          int next_pref = 0;
          try {
            next_pref = hosp_prefs.get(i).get(next_proposal[i]);
          } catch (Exception e) {
            hosp_matches.put(i, null);
            matched_hosps.add(i);
            break;
          }
          // checking if the preferred resident is already matched
          ArrayList<Integer> to_check = new ArrayList<Integer>();
          to_check.add(i);
          to_check.add(next_pref);
          boolean forbidden = forbidden_matches.contains(to_check);
          if (resident_hosp_matches.containsKey(next_pref) && (!forbidden)) {
            int curr_match = resident_hosp_matches.get(next_pref);
            if (resident_prefs.get(next_pref)[curr_match] > resident_prefs.get(next_pref)[i]) {
              resident_hosp_matches.put(next_pref, i);
              hosp_matches.put(i, String.valueOf(next_pref));
              matched_hosps.add(i);
              matched_hosps.remove(curr_match);
            }
          } else if (!forbidden) {
            resident_hosp_matches.put(next_pref, i);
            hosp_matches.put(i, String.valueOf(next_pref));
            matched_hosps.add(i);
          }
          if (next_proposal[i] <= n - 1) {
            next_proposal[i]++;
          }
        }
      }
    }
    // need to do some processing to get hospital:resident key value pairs for
    // matches
    for (int hosp = 0; hosp < n; hosp++) {
      final_hosp_matches[hosp] = hosp_matches.get(hosp) != null ? hosp_matches.get(hosp) : "*";
    }
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    for (int hosp = 0; hosp < n; hosp++) {
      bw.write(String.valueOf(hosp_matches.get(hosp) != null ? hosp_matches.get(hosp) : "*"));
      bw.write('\n');
    }
    bw.flush();
    bw.close();
  }
}
