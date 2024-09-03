package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class VotingPolesRepository {
    private static final Logger logger = LoggerFactory.getLogger(VotingPolesRepository.class);
    private final MongoCollection<Document> collection;

    public VotingPolesRepository(MongoClient mongoClient, String dbName) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        this.collection = database.getCollection("votingpoles");
    }

    public List<VotingPoles> findAll() {
        List<VotingPoles> poles = new ArrayList<>();
        for (Document doc : collection.find()) {
            poles.add(documentToVotingPole(doc));
        }
        return poles;
    }

    public VotingPoles findById(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToVotingPole(doc) : null;
    }

    public void save(VotingPoles votingPole) {
        Document doc = votingPoleToDocument(votingPole);
        if (votingPole.getId() == null) {
            logger.info("Inserting new VotingPole into database");
            collection.insertOne(doc);
            votingPole.setId(doc.getObjectId("_id").toHexString());
            logger.info("New VotingPole inserted with ID: {}", votingPole.getId());
        } else {
            logger.info("Replacing existing VotingPole with ID: {} in the database", votingPole.getId());
            collection.replaceOne(eq("_id", new ObjectId(votingPole.getId())), doc);
            logger.info("VotingPole with ID: {} replaced in the database", votingPole.getId());
        }
    }

    public void delete(String id) {
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    // New increment and save function
    public void incrementOptionCount(String id, String optionId) {
        logger.info("Starting option count increment for VotingPole ID: {} and Option ID: {}", id, optionId);

        VotingPoles votingPole = findById(id);
        if (votingPole != null) {
            logger.info("repo VotingPole ID: {} found", id);

            Optional<VotingPolesOption> option = votingPole.getOptions().stream()
                    .filter(opt -> opt.getOptionId().equals(optionId))
                    .findFirst();

            if (option.isPresent()) {
                logger.info("repo Option ID: {} found in VotingPole ID: {}", optionId, id);

                VotingPolesOption optionToUpdate = option.get();
                optionToUpdate.setOptionCount(optionToUpdate.getOptionCount() + 1);
                logger.info("repo Option count incremented for Option ID: {} in VotingPole ID: {}", optionId, id);

                save(votingPole);
                logger.info("repo VotingPole ID: {} with updated Option ID: {} saved to the database", id, optionId);
            } else {
                logger.warn("repo Option ID: {} not found in VotingPole ID: {}", optionId, id);
            }
        } else {
            logger.warn("repo VotingPole ID: {} not found", id);
        }

        logger.info("repo Completed option count increment for VotingPole ID: {} and Option ID: {}", id, optionId);
    }

    private Document votingPoleToDocument(VotingPoles votingPole) {
        List<Document> options = new ArrayList<>();
        for (VotingPolesOption option : votingPole.getOptions()) {
            options.add(new Document("optionId", option.getOptionId())
                    .append("optionName", option.getOptionName())
                    .append("optionCount", option.getOptionCount()));
        }
        return new Document("name", votingPole.getName())
                .append("description", votingPole.getDescription())
                .append("optionNum", votingPole.getOptionNum())
                .append("options", options);
    }

    private VotingPoles documentToVotingPole(Document doc) {
        VotingPoles votingPole = new VotingPoles();
        votingPole.setId(doc.getObjectId("_id").toHexString());
        votingPole.setName(doc.getString("name"));
        votingPole.setDescription(doc.getString("description"));
        votingPole.setOptionNum(doc.getInteger("optionNum"));

        List<VotingPolesOption> options = new ArrayList<>();
        List<Document> optionsDoc = (List<Document>) doc.get("options");
        for (Document optionDoc : optionsDoc) {
            VotingPolesOption option = new VotingPolesOption();
            option.setOptionId(optionDoc.getString("optionId"));
            option.setOptionName(optionDoc.getString("optionName"));
            option.setOptionCount(optionDoc.getInteger("optionCount"));
            options.add(option);
        }
        votingPole.setOptions(options);

        return votingPole;
    }
}
