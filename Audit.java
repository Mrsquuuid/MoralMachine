
import ethicalengine.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Description
 * This class is an inspection of the algorithm with the goal of revealing inherent biases
 * that may be built in as an (un)intended consequence.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class Audit {
    public final static String INITIAL_TYPE = "Unspecified";
    private String auditType = INITIAL_TYPE;
    private int numberOfRuns = 0;
    private int numberOfSurvivors = 0;
    private double sumOfAge = 0.0;
    private ArrayList<Scenario> scenarioSequence;
    private final HashMap<String, int[]> statisticHashMap = new HashMap<>();
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Empty constructor
     */
    public Audit() {}

    /**
     * Constructor with a set of scenarios for auditing
     * @param scenarioSequence a sequence of specified scenarios for auditing
     */
    public Audit(ArrayList<Scenario> scenarioSequence) {
        this.scenarioSequence = scenarioSequence;
    }

    /**
     * auditing when scenarios is randomly generated
     * @param runs the number of times the Audit must be runInteractiveScenario (int)
     */
    public void run(int runs) {
        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
        int i = 0;
        while (i < runs) {
            Scenario scenario = scenarioGenerator.generate();
            EthicalEngine.Decision decision = EthicalEngine.decide(scenario);
            this.updateOutcome(scenario, decision);
            numberOfRuns++;
            i++;
        }
    }

    /**
     * the entrance to runInteractiveScenario an audit with config once with regard to input scenarios.
     */
    public void run() {
        int i = 0, scenarioSequenceSize = scenarioSequence.size();
        while (i < scenarioSequenceSize) {
            Scenario scenario = scenarioSequence.get(i);
            EthicalEngine.Decision result = EthicalEngine.decide(scenario);
            this.updateOutcome(scenario, result);
            numberOfRuns++;
            i++;
        }
    }

    /**
     * the entrance to run an audit in the interactive mode
     */
    public void runInteractiveAudit() {
        for (int i = 0, scenarioSequenceSize = scenarioSequence.size(); i < scenarioSequenceSize; i++) {
            Scenario scenario = scenarioSequence.get(i);
            EthicalEngine.Decision decision = EthicalEngine.Decision.PASSENGERS;
            boolean isDecided = false;
            System.out.print(scenario.toString());
            while (!isDecided) {
                System.out.println();
                String prompt = "Who should be saved? (passenger(s) [1] or pedestrian(s) [2])";
                System.out.println(prompt);
                String choiceOfUser = scanner.nextLine();
                if ("passenger".equals(choiceOfUser)
                        || "passengers".equals(choiceOfUser) || "1".equals(choiceOfUser)) {
                    decision = EthicalEngine.Decision.PASSENGERS;
                    isDecided = true;
                } else if ("pedestrian".equals(choiceOfUser)
                        || "pedestrians".equals(choiceOfUser) || "2".equals(choiceOfUser)) {
                    decision = EthicalEngine.Decision.PEDESTRIANS;
                    isDecided = true;
                } else {
                    System.out.print("Invalid response. ");
                }
            }
            this.updateOutcome(scenario, decision);
            numberOfRuns++;
        }
        scenarioSequence = new ArrayList<>(); // reinitialize the ArrayList for next invoking
    }

    /**
     * Setter to set the name of the audit type
     * @param name the name of the audit type
     */
    public void setAuditType(String name) {
        this.auditType = name;
    }

    /**
     * getter to get audit type
     * @return the type of audit
     */
    public String getAuditType() {
        return auditType;
    }

    /**
     * renew the final statistics of hashmap
     * @param scenario specified scenario (Scenario)
     * @param decision final decision (Decision)
     */
    private void updateOutcome(Scenario scenario, EthicalEngine.Decision decision) {
        ArrayList<Persona> survivors;
        ArrayList<Persona> victims;
        if (decision == EthicalEngine.Decision.PASSENGERS) {
            survivors = scenario.getPassengers();
        } else survivors = scenario.getPedestrians();
        if (decision == EthicalEngine.Decision.PASSENGERS) {
            victims = scenario.getPedestrians();
        } else victims = scenario.getPassengers();
        for (int i = 0, survivorsSize = survivors.size(); i < survivorsSize; i++) {
            Persona survivor = survivors.get(i);
            updatePersona(survivor, 1, scenario.isLegalCrossing());
        }
        for (int i = 0, victimsSize = victims.size(); i < victimsSize; i++) {
            Persona victim = victims.get(i);
            updatePersona(victim, 0, scenario.isLegalCrossing());
        }
    }

    /**
     * update the value of hashmap corresponding to the specified key
     * @param key specified key to find value
     * @param flagOfSurvival life status
     */
    private void updateValueOfMap(String key, int flagOfSurvival) {
        if (!key.equals("UNKNOWN") && !key.equals("NONE") && !key.equals("UNSPECIFIED")){
            // fill the hash map with 0 if absent
            statisticHashMap.putIfAbsent(key, new int[]{0, 0});
            // countArray[0] means total count, countArray[1] means survival count
            int[] countArray = statisticHashMap.get(key);
            // flagOfSurvival aims to identify if a persona is to be survive
            // if survive, value will be 1; if not, value will be 0
            statisticHashMap.replace(key, new int[]{countArray[0]+1, countArray[1]+ flagOfSurvival});
        }
    }

    /**
     * update the value of a persona
     * @param individual specified persona (persona)
     * @param flagOfSurvival life status (int)
     * @param isLegalCrossing life status (boolean)
     */
    private void updatePersona(Persona individual, int flagOfSurvival, boolean isLegalCrossing) {
        if (!isLegalCrossing) {
            updateValueOfMap("red", flagOfSurvival);
        } else {
            updateValueOfMap("green", flagOfSurvival);
        }

        if (individual instanceof Human) {
            if (flagOfSurvival == 1){
                sumOfAge = sumOfAge + individual.getAge();
                numberOfSurvivors++;
            }
            updateValueOfMap(individual.getBodyType().name(), flagOfSurvival);
            updateValueOfMap(individual.getGender().name(), flagOfSurvival);
            updateValueOfMap(((Human) individual).getProfession().name(), flagOfSurvival);
            updateValueOfMap(((Human) individual).getAgeCategory().name(), flagOfSurvival);
            updateValueOfMap("human", flagOfSurvival);
            if ((individual).isYou()){
                updateValueOfMap("you", flagOfSurvival);
            }
            if (((Human) individual).isPregnant()){
                updateValueOfMap("pregnant", flagOfSurvival);
            }
            if ((individual).getRole().equals(Persona.Role.PASSENGER)){
                updateValueOfMap("passengers", flagOfSurvival);
            } else if ((individual).getRole().equals(Persona.Role.PEDESTRIAN)){
                updateValueOfMap("pedestrians", flagOfSurvival);
            }
        }
        else if (individual instanceof Animal) {
            updateValueOfMap(((Animal) individual).getSpecies(), flagOfSurvival);
            updateValueOfMap("animal", flagOfSurvival);
            if (((Animal) individual).isPet()){
                updateValueOfMap("pet", flagOfSurvival);
            }
            if ((individual).getRole().equals(Persona.Role.PASSENGER)){
                updateValueOfMap("passengers", flagOfSurvival);
            } else if ((individual).getRole().equals(Persona.Role.PEDESTRIAN)){
                updateValueOfMap("pedestrians", flagOfSurvival);
            }
        }
    }

    /**
     * Pass scenarios into Audit.class for auditing
     * @param scenarioSequence a sequence of specified scenarios for auditing
     */
    public void inputScenarios(ArrayList<Scenario> scenarioSequence) {
        this.scenarioSequence = scenarioSequence;
    }

    /**
     * Override toString method to return a summary of the simulation
     * @return a String detailing the ratio of Saved
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("======================================\n");
        string.append("# ");
        string.append(getAuditType());
        string.append(" Audit\n");
        string.append("======================================\n");
        string.append("- % SAVED AFTER ");
        string.append(numberOfRuns);
        string.append(" RUNS\n");

        ArrayList <String[]> sortedList = new ArrayList<>();
        Iterator<String> iterator = statisticHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int[] countArray = statisticHashMap.get(key);
            double survivalRate = (double) countArray[1] / (double) countArray[0];
            double value = new BigDecimal(String.valueOf(survivalRate))
                    .setScale(2, RoundingMode.UP).doubleValue();
            sortedList.add(new String[]{key, String.valueOf(value)});
        }

        Collections.sort(sortedList, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                if (o1[1].toLowerCase().equals((o2[1]).toLowerCase())) {
                    if (o1[0].toLowerCase().compareTo((o2[0]).toLowerCase()) > 0) {
                        return 1;
                    } else if (o1[0].toLowerCase().compareTo((o2[0]).toLowerCase()) < 0) {
                        return -1;
                    } else return 0;
                } else if (o1[1].toLowerCase().compareTo((o2[1]).toLowerCase()) > 0) {
                    return -1;
                } else return 1;
            }
        });

        for (int i = 0, sortedListSize = sortedList.size(); i < sortedListSize; i++) {
            String[] strings = sortedList.get(i);
            string.append(strings[0].toLowerCase());
            string.append(": ");
            double value = Double.parseDouble(strings[1]);
            string.append(String.format("%.2f", value));
            string.append("\n");
        }
        string.append("--\n");
        string.append("average age: ").append(String.format("%.2f", sumOfAge / numberOfSurvivors));
        return string.toString();
    }

    /**
     * prints the summary returned by the toString() method to the command-line
     */
    public void printStatistic() {
        System.out.println(this.toString());
    }

    /**
     * Prints audit results to a specified file path.
     * @param filePath filePath of file output
     */
    public void printToFile(String filePath) {
        try {
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(filePath, true));
            outputStream.println(toString());
            outputStream.close();
            File folder = new File("./"+ filePath);
            if(!folder.exists()){
                System.out.println("ERROR: could not print results. Target directory does not exist.");
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
        }
    }
}