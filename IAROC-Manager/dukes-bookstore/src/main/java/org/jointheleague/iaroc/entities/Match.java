/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PERSISTENCE_TEAM_MEMBER")
@NamedQuery(
        name = "findAllTeamMembers",
        query = "SELECT t FROM TeamMember t "
        + "ORDER BY t.id"
)
@TableGenerator(name = "teamMemberGen", initialValue = 0, allocationSize = 50)
/**
 * Bean representing a Team entity.
 */
public class Match implements java.io.Serializable {

    private static final long serialVersionUID = 6582105865012174694L;
    @Id
    @SequenceGenerator(name = "TeamMemberSeq", sequenceName = "TeamMemberSeq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeamMemberSeq")
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MATCH_TEAMS")
    private List<Team> teams = new ArrayList<Team>();

    private Long unixMatchTime;

    @Transient
    private Integer newMatchId;

    public Match() {
        this.unixMatchTime = null;
    }

    public Match(long unixMatchTime) {
        this.unixMatchTime = unixMatchTime;
    }

    public Match(List<Team> teams, long unixMatchTime) {
        this.unixMatchTime = unixMatchTime;
        for (Team toAdd : teams) {
            addTeam(toAdd);
        }
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    public void removeTeam(Team toRemove) {
        teams.remove(toRemove);
        toRemove.removeMatch(this);
    }

    public void addTeam(Team toAdd) {
        if (!(toAdd.getMatches().contains(this))) {
            teams.add(toAdd);
            toAdd.addMatch(this);
        }
    }

    private final void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
