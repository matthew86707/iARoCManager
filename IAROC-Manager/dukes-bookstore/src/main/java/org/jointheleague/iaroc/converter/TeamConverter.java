/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jointheleague.iaroc.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.jointheleague.iaroc.ejb.RequestBean;
import org.jointheleague.iaroc.entities.Team;

/**
 *
 * @author patri_000
 */
@ManagedBean(name = "teamConverterBean") 
@FacesConverter(value = "teamConverter")
public class TeamConverter implements Converter{
    
    @EJB
    RequestBean request;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer id = Integer.parseInt(value);
        Team team = request.findTeam(id);
        return team;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Integer val = (Integer)value;
        return Integer.toString(val);
    }
}
