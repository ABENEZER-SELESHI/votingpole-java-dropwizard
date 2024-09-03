package org.example;

public class VotingPolesOption {
    private String optionId;
    private String optionName;
    private int optionCount;

    // Getters and setters
    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String id) {
        this.optionId = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }
}