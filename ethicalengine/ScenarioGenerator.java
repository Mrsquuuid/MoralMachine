package ethicalengine;

import java.util.ArrayList;
import java.util.Random;

import static ethicalengine.Animal.*;
import static ethicalengine.Human.Profession;
import static java.lang.Boolean.parseBoolean;

/**
 * Description
 * This class is the basis of simulations and is be used to create a variety of scenarios.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class ScenarioGenerator {

    public final static int DEFAULT_MIN = 1;
    public final static int DEFAULT_MAX = 5;
    public final static int DEFAULT_SEED = 1;
    public final static int DEFAULT_NUMBER_OF_VALUE = 10;
    private final Random randomNumber = new Random();
    private int passengerMaxCount = DEFAULT_MAX;
    private int passengerMinCount = DEFAULT_MIN;
    private int pedestrianMaxCount = DEFAULT_MAX;
    private int pedestrianMinCount = DEFAULT_MIN;

    Gender[] genders = Gender.values();
    Profession[] professions = Profession.values();
    BodyType[] bodyTypes = BodyType.values();
    Species[] species = Species.values();

    /**
     * Empty constructor to set the seed to a truly random number
     */
    public ScenarioGenerator() {
        randomNumber.setSeed(randomNumber.nextLong());
    }

    /**
     * Constructor which sets the seed with a predefined value
     * @param seed seed for random generation
     */
    public ScenarioGenerator(long seed) {
        randomNumber.setSeed(seed);
    }

    /**
     * Constructor which sets the seed as well as the minimum and maximum number for
     * both passengers and pedestrians with predefined values
     * @param seed seed for random generation (long)
     * @param passengerMaxCount maximum number of passengers (int)
     * @param passengerMinCount minimum number of passengers (int)
     * @param pedestrianMaxCount maximum number of pedestrians (int)
     * @param pedestrianMinCount minimum number of pedestrians (int)
     * @throws NumberFormatException when input of value boundary is invalid
     */
    public ScenarioGenerator(long seed, int passengerMinCount, int passengerMaxCount,
                             int pedestrianMinCount, int pedestrianMaxCount) {
        randomNumber.setSeed(seed);
        if (passengerMaxCount < passengerMinCount || pedestrianMaxCount < pedestrianMinCount) {
            throw new NumberFormatException("Input of value boundary is invalid");
        } else {
            setPassengerCountMax(passengerMaxCount);
            setPassengerCountMin(passengerMinCount);
            setPedestrianCountMax(pedestrianMaxCount);
            setPedestrianCountMin(pedestrianMinCount);
        }
    }

    /**
     * Setter to set the minimum number of car passenger for each scenario
     * @param passengerMinCount maximum number of passenger (int)
     */
    public void setPassengerCountMin(int passengerMinCount) {
        this.passengerMinCount = passengerMinCount;
    }

    /**
     * Setter to set the maximum number of car passengers for each scenario
     * @param passengerMaxCount minimum number of passengers (int)
     */
    public void setPassengerCountMax(int passengerMaxCount) {
        this.passengerMaxCount = passengerMaxCount;
    }

    /**
     * Setter to set the minimum number of pedestrians for each scenario
     * @param pedestrianMinCount minimum number of pedestrians (int)
     */
    public void setPedestrianCountMin(int pedestrianMinCount) {
        this.pedestrianMinCount = pedestrianMinCount;
    }

    /**
     * Setter to set the maximum number of pedestrians for each scenario
     * @param pedestrianMaxCount maximum number of pedestrians (int)
     */
    public void setPedestrianCountMax(int pedestrianMaxCount) {
        this.pedestrianMaxCount = pedestrianMaxCount;
    }

    /**
     * Generate a random newly created instance of Animal
     * @return a random animal (Animal)
     */
    public Animal getRandomAnimal() {
        int ageOfAnimal = randomNumber.nextInt(20);
        Gender gender = genders[randomNumber.nextInt(genders.length)];
        BodyType bodyType = bodyTypes[randomNumber.nextInt(bodyTypes.length)];
        String species = this.species[randomNumber.nextInt(this.species.length)].toString();
        boolean isPet;
        if (randomNumber.nextInt(2) == 0) {
            isPet = true;
        } else {
            isPet = false;
        }
        Animal animal = new Animal(ageOfAnimal, species, gender, bodyType, isPet);
        return animal;
    }

    /**
     * Generate a random newly created instance of Human
     * @return a random Human (Human)
     */
    public Human getRandomHuman() {
        int ageOfHuman = randomNumber.nextInt(100);
        boolean isPregnant;
        Profession profession;
        Gender gender = genders[randomNumber.nextInt(genders.length)];
        BodyType bodyType = bodyTypes[randomNumber.nextInt(bodyTypes.length)];
        if(ageOfHuman >= 17 && ageOfHuman <= 68){
            profession = professions[randomNumber.nextInt(professions.length-1)];
        }else{
            profession = Profession.NONE;
        }
        // According to the statistics of newborns, age Structure and the total population in recent years,
        // the occurrence probability of pregnant women was estimated.
        if ((gender == Gender.FEMALE) && (ageOfHuman >= 17) && (ageOfHuman <= 68)
                && (randomNumber.nextInt(32) == 16)) {
            isPregnant = true;
        } else {
            isPregnant = false;
        }
        Human human = new Human(ageOfHuman, profession, gender, bodyType, isPregnant);
        return human;
    }

    /**
     * Generate a random scenario
     * @return a random scenario (Scenario)
     */
    public Scenario generate() {
        int passengerCount =
                randomNumber.nextInt(passengerMaxCount - passengerMinCount +1) + passengerMinCount;
        int pedestrianCount =
                randomNumber.nextInt(pedestrianMaxCount - pedestrianMinCount +1) + pedestrianMinCount;
        Human you = getRandomHuman();
        you.setAsYou(true);
        // Divide the persona by positions into three flags:
        // 0 represents null, 1 represents passenger, 2 represents pedestrian
        int whereAreYou = randomNumber.nextInt(3);
        boolean isLegalCrossing = randomNumber.nextInt(2) == 0;
        ArrayList<Persona> passengerList = new ArrayList<>();
        ArrayList<Persona> pedestrianList = new ArrayList<>();
        if (whereAreYou == 1) {
            passengerList.add(you);
            passengerCount--;
        } else if (whereAreYou == 2) {
            pedestrianList.add(you);
            pedestrianCount--;
        }
        // randomly add passengers into the scenario
        for (int i = 0; i < passengerCount; i++) {
            if (randomNumber.nextInt(2) == 0) {
                passengerList.add(getRandomHuman());
            } else passengerList.add(getRandomAnimal());
        }
        // randomly add pedestrians into the scenario
        for (int j = 0; j < pedestrianCount; j++) {
            if (randomNumber.nextInt(2) == 0) {
                pedestrianList.add(getRandomHuman());
            } else pedestrianList.add(getRandomAnimal());
        }
        Scenario scenario = new Scenario(passengerList, pedestrianList, isLegalCrossing);
        return scenario;
    }

    /**
     * initialize a scenario from config file
     * @param dataFromConfig string of configs
     * @return scenario from config (ArrayList)
     */
    public ArrayList<Scenario> scenariosFromConfig(ArrayList<String[]> dataFromConfig) {
        ArrayList<Persona> passengers = new ArrayList<>();
        ArrayList<Persona> pedestrians = new ArrayList<>();
        ArrayList<Scenario> scenarios = new ArrayList<>();
        int lineSerialNumber = 1;
        boolean isSignalGreen = true;
        for (int i = 0, dataFromConfigSize = dataFromConfig.size(); i < dataFromConfigSize; i++) {
            String[] lineNumber = dataFromConfig.get(i);
            lineSerialNumber++;
            try {
                if (lineNumber.length != DEFAULT_NUMBER_OF_VALUE) {
                    throw new InvalidDataFormatException(
                            "WARNING: invalid data format in config file in lineNumber " + lineSerialNumber);
                }
                if ("human".equals(lineNumber[0].toLowerCase())) {
                    try {
                        Human human = new Human();
                        human.setGender(lineNumber[1], lineSerialNumber);
                        human.setAge(lineNumber[2], lineSerialNumber);
                        human.setBodyType(lineNumber[3], lineSerialNumber);
                        human.setProfession(lineNumber[4], lineSerialNumber);
                        human.setPregnant(parseBoolean(lineNumber[5]));
                        human.setAsYou(parseBoolean(lineNumber[6]));
                        if ("passenger".equals(lineNumber[9])) {
                            passengers.add(human);
                            human.setRole(Role.PASSENGER);
                        } else {
                            pedestrians.add(human);
                            human.setRole(Role.PEDESTRIAN);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("WARNING: invalid number format in config file in lineNumber " 
                                + lineSerialNumber);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                } else if ("animal".equals(lineNumber[0].toLowerCase())) {
                    try {
                        Animal animal = new Animal();
                        animal.setGender(lineNumber[1], lineSerialNumber);
                        animal.setAge(lineNumber[2], lineSerialNumber);
                        animal.setBodyType(lineNumber[3], lineSerialNumber);
                        animal.setPet(parseBoolean(lineNumber[8]));
                        if (!lineNumber[7].equals("")) {
                            animal.setSpecies(lineNumber[7]);
                        } else {
                            animal.setSpecies("cat");
                        }
                        if ("passenger".equals(lineNumber[9])) {
                            passengers.add(animal);
                            animal.setRole(Role.PASSENGER);
                        } else {
                            pedestrians.add(animal);
                            animal.setRole(Role.PEDESTRIAN);
                        }
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                } else if ("scenario:green".equals(lineNumber[0].toLowerCase()) ||
                        "scenario:red".equals(lineNumber[0].toLowerCase())) {
                    if (lineSerialNumber != 2) {
                        Scenario scenario = new Scenario(passengers, pedestrians, isSignalGreen);
                        scenarios.add(scenario);
                        passengers = new ArrayList<>();
                        pedestrians = new ArrayList<>();
                    }
                    String string = lineNumber[0];
                    string.toLowerCase();
                    isSignalGreen = string.equals("scenario:green");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        Scenario scenario = new Scenario(passengers, pedestrians, isSignalGreen);
        scenarios.add(scenario);
        return scenarios;
    }
}
