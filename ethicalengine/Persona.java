package ethicalengine;

/**
 * Description
 * This class is an Abstract Class from which all Persona types inherit.
 * yuzyou@student.unimelb.edu.au
 * No.1159774
 *
 * @author Yuzhe You
 */

public abstract class Persona {
    /**
     * This list is used for random generation of gender of persona.
     */
    public enum Gender {
        /**
         * MALE as gender
         */
        MALE,
        /**
         * FEMALE as gender
         */
        FEMALE,
        /**
         * UNKNOWN as gender
         */
        UNKNOWN}

    /**
     * This list is used for random generation of body type of persona.
     */
    public enum BodyType {
        /**
         * AVERAGE as body type
         */
        AVERAGE,
        /**
         * ATHLETIC as body type
         */
        ATHLETIC,
        /**
         * OVERWEIGHT as body type
         */
        OVERWEIGHT,
        /**
         * UNSPECIFIED as body type
         */
        UNSPECIFIED}

    /**
     * This list is used for random generation of role of persona.
     */
    public enum Role {
        /**
         * PASSENGER as role
         */
        PASSENGER ,
        /**
         * PEDESTRIAN as role
         */
        PEDESTRIAN,
        /**
         * UNSPECIFIED as role
         */
        UNSPECIFIED}

    private int age;
    private Gender gender = Gender.UNKNOWN;
    private BodyType bodyType = BodyType.UNSPECIFIED;
    private Role role = Role.UNSPECIFIED;

    /**
     * Empty constructor
     */
    public Persona() {}

    /**
     * Default constructor
     * @param age age of a persona (int)
     * @param gender gender of a persona (Gender)
     * @param bodyType body type of a persona (BodyType)
     */
    public Persona(int age, Gender gender, BodyType bodyType) {
            this.setAge(age);
            this.setGender(gender);
            this.setBodyType(bodyType);
    }

    /**
     * Copy constructor
     * @param otherPersona another persona (Persona)
     */
    public Persona(Persona otherPersona) {
        if(otherPersona != null){
            this.age = otherPersona.age;
            this.gender = otherPersona.gender;
            this.bodyType = otherPersona.bodyType;
        }
    }

    /**
     * Get the age of a persona
     * @return age of a persona (int)
     */
    public int getAge(){
        return this.age;
    }

    /**
     * Set the age of a persona
     * @param age age of a persona (int)
     * @throws NumberFormatException when the input number of age is invalid
     */
    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
        else{
            throw new NumberFormatException("Invalid input: the age of a persona should not be negative");
        }
    }

    /**
     * Set age of a persona when it is config mode
     * @param age age of persona (int)
     * @param lineSerialNumber serial number of the line in scenarios (String)
     * @throws NumberFormatException when the input number of age is invalid
     * @see ScenarioGenerator
     */
    public void setAge(String age, int lineSerialNumber){
        try {
            setAge(Integer.parseInt(age));
        }
        catch (NumberFormatException e) {
            System.out.println("WARNING: invalid number format in config file in line " + lineSerialNumber);
            setAge(28);
        }
    }

    /**
     * Get the gender of a persona
     * @return gender of a persona (Gender)
     */
    public Gender getGender(){
        return this.gender;
    }

    /**
     * Set the gender of a persona
     * @param gender gender of a persona (Gender)
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Set the gender of a persona when it is config mode
     * @param gender gender of a persona (String)
     * @param lineSerialNumber serial number of the line in scenarios (int)
     * @throws InvalidCharacteristicException when the input number of age is invalid
     * @see ScenarioGenerator
     */
    public void setGender(String gender, int lineSerialNumber) {
        if (gender.toLowerCase().equals("female")){
            this.gender = Gender.FEMALE;
        }
        else if (gender.toLowerCase().equals("male")){
            this.gender = Gender.MALE;
        }
        else {
            this.gender = Gender.UNKNOWN;
            if (!gender.equals("")){
                throw new InvalidCharacteristicException(
                        "WARNING: invalid PersonaAttribute in config file in line " + lineSerialNumber);
            }
        }
    }

    /**
     * Get the body type of a persona
     * @return body type of a persona (BodyType)
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     * Set the body type of a persona
     * @param bodyType body type of a persona (BodyType)
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    /**
     * Set the body type of a persona when it is config mode
     * @param bodyType body type of a persona (String)
     * @param lineSerialNumber serial number of the line in scenarios (int)
     * @throws InvalidCharacteristicException when the input number of age is invalid
     */
    public void setBodyType(String bodyType, int lineSerialNumber) {
        BodyType[] values = BodyType.values();
        boolean isAlreadySet = false;
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            BodyType typeOfBody = values[i];
            if (typeOfBody.name().equals(bodyType.toUpperCase())) {
                this.bodyType = typeOfBody;
                isAlreadySet = true;
            }
        }
        if (!isAlreadySet && !bodyType.equals("")){
            this.bodyType = BodyType.UNSPECIFIED;
            throw new InvalidCharacteristicException(
                    "WARNING: invalid PersonaAttribute in config file in line " + lineSerialNumber);
        }
    }

    /**
     * Get the role of a persona
     * @return role of a persona (Role)
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the role of a persona
     * @param role role of a persona (Role)
     */
    public void setRole(Role role) {
        this.role = role;
    }


    /**
     * initialize abstract toString method for overriding, which aims to print persona message
     * @return string represents a persona (String)
     * @see Human
     * @see Animal
     */
    public abstract String toString();

    /**
     * initialize abstract isYou method for overriding, which shows the persona is you or not
     * @return is you? (boolean)
     * @see Human
     * @see Animal
     */
    public abstract boolean isYou();
}


