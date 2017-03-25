/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.ejb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jointheleague.iaroc.entities.Team;
import org.jointheleague.iaroc.entities.TeamMember;

@Stateful
public class RequestBean {

    @PersistenceContext(unitName="iaroc")
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("order.ejb.RequestBean");
    
    public static final String TEST_PU = "iaroc-test";
    
    private static RequestBean testBean = null;
    
    public static final String UNSPECIFICIED_TEAM_NAME = "Unspecified";

    
    /**
     * If this isn't being provided by the framework, provide a method of injecting it ourselves.
     * @return 
     */
    public static RequestBean getTestBean() {
        if(testBean == null) {
            testBean = new RequestBean();
            testBean.em = Persistence.createEntityManagerFactory(TEST_PU).
                createEntityManager();
            testBean.getSession().beginTransaction();
        }
        return testBean;
    }

    public EntityManager getEntityManager() {
        return em;
    }
    
    public Session getSession() {
        return em.unwrap(Session.class);
    }

    public void clearDatabase() {
        Connection c = ((SessionImpl) em.getDelegate()).connection();
        Statement s;
        try {
            s = c.createStatement();

            s.execute("SET DATABASE REFERENTIAL INTEGRITY FALSE");
            Set<String> tables = new HashSet<String>();
            ResultSet rs = s.executeQuery("select table_name "
                    + "from INFORMATION_SCHEMA.system_tables "
                    + "where table_type='TABLE' and table_schem='PUBLIC'");
            while (rs.next()) {
                if (!rs.getString(1).startsWith("DUAL_")) {
                    tables.add(rs.getString(1));
                }
            }
            rs.close();
            for (String table : tables) {
                s.executeUpdate("DELETE FROM " + table);
            }
            s.execute("SET DATABASE REFERENTIAL INTEGRITY TRUE");
            s.close();
        } catch (SQLException ex) {
        }
    }
    
    private Team unspecifiedTeam = null;
    public Team getUnspecifiedTeam() {
        
        if(unspecifiedTeam != null) {
            return unspecifiedTeam;
        }
                
        unspecifiedTeam = getTeamByName(UNSPECIFICIED_TEAM_NAME);
        
        if(unspecifiedTeam != null) {
            return unspecifiedTeam;
        }
        else {
            unspecifiedTeam = new Team(UNSPECIFICIED_TEAM_NAME, UNSPECIFICIED_TEAM_NAME);
            em.persist(unspecifiedTeam);
            return unspecifiedTeam;
        }
    }

    public void addOrUpdateTeam(Team team) {
        Team foundTeam = findTeam(team.getId());
        if (foundTeam != null) {
            em.merge(team);
        } else {
            em.persist(team);
        }
    }

    public void addOrUpdateTeamMember(TeamMember teamMember) {
        
        Integer newTeamMemberId = teamMember.getNewTeamMemberId();
        if(teamMember.getTeam() == null || newTeamMemberId != teamMember.getTeam().getId()) {
            Team newTeam = findTeam(newTeamMemberId);
            if(newTeam != null) {
                teamMember.setTeam(newTeam);
            }
        }
        
        TeamMember foundTeamMember = findTeamMember(teamMember.getId());
        if (foundTeamMember != null) {
            em.merge(teamMember);
        } else {
            em.persist(teamMember);
        }
    }

    public void removeTeamMember(Integer teamMemberId) {
        TeamMember member = em.find(TeamMember.class, teamMemberId);
        if(member != null && member.getTeam() != null && member.getTeam() != getUnspecifiedTeam()) {
            Team teamPersisted = em.find(Team.class, member.getTeam().getId());
            if(teamPersisted != null) {
                teamPersisted.getTeamMembers().remove(member);
                em.merge(member.getTeam());
            }
        }
        em.remove(member);
    }

    public void removeTeam(Integer teamId) {
        //Need to also remove team members, as a team member cannot exist without a team.
        if(teamId == null) {
            return;
        }
        Team team = em.find(Team.class, teamId);
        if(team != null) {
            Team unspecifiedTeam = getUnspecifiedTeam();
            //Can't just loop over them in a for each because the collection will change as it loops.
            while(!team.getTeamMembers().isEmpty()) {
                TeamMember currentMember = team.getTeamMembers().get(0);
                team.removeTeamMember(currentMember);
                TeamMember currentMemberPersisted = em.find(TeamMember.class, currentMember.getId());
                if(currentMemberPersisted != null) {
                    currentMemberPersisted.setTeam(unspecifiedTeam);
                    em.merge(currentMemberPersisted);
                }  
            }
            em.remove(team);
        }
    }
    
    public Team getTeamByName(String name) {
        List<Team> teams = (List<Team>) em.createNamedQuery("findTeamByName").
                setParameter("name", name).getResultList();
        if(teams.isEmpty()) {
            return null;
        }
        return teams.get(0);
    }

    public TeamMember findTeamMember(Integer id) {
        try {
            if(id == null) {
                return null;
            }
            TeamMember teamMember = em.find(TeamMember.class, id);
            return teamMember;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public Team findTeam(Integer id) {
        try {
            if(id == null) {
                return null;
            }
            Team team = em.find(Team.class, id);
            return team;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<Team> getAllTeams() {
        List<Team> teams = (List<Team>) em.createNamedQuery("findAllTeams").getResultList();
        return teams;
    }

    public List<TeamMember> getAllTeamMembers() {
        List<TeamMember> teamMembers = (List<TeamMember>) 
                em.createNamedQuery("findAllTeamMembers").getResultList();
        return teamMembers;
    }
}
