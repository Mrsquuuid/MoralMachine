package ethicalengine;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Description
 * This class contains all relevant information about a presented scenario.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class Scenario {
    private ArrayList<Persona> passengers;
    private ArrayList<Persona> pedestrians;
    private boolean isLegalCrossing;
    private boolean hasYouInCar;

    /**
     * Constructor if the input is array
     * @param passengers an Array containing passengers (Persona[])
     * @param pedestrians an Array containing pedestrians (Persona[])
     * @param isLegalCrossing legal crossing status which means crossing at green light (boolean)
     */
    public Scenario(Persona[] passengers, Persona[] pedestrians, boolean isLegalCrossing){
        this.passengers = new ArrayList<>(Arrays.asList(passengers));
        this.pedestrians = new ArrayList<>(Arrays.asList(pedestrians));
        setLegalCrossing(isLegalCrossing);
    }

    /**
     * Constructor if the input is arraylist
     * @param passengers an ArrayList containing passengers (ArrayList)
     * @param pedestrians an ArrayList containing pedestrians (ArrayList)
     * @param isLegalCrossing legal crossing status which means crossing at green light (boolean)
     */
    public Scenario(ArrayList<Persona> passengers, ArrayList<Persona> pedestrians, boolean isLegalCrossing){
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        setLegalCrossing(isLegalCrossing);
    }

    /**
     * Getter to get passengers
     * @return passengers (ArrayList)
     */
    public ArrayList<Persona> getPassengers() {
        return passengers;
    }

    /**
     * Getter to get pedestrians
     * @return pedestrians (ArrayList)
     */
    public ArrayList<Persona> getPedestrians() {
        return pedestrians;
    }

    /**
     * Getter to get pedestrians
     * @return pedestrians (ArrayList)
     */
    public Persona[] getPedestriansArray() {
        Persona[] personas = pedestrians.toArray(new Persona[0]);
        return personas;
    }

    /**
     * Getter to get pedestrians
     * @return pedestrians (Persona)
     */
    public Persona[] getPassengersArray() {
        Persona[] personas = passengers.toArray(new Persona[0]);
        return personas;
    }

    /**
     * Getter to know whether you are in lane
     * @return Is you in lane (boolean)
     */
    public boolean hasYouInLane() {
        for (Persona persona: getPedestrians()) {
            if (persona.isYou()){
                return true;
            }
        }
        return false;
    }

    /**
     * Getter to know whether you are in car
     * @return Is you in car (boolean)
     */
    public boolean hasYouInCar() {
        for (Persona persona: getPassengers()) {
            if (persona.isYou()){
                return true;
            }
        }
        return false;
    }

    /**
     * Getter to know whether crossing is legal
     * @return Is legal crossing (boolean)
     */
    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

    /**
     * Setter to set legal Crossing status
     * @param isLegalCrossing Is legal crossing (boolean)
     */
    public void setLegalCrossing(boolean isLegalCrossing) {
        this.isLegalCrossing = isLegalCrossing;
    }

    /**
     * Getter to get the number of passengers in the car
     * @return number of passenger (int)
     */
    public int getPassengerCount() {
        return getPassengers().size();
    }

    /**
     * Getter to get the number of pedestrians on the street
     * @return number of pedestrian (int)
     */
    public int getPedestrianCount() {
        return getPedestrians().size();
    }

    /**
     * override toString method to return scenario in string
     * @return String representing the scenario (String)
     */
    public String toString() {
        StringBuilder scenarioString = new StringBuilder();
        scenarioString.append("======================================\n" +
                "# Scenario\n======================================\n");
        scenarioString.append("Legal Crossing: ");
        if (isLegalCrossing) {
            scenarioString.append("yes\n");
        } else {
            scenarioString.append("no\n");
        }
        scenarioString.append("Passengers (").append(getPassengerCount()).append(")\n");
        for (int i = 0, passengersSize = passengers.size(); i < passengersSize; i++) {
            Persona persona = passengers.get(i);
            scenarioString.append("- ").append(persona.toString()).append("\n");
        }
        scenarioString.append("Pedestrians (").append(getPedestrianCount()).append(")\n");
        for (int i = 0, pedestriansSize = pedestrians.size(); i < pedestriansSize; i++) {
            Persona persona = pedestrians.get(i);
            scenarioString.append("- ").append(persona.toString()).append("\n");
        }
        scenarioString.deleteCharAt(scenarioString.lastIndexOf("\n"));
        return scenarioString.toString();
    }
}
