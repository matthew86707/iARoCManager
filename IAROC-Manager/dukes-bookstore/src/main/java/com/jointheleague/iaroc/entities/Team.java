/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.jointheleague.iaroc.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * <p>Entity class for teams.</p>
 */
@Entity
@Table(name = "TEAMS")
@NamedQuery(
        name = "findTeams",
        query = "SELECT t FROM Team t ORDER BY t.teamId")
public class Team implements Serializable {

    private static final long serialVersionUID = -4146681491856848089L;
    @Id
    @NotNull
    private String teamId;
    private String name;
    private String imageUrl;
    
    private Team() {
    }

    public Team(String teamId, String name, String imageUrl) {
        this.teamId = teamId;
        this.name = name;
        this.imageUrl = imageUrl;
        
    }

    public Team(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

  
    @Override
    public int hashCode() {
        return teamId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        return this.teamId.equals(other.teamId);
    }

    @Override
    public String toString() {
        return "jointheleague.iaroc.entities.Team [teamId=" + teamId + " ]";
    }
}
