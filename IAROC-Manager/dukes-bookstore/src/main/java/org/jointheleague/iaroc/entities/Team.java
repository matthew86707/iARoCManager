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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUtil;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jointheleague.iaroc.ejb.RequestBean;

@Entity
@Table(name="PERSISTENCE_TEAM")
@NamedQueries({
@NamedQuery(
    name="findAllTeams",
    query="SELECT t FROM Team t WHERE t.name <> 'Unspecified'"
),
@NamedQuery(
    name="findTeamByName",
    query="SELECT t FROM Team t WHERE t.name = :name "
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
    @Size(max=25)
    private String name;
    private String slogan;
    private String iconURL;
    
    @OneToMany(cascade = CascadeType.ALL, 
        mappedBy = "team", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TeamMember> teamMembers = new ArrayList<>();
    
    public Team() {
        name = "";
        iconURL = "";
        slogan = "";
    }
 
    public Team(String name, String iconURL) {
        this.name = name;
        this.iconURL = iconURL;
        this.slogan = "";
    }
    
    public Team(Integer id, String name, String iconURL) {
        this.id = id;
        this.name = name;
        this.iconURL = iconURL;
        this.slogan = "";
    }
    
    public Team(String name, String slogan, String iconURL){
        this.name = name;
        this.slogan = slogan;
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
    public String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", iconURL=" + iconURL + '}';
    }
    
    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void addTeamMember(TeamMember teamMember) {
        //Add-and-remove incase we have a match due to something like ID but this one is newer.
        if(this.teamMembers.contains(teamMember)) {
            this.teamMembers.remove(teamMember);
        }
        this.teamMembers.add(teamMember);
    }

    public void removeTeamMember(TeamMember capnCrunch) {
        this.teamMembers.remove(capnCrunch);
    }
}
