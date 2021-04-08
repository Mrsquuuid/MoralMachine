
import ethicalengine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ethicalengine.Human.AgeCategory.*;
import static ethicalengine.Human.Profession.*;
import static ethicalengine.Persona.BodyType.*;

/**
 * Description
 * This class holds the main method and manages program execution,
 * and coordinates the program flow, which also includes interactive mode.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class EthicalEngine {
    public final static String DEFAULT_LOG_PATH = "results.log";
    public final static String DEFAULT_WELCOME_PATH = "welcome.ascii";
    public final static int DEFAULT_RUNS = 3;
    private ArrayList<Scenario> scenariosFromConfig = new ArrayList<>();
    private boolean isConfig = false;
    private boolean isToSave = false;
    private String pathOfResult = DEFAULT_LOG_PATH;

    private final double weightOfIllegalCrossing = 0.60;
    private final double weightOfPregnant = 2.50;
    private final double weightOfAnimal = 0.015;
    private final double weightOfPet = 20.00;
    private final double weightOfYou = 2.0;

    /**
     * Assign weight for different professions
     */
    final HashMap<Human.Profession, Double> weightOfProfession = new HashMap<>() {
        {
            put(CEO, 1.10);
            put(CRIMINAL, 0.50);
            put(DOCTOR, 1.80);
            put(HOMELESS, 1.00);
            put(UNEMPLOYED, 1.00);
            put(PROFESSOR, 1.50);
            put(ENGINEER, 1.30);
            put(TEACHER, 1.30);
            put(WORKER, 1.10);
            put(NONE, 1.00);
            put(null, 1.00);
        }
    };

    /**
     * Assign weight for different AgeCategories
     */
    final HashMap<Human.AgeCategory, Double> weightOfAgeCategory = new HashMap<>() {
        {
            put(BABY, 2.50);
            put(CHILD, 2.00);
            put(ADULT, 1.00);
            put(SENIOR, 0.80);
            put(null, 1.00);
        }
    };

    /**
     * Assign weight for different body types
     */
    final HashMap<Persona.BodyType, Double> weightOfBodyType = new HashMap<>() {
        {
            put(AVERAGE, 1.00);
            put(ATHLETIC, 1.050);
            put(OVERWEIGHT, 0.950);
            put(UNSPECIFIED, 1.00);
            put(null, 1.00);
        }
    };

    /**
     * Enum of decision
     */
    public enum Decision {
        /**
         * PEDESTRIANS as decision
         */
        PEDESTRIANS,
        /**
         * PASSENGERS as decision
         */
        PASSENGERS
    }

    /**
     * Empty constructor
     */
    public EthicalEngine() {}

    /**
     * Constructor with given data from config
     * @param dataFromConfig data from config
     * @param isConfig is config given to the system
     */
    public EthicalEngine(ArrayList<Scenario> dataFromConfig, boolean isConfig) {
        this.scenariosFromConfig = dataFromConfig;
        this.isConfig = isConfig;
    }

    /**
     * provide a static method to return final decision
     * @param scenario a scenario
     * @return decision the decision for whom to survive
     */
    public static Decision decide(Scenario scenario) {
        Decision decision = (new EthicalEngine()).nonStaticDecide(scenario);
        return decision;
    }

    /**
     * Help messages will show when it is needed or system comes across invalid command
     */
    public static void printHelpMessage() {
        System.out.println("EthicalEngine - COMP90041 - Final Project");
        System.out.println();
        System.out.println("Usage: java EthicalEngine [arguments]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("   -c or --config      Optional: path to config file");
        System.out.println("   -h or --help        Print Help (this message) and exit");
        System.out.println("   -r or --results     Optional: path to results log file");
        System.out.println("   -i or --interactive Optional: launches interactive mode");
        System.exit(0);
    }

    /**
     * Make a decision based on a specified scenario
     * @param scenario a scenario
     * @return decision
     */
    public Decision nonStaticDecide(Scenario scenario){
        double scoreOfPassenger = 0;
        double scoreOfPedestrian = 0;

        ArrayList<Persona> passengers = scenario.getPassengers();
        for (int i = 0, passengersSize = passengers.size(); i < passengersSize; i++) {
            Persona passenger = passengers.get(i);
            scoreOfPassenger = scoreOfPassenger + scoreOfPersona(passenger);
        }
        ArrayList<Persona> pedestrians = scenario.getPedestrians();
        for (int i = 0, pedestriansSize = pedestrians.size(); i < pedestriansSize; i++) {
            Persona pedestrian = pedestrians.get(i);
            scoreOfPedestrian = scoreOfPedestrian + scoreOfPersona(pedestrian);
        }
        if (!scenario.isLegalCrossing()){
            scoreOfPedestrian *= weightOfIllegalCrossing;
        }
        Decision decision;
        if (scoreOfPassenger > scoreOfPedestrian) {
            decision = Decision.PASSENGERS;
        } else {
            decision = Decision.PEDESTRIANS;
        }
        return decision;
    }

    /**
     * score of personas for making decision
     * @param individual a persona
     * @return score of a persona
     */
    private double scoreOfPersona(Persona individual) {
        if (individual instanceof Human) {
            double totalScore;
            if (((Human) individual).isPregnant()) {
                if (individual.isYou()) totalScore =
                        weightOfProfession.get(((Human) individual).getProfession()) *
                        weightOfAgeCategory.get(((Human) individual).getAgeCategory()) *
                        weightOfBodyType.get(individual.getBodyType()) *
                        weightOfPregnant * weightOfYou;
                else totalScore =
                        weightOfProfession.get(((Human) individual).getProfession()) *
                        weightOfAgeCategory.get(((Human) individual).getAgeCategory()) *
                        weightOfBodyType.get(individual.getBodyType()) * weightOfPregnant * 1.00;
            }
            else {
                if (individual.isYou()) totalScore =
                        weightOfProfession.get(((Human) individual).getProfession()) *
                        weightOfAgeCategory.get(((Human) individual).getAgeCategory()) *
                        weightOfBodyType.get(individual.getBodyType()) * 1.00 * weightOfYou;
                else totalScore =
                        weightOfProfession.get(((Human) individual).getProfession()) *
                        weightOfAgeCategory.get(((Human) individual).getAgeCategory()) *
                        weightOfBodyType.get(individual.getBodyType()) * 1.00 * 1.00;
            }
            return totalScore;
        }
        if (individual instanceof Animal) {
            if (((Animal) individual).isPet()) {
                return weightOfAnimal * weightOfPet;
            }
            double totalScore = weightOfAnimal * 1.00;
            return totalScore;
        }
        return 0;
    }

    /**
     * read welcome template and print it
     */
    public static void printWelcome() {
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(DEFAULT_WELCOME_PATH));
            String welcomeString = inputStream.readLine();
            while (true){
                if (welcomeString == null) break;
                System.out.println(welcomeString);
                welcomeString = inputStream.readLine();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * entrance of interactive mode
     * @param instanceOfAudit path for file to save (Audit)
     * @param arraylistFromConfig path for file to save (ArrayList)
     */
    private void executeAudit(Audit instanceOfAudit, ArrayList<Scenario> arraylistFromConfig) {
        instanceOfAudit.inputScenarios(arraylistFromConfig);
        instanceOfAudit.runInteractiveAudit();
        instanceOfAudit.printStatistic();
        if (isToSave) {
            instanceOfAudit.printToFile(pathOfResult);
        }
    }

    /**
     * entrance of interactive mode
     */
    public void runInteractiveScenario() {
        printWelcome();
        // ask user for consent whether system will save it to file
        boolean isConsented = false;
        while (!isConsented){
            System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
            String commandReceived = Audit.scanner.nextLine();
            switch (commandReceived) {
                case "yes":
                    isToSave = true;
                    isConsented = true;
                    break;
                case "no":
                    isToSave = false;
                    isConsented = true;
                    break;
                default:
                    System.out.print("Invalid response.");
                    break;
            }
        }
        Audit instanceOfAudit = new Audit();
        instanceOfAudit.setAuditType("User");

        int numberOfScenarios = scenariosFromConfig.size();
        if (!isConfig) {
            ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
            int i = 0;
            while (i < DEFAULT_RUNS) {
                scenariosFromConfig.add(scenarioGenerator.generate());
                i++;
            }
        }
        if (numberOfScenarios >= 4) {
            for (int i = 0; i < numberOfScenarios / 3; i++) {
                ArrayList<Scenario> scenarios;
                scenarios = new ArrayList<>(scenariosFromConfig.subList(3 * i, 3 * i + 3));
                executeAudit(instanceOfAudit, scenarios);
                System.out.println("Would you like to continue? (yes/no)");
                String inputCommand = Audit.scanner.nextLine();
                if (!"yes".equals(inputCommand)){
                    break;
                }
            }
            if (numberOfScenarios % 3 > 0) {
                ArrayList<Scenario> scenarioFragment;
                scenarioFragment = new ArrayList<>(scenariosFromConfig.subList(3 * (numberOfScenarios / 3),
                        3 * (numberOfScenarios / 3) + numberOfScenarios % 3));
                executeAudit(instanceOfAudit, scenarioFragment);
            }
        } else {
            if (!isConfig) {
                boolean doUserContinue = true;
                while (doUserContinue){
                    scenariosFromConfig = new ArrayList<>();
                    ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
                    int i = 0;
                    while (i < DEFAULT_RUNS) {
                        scenariosFromConfig.add(scenarioGenerator.generate());
                        i++;
                    }
                    executeAudit(instanceOfAudit, scenariosFromConfig);
                    System.out.print("Would you like to continue? (yes/no)");
                    String choiceOfUser = Audit.scanner.nextLine();
                    if (!"yes".equals(choiceOfUser)){
                        doUserContinue = false;
                    }
                }
            } else {
                executeAudit(instanceOfAudit, scenariosFromConfig);
            }
        }
        System.out.println("That's all. Press Enter to quit.");
        while(true){
            try {
                if(System.in.read() == '\n')
                    System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setter to set the path of result
     * @param pathOfResult path for file to save (String)
     */
    public void setPathOfResult(String pathOfResult) {
        this.pathOfResult = pathOfResult;
    }

    /**
     * Getter to get the path of result
     * @return path for file to save (String)
     */
    public String getPathOfResult() {
        return pathOfResult;
    }

    /**
     * The main method for moral machine
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        String outputFile = DEFAULT_LOG_PATH;
        String combinedArguments = String.join(" ",args);
        String[] commandlineArgs = (" "+ combinedArguments).split(" -");
        boolean flagOfInteractiveMode = false;
        boolean flagOfConfigMode = false;
        ArrayList<Scenario> configBuffer = new ArrayList<>();
        for (int i = 0, commandlineArgsLength = commandlineArgs.length; i < commandlineArgsLength; i++) {
            String commandString = commandlineArgs[i];
            String[] command = commandString.split(" ");
            try {
                if ("c".equals(command[0]) || "-config".equals(command[0])) {
                    // Optional: path to config file
                    if (command.length >= 2) {
                        ArrayList<String[]> configToInput = new ArrayList<>();
                        try {
                            BufferedReader inputStream = new BufferedReader(new FileReader(command[1]));
                            String lineFromFile = inputStream.readLine();
                            int lineSerialNumber = 1;
                            if (lineFromFile != null) {
                                do {
                                    if (lineSerialNumber != 1) {
                                        configToInput.add(lineFromFile.split(",", -1));
                                    }
                                    lineSerialNumber++;
                                    lineFromFile = inputStream.readLine();
                                } while (lineFromFile != null);
                            }
                            inputStream.close();
                            ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
                            configBuffer = scenarioGenerator.scenariosFromConfig(configToInput);
                            flagOfConfigMode = true;
                        } catch (FileNotFoundException e) {
                            System.out.println("ERROR: could not find config file.");
                            System.exit(0);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        printHelpMessage();
                    }
                    // Print Help (this message) and exit
                } else if ("h".equals(command[0]) || "-help".equals(command[0])) {
                    printHelpMessage();
                    // Optional: launches interactive mode
                } else if ("i".equals(command[0]) || "-interactive".equals(command[0])) {
                    flagOfInteractiveMode = true;
                    outputFile = "user.log";
                    // Optional: path to results log file
                } else if ("r".equals(command[0]) || "-results".equals(command[0])) {
                    if (command.length < 2) {
                        printHelpMessage();
                    }
                    try {
                        File file = new File(command[1]);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        new FileReader(command[1]);
                    } catch (IOException e) {
                        System.out.println("ERROR: could not print results. Target directory does not exist.");
                        System.exit(0);
                    }
                    outputFile = command[1];
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }
        // entrance of interactive mode
        if (flagOfInteractiveMode) {
            EthicalEngine interactiveScenarios = new EthicalEngine(configBuffer, flagOfConfigMode);
            interactiveScenarios.setPathOfResult(outputFile);
            interactiveScenarios.runInteractiveScenario();
        }
        // entrance of automatic mode
        else {
            boolean doUserContinue = true;
            Audit instanceOfAudit = new Audit();
            instanceOfAudit.setAuditType("Algorithm");
            if (flagOfConfigMode) {
                instanceOfAudit = new Audit(configBuffer);
                instanceOfAudit.run();
                instanceOfAudit.printToFile(outputFile);
                instanceOfAudit.printStatistic();
            }
            else {
                while (doUserContinue) {
                    printWelcome();
                    System.out.println();
                    System.out.println("How many runs do you want?");
                    instanceOfAudit.run(Integer.parseInt(Audit.scanner.nextLine()));
                    instanceOfAudit.printToFile(outputFile);
                    instanceOfAudit.printStatistic();
                    System.out.println("Would you like to continue? (yes/no)");
                    if (!"yes".equals(Audit.scanner.nextLine())){
                        doUserContinue = false;
                    }
                }
            }
        }
    }
}

