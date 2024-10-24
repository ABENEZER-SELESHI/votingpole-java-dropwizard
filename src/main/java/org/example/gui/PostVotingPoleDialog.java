package org.example.gui;

import org.example.VotingPoles;
import org.example.VotingPolesOption;
import org.example.VotingPolesService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VotingPoleDetailsDialog extends JDialog {
    private VotingPoles votingPole;
    private VotingPolesService votingPolesService;

    public VotingPoleDetailsDialog(Frame parent, VotingPoles votingPole, VotingPolesService votingPolesService, String userId) {
        super(parent, "Voting Pole Details", true);
        this.votingPole = votingPole;
        this.votingPolesService = votingPolesService;

        setLayout(new BorderLayout());
        setSize(400, 300); // Increased size for better readability

        // Display voting pole name and description
        JTextArea detailsArea = new JTextArea();
        detailsArea.setText("Name: " + votingPole.getName() + "\n" +
                "Description: " + votingPole.getDescription() + "\n");

        // Display options and their counts
        StringBuilder optionsDetails = new StringBuilder("Options:\n");
        for (VotingPolesOption option : votingPole.getOptions()) {
            optionsDetails.append(option.getOptionName()) // Changed to getOptionName()
                    .append(" - Count: ")
                    .append(option.getOptionCount())
                    .append("\n");
        }
        detailsArea.append(optionsDetails.toString()); // Convert StringBuilder to String
        detailsArea.setEditable(false);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        // Create buttons
        JButton voteButton = new JButton("Vote for Option 1"); // Default button for demo
        voteButton.addActionListener(e -> {
            if (!votingPole.getOptions().isEmpty()) {
                VotingPolesOption selectedOption = votingPole.getOptions().get(0); // Example for the first option
                selectedOption.setOptionCount(selectedOption.getOptionCount() + 1); // Increment option count
                votingPolesService.incrementOptionCount(votingPole.getId(), selectedOption.getOptionId(), userId); // Use actual user ID
                JOptionPane.showMessageDialog(this, "Voted for: " + selectedOption.getOptionName());
                dispose(); // Close dialog after voting
            } else {
                JOptionPane.showMessageDialog(this, "No options available to vote on.");
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this voting pole?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (votingPolesService.deleteVotingPole(votingPole.getId())) {
                    JOptionPane.showMessageDialog(this, "Voting pole deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting voting pole.");
                }
                dispose(); // Close dialog
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(voteButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
