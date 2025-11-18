package org.project.hackathonteamgenerator.controller;


import org.project.hackathonteamgenerator.model.Participant;
import org.project.hackathonteamgenerator.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/generateTeams/{teamSize}")
    public List<List<String>> generateTeams(@PathVariable int teamSize, @RequestBody List<String> roleOrder) {
        List<Participant> participants = new ArrayList<>();

        participantRepository.findAll().iterator().forEachRemaining(participants::add);

        List<String> team = new ArrayList<>();
        List<List<String>> teamList = new ArrayList<>();

        //find remaining size team
        int remainingSize = participants.size() % teamSize;
        int roleOrderIndex = 0;

        //while there is enough participants
        while(participants.size() > remainingSize) {

            //sort by role
            int finalCurrentRoleOrder = roleOrderIndex;
            List<Participant> participantsByRole = participants
                    .stream()
                    .filter(participant -> participant.getRole().equals(roleOrder.get(finalCurrentRoleOrder)))
                    .toList();

            //update roleOrder
            roleOrderIndex++;
            if(roleOrderIndex == roleOrder.size()){
                roleOrderIndex = 0;
            }

            //if no participants with role skip
            if(participantsByRole.isEmpty())
                continue;

            //find random
            Random rand = new Random();
            Participant randomElement = participantsByRole.get(rand.nextInt(participantsByRole.size()));

            //add to team
            team.add(randomElement.getTag());

            //retire
            participants.remove(randomElement);

            //add team to team list
            if(team.size() == teamSize){
                teamList.add(team);
                team = new ArrayList<>();

                //reset order
                roleOrderIndex = 0;
            }

        }

        //add remaining participant
        teamList.add(participants.stream().map(Participant::getTag).toList());

        return teamList;
    }
}
