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
import java.util.List;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.OneToMany;

@Entity
@Table(name="PERSISTENCE_TEAM")
@NamedQueries({
@NamedQuery(
    name="findAllTeams",
    query="SELECT t FROM Team t " +
          "ORDER BY t.id"
)})
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
    @Size(max=25)
    private String name;
    @Size(max=128)
    private String slogan;
    private String iconURL;
    
    @OneToMany(cascade=ALL, mappedBy="teamMember")
    private List<TeamMember> members;
    
    public Team() {
        name = "";
        iconURL = "";
        slogan = "";
    }
 
    public Team(String name, String iconURL, String slogan) {
        this.name = name;
        this.iconURL = iconURL;
        this.slogan = slogan;
    }
    
    public Team(Integer id, String name, String iconURL, String slogan) {
        this.id = id;
        this.name = name;
        this.iconURL = iconURL;
        this.slogan = slogan;
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
    
    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
    
    public List<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMember> members) {
        this.members = members;
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
        return "Team{" + "id=" + id + ", name=" + name + ", iconURL=" + iconURL + ", slogan=" + slogan + '}';
    }

}
