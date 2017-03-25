/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jointheleague.iaroc.entities;

import java.util.List;
import org.jointheleague.iaroc.ejb.RequestBean;
import org.jointheleague.iaroc.managers.TeamManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patri_000
 */
public class TeamTest {
    
    RequestBean request;
    TeamManager mgr;

    public TeamTest() {
        request = RequestBean.getTestBean();
        mgr = new TeamManager(request);
    }

    Team pirateTeam = new Team("The Pirates", "cannon.jpg");
    Team ninjaTeam = new Team("The Ninjas", "shuriken.gif");
    Team robotTeam = new Team("The Robots", "1011101.gif");
    Team zombieTeam = new Team("The Zombies", "brains.gif");
    Team doggoTeam = new Team("The Doggos", "adorableDogPic.jpg");

    @Before
    public void setUp() {
        request.clearDatabase();
    }

    @After
    public void tearDown() {
        request.clearDatabase();
    }

    @Test
    public void testCreateTeam() {

        int numExceptions = 0;
        try {

            //Create using no-args constructor
            Team team = new Team();

            assertEquals("", team.getName());
            assertEquals("", team.getIconURL());
            assertEquals(null, team.getId());

            team = new Team("team", "team.jpg");
            assertNotNull(team);

            //Now try creating a team that fails name validation via being too long.
            try {
                team = new Team(String.format("%1$-26"), "team.jpg");
            } catch (Exception e) {
                numExceptions++;
            }
            assertEquals(1, numExceptions);

        } catch (Exception e) {
            org.junit.Assert.fail("Received an exception.");
        }

    }

    @Test
    /**
     * Test insertion of teams to entity manager.
     */
    public void testInsertTeam() {
        request.addOrUpdateTeam(pirateTeam);
        
        request.getEntityManager().flush();
        
        List<Team> allTeams = (List<Team>) request.getEntityManager().
                createNamedQuery("findAllTeams").getResultList();
        
        assertEquals(1, allTeams.size());

        TeamMember capnJack = new TeamMember(pirateTeam, "Capn", "Jack", "capnJack@gmail.com");
        TeamMember capnCrunch = new TeamMember(pirateTeam, "Capn", "Crunch", "capnCrunch@yahoo.com");
        request.addOrUpdateTeamMember(capnJack);
        request.addOrUpdateTeamMember(capnCrunch);

        Team teamOfCapnJack = capnJack.getTeam();

        assertEquals(pirateTeam, teamOfCapnJack);

        pirateTeam = request.findTeam(pirateTeam.getId());

        List<TeamMember> members = pirateTeam.getTeamMembers();
        assertEquals(2, members.size());

        assertEquals(pirateTeam, capnCrunch.getTeam());
        
        request.getEntityManager().flush();
        
    }

    @Test
    /**
     * Test insertion of teams to entity manager.
     */
    public void testDeleteTeam() {
        
        //Starting things off, make sure there aren't any teams that weren't cleared out.
        assertEquals(0,request.getAllTeamMembers().size());
        assertEquals(0,request.getAllTeams().size());
        
        request.addOrUpdateTeam(pirateTeam);

        TeamMember capnJack = new TeamMember(pirateTeam, "Capn", "Jack", "capnJack@gmail.com");
        TeamMember capnCrunch = new TeamMember(pirateTeam, "Capn", "Crunch", "capnCrunch@yahoo.com");
        TeamMember whiteShadow = new TeamMember(ninjaTeam, "White", "Shadow", "obviouslyfakeemail@lol.org");

        request.addOrUpdateTeamMember(capnJack);
        request.addOrUpdateTeamMember(capnCrunch);
        
        request.getEntityManager().flush();
        
        request.removeTeam(pirateTeam.getId());
        
        request.getEntityManager().flush();

        List<Team> teams = (List<Team>) request.getEntityManager().
                createNamedQuery("findAllTeams").getResultList();

        assertEquals(1, teams.size());

        request.addOrUpdateTeam(ninjaTeam);
        request.addOrUpdateTeamMember(whiteShadow);
        
        request.getEntityManager().flush();

        List<TeamMember> teamMembers = (List<TeamMember>) request.getEntityManager().
                createNamedQuery("findAllTeamMembers").getResultList();

        int numUnspecifiedTeamMembers = 0;
        for(TeamMember member : teamMembers) {
            if(member.getTeam() == request.getUnspecifiedTeam()) {
                numUnspecifiedTeamMembers++;
            }
        }

        //We expect that the deletion of the pirate team will have made two teams use unspecified teams.
        //So, only the ninja team member remains.
        assertEquals(2, numUnspecifiedTeamMembers);
        request.getEntityManager().flush();
    }
}