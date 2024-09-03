package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VotingPolesService {

    private static final Logger logger = LoggerFactory.getLogger(VotingPolesService.class);
    private final VotingPolesRepository repository;

    public VotingPolesService(VotingPolesRepository repository) {
        this.repository = repository;
    }

    public List<VotingPoles> getAllVotingPoles() {
        return repository.findAll();
    }

    public VotingPoles getVotingPoleById(String id) {
        return repository.findById(id);
    }

    public void createVotingPole(VotingPoles votingPole) {
        repository.save(votingPole);
    }

    // Now delegates to the repository for incrementing option count
    public void incrementOptionCount(String id, String optionId) {
        logger.info("Delegating option count increment to repository for VotingPole ID: {} and Option ID: {}", id, optionId);
        repository.incrementOptionCount(id, optionId);
    }

    public boolean deleteVotingPole(String id) {
        VotingPoles votingPole = repository.findById(id);
        if (votingPole != null) {
            repository.delete(id);
            return true;
        } else {
            logger.warn("VotingPole ID: {} not found", id);
            return false;
        }
    }
}
