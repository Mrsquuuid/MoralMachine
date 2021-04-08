package ethicalengine;

/**
 * Description
 * This class represents animals in the scenarios.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class Animal extends Persona {
    /**
     * This list is used for random generation of species of animals.
     */
    enum Species {
        /**
         * cat as specie
         */
        cat,
        /**
         * dog as specie
         */
        dog,
        /**
         * monkey as specie
         */
        monkey,
        /**
         * pig as specie
         */
        pig,
        /**
         * duck as specie
         */
        duck,
        /**
         * chicken as specie
         */
        chicken,
        /**
         * horse as specie
         */
        horse}

    private String species;
    private boolean isPet;

    /**
     * Empty constructor
     */
    public Animal() {}

    /**
     * Constructor to create a new instance of Animal
     * @param species species of animal (String)
     */
    public Animal(String species){
        this.setSpecies(species);
    }

    /**
     * Copy constructor to create new instance of Class Animal
     * @param otherAnimal Other Animal to copy (Animal)
     */
    public Animal(Animal otherAnimal){
        super(otherAnimal);
        if (otherAnimal != null){
            this.species = otherAnimal.species;
            this.isPet = otherAnimal.isPet;
        }
    }

    /**
     * Default Constructor
     * @param age age of animal (int)
     * @param species species of animal (String)
     * @param gender gender of animal (Gender)
     * @param bodyType bodytype of animal (BodyType)
     * @param isPet is the animal pet? (boolean)
     */
    public Animal(int age, String species, Gender gender, BodyType bodyType, boolean isPet){
        super(age, gender, bodyType);
        this.setSpecies(species);
        this.setPet(isPet);
    }

    /**
     * setter method to set species of animal
     * @param species species of animal (String)
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * getter method to get species of animal
     * @return species of animal (String)
     */
    public String getSpecies() {
        return species;
    }

    /**
     * getter method to find is the animal a pet?
     * @return is pet? (String)
     */
    public boolean isPet() {
        return isPet;
    }

    /**
     * getter method to set the animal as a pet or not
     * @param isPet is pet? (boolean)
     */
    public void setPet(boolean isPet) {
        this.isPet = isPet;
    }

    /**
     * override toString method
     * @return a string representing the animal (String)
     */
    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append(species.toLowerCase());
        if (isPet()) string.append(" is pet");
        return string.toString();
    }

    /**
     * is the animal you?
     * @return keep false (boolean)
     */
    @Override
    public boolean isYou() {
        return false;
    }
}

