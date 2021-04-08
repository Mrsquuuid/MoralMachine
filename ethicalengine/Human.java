package ethicalengine;

import java.util.Objects;

/**
 * Description
 * This class represents humans in the scenarios.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public class Human extends Persona {
    /**
     * This list is used for random generation of profession of human.
     */
    public enum Profession {
        /**
         * DOCTOR as profession
         */
        DOCTOR,
        /**
         * CEO as profession
         */
        CEO,
        /**
         * CRIMINAL as profession
         */
        CRIMINAL,
        /**
         * HOMELESS as profession
         */
        HOMELESS,
        /**
         * PROFESSOR as profession
         */
        PROFESSOR,
        /**
         * UNEMPLOYED as profession
         */
        UNEMPLOYED,
        /**
         * ENGINEER as profession
         */
        ENGINEER,           // newly add
        /**
         * TEACHER as profession
         */
        TEACHER,            // newly add
        /**
         * WORKER as profession
         */
        WORKER,             // newly add
        /**
         * NONE as profession
         */
        NONE
    }

    /**
     * This list is used for random generation of age category of human.
     */
    public enum AgeCategory {
        /**
         * BABY as age category
         */
        BABY,
        /**
         * CHILD as age category
         */
        CHILD,
        /**
         * ADULT as age category
         */
        ADULT,
        /**
         * SENIOR as age category
         */
        SENIOR
    }

    private boolean isPregnant;
    private boolean isYou;
    private Profession profession = Profession.NONE;

    /**
     * Empty constructor
     */
    public Human() {}

    /**
     * Copy constructor
     * @param otherHuman a human (Human)
     */
    public Human(Human otherHuman) {
        super(otherHuman);
        if (otherHuman != null){
            this.profession = otherHuman.profession;
            this.isPregnant = otherHuman.isPregnant;
            this.isYou = otherHuman.isYou;
        }
    }

    /**
     * Default constructor
     * @param age age of a human (int)
     * @param profession profession of a human (Profession)
     * @param gender gender of a human (Gender)
     * @param bodyType body type of a human (BodyType)
     * @param isPregnant is the human pregnant (boolean)
     */
    public Human(int age, Profession profession, Gender gender,
                 BodyType bodyType, boolean isPregnant) {
        super(age, gender, bodyType);
        this.setProfession(profession);
        this.setPregnant(isPregnant);
    }

    /**
     * Default constructor to initialize a null human
     * @param age age of a human (int)
     * @param gender gender of a human (Gender)
     * @param bodyType body type of a human (BodyType)
     */
    public Human(int age, Gender gender, BodyType bodyType) {
        super(age, gender, bodyType);
        this.setProfession(Profession.NONE);
        this.setPregnant(false);
    }

    /**
     * Get the profession of a Human
     * @return profession of a Human (Profession)
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * Set profession of a Human
     * @param profession profession of Human (Profession)
     */
    public void setProfession(Profession profession) {
        if (getAgeCategory() == AgeCategory.ADULT){
            this.profession = profession;
        }
        else {
            this.profession = Profession.NONE;
        }
    }

    /**
     * Set the profession of a Human when it is config mode
     * @param profession profession of a Human (String)
     * @param lineSerialNumber line count of parsing scenario (int)
     * @throws InvalidCharacteristicException when invalid input received
     * @see ScenarioGenerator
     */
    public void setProfession(String profession, int lineSerialNumber) {
        boolean isProfessionSpecified = false;
        Profession[] values = Profession.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            Profession professionOfHuman = values[i];
            if (Objects.equals(professionOfHuman.name(), profession.toUpperCase())) {
                setProfession(professionOfHuman);
                isProfessionSpecified = true;
            }
        }
        if (!isProfessionSpecified && !profession.equals("")){
            setProfession(Profession.NONE);
            throw new InvalidCharacteristicException(
                    "WARNING: invalid Persona in config file in line " + lineSerialNumber);
        }
    }

    /**
     * Getter method to know whether the Human is pregnant
     * @return is pregnant? (boolean)
     */
    public boolean isPregnant() {
        return isPregnant;
    }

    /**
     * Set the Human as pregnant or not
     * @param isPregnant is pregnant? (boolean)
     */
    public void setPregnant(boolean isPregnant) {
        if (getGender() == Gender.FEMALE) {
            this.isPregnant = isPregnant;
        }
        else {
            this.isPregnant = false;
        }
    }

    /**
     * Get the Human's age category
     * @return age category of a Human (AgeCategory)
     */
    public AgeCategory getAgeCategory() {
        if (getAge() >= 0 && getAge() <= 4) {
            return AgeCategory.BABY;
        }
        else if (getAge() >= 5 &&getAge() <= 16) {
            return AgeCategory.CHILD;
        }
        else if (getAge() >= 17 && getAge() <= 68) {
            return AgeCategory.ADULT;
        }
        else{
            return AgeCategory.SENIOR;
        }
    }

    /**
     * Getter method to show whether the Human is you?
     * @return is you or not (boolean)
     */
    @Override
    public boolean isYou() {
        return isYou;
    }

    /**
     * Set the Human as you or not
     * @param isYou is you? (boolean)
     */
    public void setAsYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * override toString method
     * @return string represents a Human (String)
     */
    @Override
    public String toString() {
        StringBuilder humanString = new StringBuilder();
        if (isYou()) humanString.append("you ");
        humanString.append(getBodyType().name().toLowerCase()).append(" ");
        humanString.append(getAgeCategory().name().toLowerCase()).append(" ");
        switch (getAgeCategory()) {
            case ADULT:
                humanString.append(getProfession().name().toLowerCase());
                humanString.append(" ");
                break;
        }
        humanString.append(getGender().name().toLowerCase());
        if (isPregnant()) humanString.append(" pregnant");
        return humanString.toString();
    }
}
