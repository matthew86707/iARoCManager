/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jointheleague.iaroc.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.jointheleague.iaroc.entities.Team;
import org.jointheleague.iaroc.entities.TeamMember;

/**
 *
 * @author ian
 */
@Singleton
@Startup
public class ConfigBean {

    @EJB
    private RequestBean request;

    @PostConstruct
    public void createData() {
        Team pirateTeam = new Team("The Pirates", "cannon.jpg");
        request.addOrUpdateTeam(pirateTeam);
        Team ninjaTeam = new Team("The Ninjas", "shuriken.gif");
        request.addOrUpdateTeam(ninjaTeam);
        Team robotTeam = new Team("The Robots", "1011101.gif");
        request.addOrUpdateTeam(robotTeam);
        Team zombieTeam = new Team("The Zombies", "brains.gif");
        request.addOrUpdateTeam(zombieTeam);
        Team doggoTeam = new Team("The Doggos", "adorableDogPic.jpg");
        request.addOrUpdateTeam(doggoTeam);
        
        request.addOrUpdateTeamMember(new TeamMember(1, pirateTeam, "Capn", "Jack", "capnJack@gmail.com"));
        request.addOrUpdateTeamMember(new TeamMember(2, pirateTeam, "Capn", "Crunch", "capnCrunch@yahoo.com"));
        request.addOrUpdateTeamMember(new TeamMember(3, ninjaTeam, "White", "Shadow", "obviouslyfakeemail@lol.org"));
        request.addOrUpdateTeamMember(new TeamMember(4, robotTeam, "1011", "111001", "111011001@aol.com"));
        request.addOrUpdateTeamMember(new TeamMember(5, zombieTeam, "Brains", "McBrains", "mmmBrains@theHorde.gov"));
        request.addOrUpdateTeamMember(new TeamMember(6, doggoTeam, "Twilight", "Sparkle", "pupper@omgdogs.com"));
    }

    @PreDestroy
    public void deleteData() {
        request.getAllTeams().forEach((team) -> {
            request.removeTeam(team.getId());
        });
        
        request.getAllTeamMembers().forEach((teamMember) -> {
            request.removeTeamMember(teamMember.getId());
        });
    }
}
