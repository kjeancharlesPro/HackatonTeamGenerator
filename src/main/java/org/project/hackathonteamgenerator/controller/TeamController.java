package org.project.hackathonteamgenerator.controller;


import org.project.hackathonteamgenerator.model.Participant;
import org.project.hackathonteamgenerator.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TeamController {

    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping("/generateTeams/{teamSize}")
    public List<List<String>> generateTeams(@PathVariable int teamSize) {
        List<Participant> participants = new ArrayList<>();

        participantRepository.findAll().iterator().forEachRemaining(participants::add);

        List<String> team = new ArrayList<>();
        List<List<String>> teamList = new ArrayList<>();

        //find remaining size team
        int remainingSize = participants.size() % teamSize;

//        List<String> roleOrder = List.of("1", "2", "3", "4", "5");
//        int currentRoleOrder = 0;

        //while the
        while(participants.size() > remainingSize) {

            //sorted by role

//            //sturcture exemple 1 > 2 > 3 > 4 > 5
//            int finalCurrentRoleOrder = currentRoleOrder;
//            List<Participant> participantsByRole = participants
//                    .stream()
//                    .filter(participant -> participant.getRole().equals(roleOrder.get(finalCurrentRoleOrder)))
//                    .toList();

            //update roleOrder
//            currentRoleOrder++;
//            if(currentRoleOrder == roleOrder.size()){
//                currentRoleOrder = 0;
//            }

            //find random
            Random rand = new Random();
            Participant randomElement = participants.get(rand.nextInt(participants.size()));

            //add to team
            team.add(randomElement.getTag());

            //retire
            participants.remove(randomElement);

            //add team to team list
            if(team.size() == teamSize){
                teamList.add(team);
                team = new ArrayList<>();

                //reset order
                //currentRoleOrder = 0;
            }

        }

        //add remaining participant
        teamList.add(participants.stream().map(Participant::getTag).toList());

        return teamList;
    }
}
