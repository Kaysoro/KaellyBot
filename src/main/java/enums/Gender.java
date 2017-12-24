package enums;

public enum Gender {
    MALE("mg"), FEMALE("fg"), ASEXUAL("ng");

    private String definition;

    Gender(String definition){
        this.definition = definition;
    }

    public String getDefinition(){
        return definition;
    }
}
