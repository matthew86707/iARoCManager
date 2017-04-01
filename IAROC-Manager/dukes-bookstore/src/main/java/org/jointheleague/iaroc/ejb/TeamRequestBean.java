/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.ejb;

import org.jointheleague.iaroc.entities.Team;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jointheleague.iaroc.exceptions.TeamsNotFoundException;
import org.jointheleague.iaroc.exceptions.TeamNotFoundException;

/**
 * <p>
 * Stateful session bean for the teams.</p>
 */
@Stateful
public class TeamRequestBean {

    @PersistenceContext
    private EntityManager em;
    private static final Logger logger
            = Logger.getLogger("com.jointheleague.iaroc.ejb.TeamRequestBean");

    public TeamRequestBean() throws Exception {
    }

    public void createTeam(String name, String imageUrl, String slogan) {
        try {
            Team team = new Team(name, imageUrl, slogan);
            logger.log(Level.INFO, "Created team {0}", team.getId());
            em.persist(team);
            logger.log(Level.INFO, "Persisted team {0}", team.getId());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public List<Team> getTeams() throws TeamsNotFoundException {
        try {
            return (List<Team>) em.createNamedQuery("findTeams").getResultList();
        } catch (Exception ex) {
            throw new TeamsNotFoundException(
                    "Could not get teams: " + ex.getMessage());
        }
    }

    public Team getTeam(String teamId) throws TeamNotFoundException {
        Team requestedTeam = em.find(Team.class, teamId);

        if (requestedTeam == null) {
            throw new TeamNotFoundException("Couldn't find book: " + teamId);
        }

        return requestedTeam;
    }
}
