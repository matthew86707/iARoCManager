/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="PERSISTENCE_TEAM_MEMBER")
@NamedQuery(
    name="findAllTeamMembers",
    query="SELECT t FROM TeamMember t " +
          "ORDER BY t.id"
)
@TableGenerator(name="teamMemberGen", initialValue=0, allocationSize=50)
/**
 * Bean representing a Team entity.
 */
public class TeamMember implements java.io.Serializable {
    private static final long serialVersionUID = 6582105865012174694L;
    @Id
    @SequenceGenerator(name = "TeamMemberSeq", sequenceName = "TeamMemberSeq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TeamMemberSeq")
    private Integer id;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "teamId")
    private Team team;
    
    @NotNull
    @Size(min=2, max=25)
    private String firstName;
    
    @NotNull
    @Size(min=2, max=25)
    private String lastName;
    
    @NotNull
    @Size(min=2, max=50)
    //Probably want to add more contact info. But, we will prob have a separate store for this info.
    //Besides, who cares other forms of communications when you have email?
    private String email;
    
    @Transient
    private Integer newTeamMemberId;
    
    public TeamMember() {
        firstName = "   ";
        lastName  = "   ";
        email = "   ";
    }
    
    public TeamMember(Team team, String firstName, String lastName, String email) {
        this.setTeam(team);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }
    
    public Team getTeam() {
        return team;
    }

    public final void setTeam(Team team) {
        if(this.team != null && !this.team.equals(team)) {
            this.team.removeTeamMember(this);
        }
        this.team = team;
        if(this.team != null) {
            this.team.addTeamMember(this);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getNewTeamMemberId() {
        if(newTeamMemberId == null && getTeam() != null) {
            return getTeam().getId();
        }
        return newTeamMemberId;
    }

    public void setNewTeamMemberId(Integer newTeamMemberId) {
        this.newTeamMemberId = newTeamMemberId;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "TeamMember{" + "id=" + id + ", team=" + team + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + '}';
    }
}