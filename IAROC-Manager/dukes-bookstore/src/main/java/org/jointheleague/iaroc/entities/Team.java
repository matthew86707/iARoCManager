/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="PERSISTENCE_TEAM")
@NamedQueries({
@NamedQuery(
    name="findAllTeams",
    query="SELECT t FROM Team t " +
          "ORDER BY t.id"
),
@NamedQuery(
    name="findMembersOfTeam",
    query="SELECT tm FROM TeamMember tm "
            + "WHERE tm.team.id = :teamId " +
          "ORDER BY tm.id"
)
})
@TableGenerator(name="teamGen", initialValue=0, allocationSize=50)
/**
 * Bean representing a Team entity.
 */
public class Team implements java.io.Serializable {
    private static final long serialVersionUID = 6582105865012174694L;
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="teamGen")
    private Integer id;
    @NotNull
    @Size(min=3, max=25)
    private String name;
    
    private String iconURL;
    
    public Team() {
        name = "   ";
        iconURL = "";
    }
 
    public Team(String name, String iconURL) {
        this.name = name;
        this.iconURL = iconURL;
    }
    
    public Team(Integer id, String name, String iconURL) {
        this.id = id;
        this.name = name;
        this.iconURL = iconURL;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Team other = (Team) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", iconURL=" + iconURL + '}';
    }

}
