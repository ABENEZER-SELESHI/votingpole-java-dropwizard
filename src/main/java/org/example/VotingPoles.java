package org.example;

import java.util.List;

public class VotingPoles {
    private String id;
    private String name;
    private String description;
    private int optionNum;
    private List<VotingPolesOption> options;

    // Getters and setters
    public VotingPolesOption getOptionById(String optionId) {
        return options.stream()
                .filter(option -> option.getOptionId().equals(optionId))
                .findFirst()
                .orElse(null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
    }

    public List<VotingPolesOption> getOptions() {
        return options;
    }

    public void setOptions(List<VotingPolesOption> options) {
        this.options = options;
    }
}
