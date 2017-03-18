/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.jointheleague.iaroc.ejb;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jointheleague.iaroc.entities.Team;
import org.jointheleague.iaroc.entities.TeamMember;

@Stateful
public class RequestBean {

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("order.ejb.RequestBean");
    
    
    public void clearAll(){
        em.clear();
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
        try {
            TeamMember member = em.find(TeamMember.class, teamMemberId);
            em.remove(member);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void removeTeam(Integer teamId) {
        try {
            //Need to also remove team members, as a team member cannot exist without a team.
            if(teamId == null) {
                return;
            }
            Team team = em.find(Team.class, teamId);
            if(team != null) {
                getTeamMembers(teamId).forEach(tm -> { em.remove(tm);});
            }
            em.remove(team);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
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
    
    public List<TeamMember> getTeamMembers(int teamId) {
        List<TeamMember> teamMembers = findTeam(teamId).getMembers();
        return teamMembers;
    }

    //    public void createPart(String partNumber,
//            int revision,
//            String description,
//            java.util.Date revisionDate,
//            String specification,
//            Serializable drawing) {
//        try {
//            Part part = new Part(partNumber,
//                    revision,
//                    description,
//                    revisionDate,
//                    specification,
//                    drawing);
//            logger.log(Level.INFO, "Created part {0}-{1}", new Object[]{partNumber, revision});
//            em.persist(part);
//            logger.log(Level.INFO, "Persisted part {0}-{1}", new Object[]{partNumber, revision});
//        } catch (Exception ex) {
//            throw new EJBException(ex.getMessage());
//        }
//    }
//
//    public List<Part> getAllParts() {
//        List<Part> parts = (List<Part>) em.createNamedQuery("findAllParts").getResultList();
//        return parts;
//    }
//
//    public void addPartToBillOfMaterial(String bomPartNumber,
//            int bomRevision,
//            String partNumber,
//            int revision) {
//        logger.log(Level.INFO, "BOM part number: {0}", bomPartNumber);
//        logger.log(Level.INFO, "BOM revision: {0}", bomRevision);
//        logger.log(Level.INFO, "Part number: {0}", partNumber);
//        logger.log(Level.INFO, "Part revision: {0}", revision);
//        try {
//            PartKey bomKey = new PartKey();
//            bomKey.setPartNumber(bomPartNumber);
//            bomKey.setRevision(bomRevision);
//            
//            Part bom = em.find(Part.class, bomKey);
//            logger.log(Level.INFO, "BOM Part found: {0}", bom.getPartNumber());
//            
//            PartKey partKey = new PartKey();
//            partKey.setPartNumber(partNumber);
//            partKey.setRevision(revision);
//            
//            Part part = em.find(Part.class, partKey);
//            logger.log(Level.INFO, "Part found: {0}", part.getPartNumber());
//            bom.getParts().add(part);
//            part.setBomPart(bom);
//        } catch (EJBException e) {
//        }
//    }
//    
//    public void createVendor(int vendorId,
//            String name,
//            String address,
//            String contact,
//            String phone) {
//        try {
//            Vendor vendor = new Vendor(vendorId, name, address, contact, phone);
//            em.persist(vendor);
//        } catch (Exception e) {
//            throw new EJBException(e);
//        }
//    }
//    
//    public void createVendorPart(String partNumber,
//            int revision,
//            String description,
//            double price,
//            int vendorId) {
//        try {
//            PartKey pkey = new PartKey();
//            pkey.setPartNumber(partNumber);
//            pkey.setRevision(revision);
//            
//            Part part = em.find(Part.class, pkey);
//            
//            VendorPart vendorPart = new VendorPart(description, price, part);
//            em.persist(vendorPart);
//            
//            Vendor vendor = em.find(Vendor.class, vendorId);
//            vendor.addVendorPart(vendorPart);
//            vendorPart.setVendor(vendor);
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public void createOrder(Integer orderId, char status, int discount, String shipmentInfo) {
//        try {
//            Team order = new Team(orderId, status, discount, shipmentInfo);
//            Team foundOrder = findOrder(orderId);
//            if(foundOrder != null) {
//                em.merge(order);
//            }
//            else {
//                em.persist(order);
//            }
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//
//    public List<Team> getOrders() {
//        try {
//            return (List<Team>) em.createNamedQuery("findAllOrders").getResultList();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public void addLineItem(Integer orderId, String partNumber, int revision, int quantity) {
//        try {
//            Team order = em.find(Team.class, orderId);
//            logger.log(Level.INFO, "Found order ID {0}", orderId);
//            
//            PartKey pkey = new PartKey();
//            pkey.setPartNumber(partNumber);
//            pkey.setRevision(revision);
//            
//            Part part = em.find(Part.class, pkey);
//            
//            LineItem lineItem = new LineItem(order, quantity, part.getVendorPart());
//            order.addLineItem(lineItem);
//        } catch (Exception e) {
//            logger.log(Level.WARNING, "Couldn''t add {0} to order ID {1}.", new Object[]{partNumber, orderId});
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public double getBillOfMaterialPrice(String bomPartNumber, int bomRevision, String partNumber, int revision) {
//        double price = 0.0;
//        try {
//            PartKey bomkey = new PartKey();
//            bomkey.setPartNumber(bomPartNumber);
//            bomkey.setRevision(bomRevision);
//            
//            Part bom = em.find(Part.class, bomkey);
//            Collection<Part> parts = bom.getParts();
//            for (Part part : parts) {
//                VendorPart vendorPart = part.getVendorPart();
//                price += vendorPart.getPrice();
//            }
//            
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//        return price;
//    }
//    
//    public double getOrderPrice(Integer orderId) {
//        double price = 0.0;
//        try {
//            Team order = em.find(Team.class, orderId);
//            price = order.calculateAmmount();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//        return price;
//    }
//    
//    public void adjustOrderDiscount(int adjustment) {
//        try {
//            List orders = em.createNamedQuery(
//                    "findAllOrders")
//                    .getResultList();
//            for (Iterator it = orders.iterator(); it.hasNext();) {
//                Team order = (Team)it.next();
//                int newDiscount = order.getDiscount() + adjustment;
//                order.setDiscount((newDiscount > 0)? newDiscount : 0);
//            }
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public Double getAvgPrice() {
//        try {
//            return (Double) em.createNamedQuery(
//                    "findAverageVendorPartPrice")
//                    .getSingleResult();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public Double getTotalPricePerVendor(int vendorId) {
//        try {
//            return (Double) em.createNamedQuery(
//                    "findTotalVendorPartPricePerVendor")
//                    .setParameter("id", vendorId)
//                    .getSingleResult();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
//    
//    public List<String> locateVendorsByPartialName(String name) {
//        
//        List<String> names = new ArrayList<>();
//        try {
//            List vendors = em.createNamedQuery(
//                    "findVendorsByPartialName")
//                    .setParameter("name", name)
//                    .getResultList();
//            for (Iterator iterator = vendors.iterator(); iterator.hasNext();) {
//                Vendor vendor = (Vendor)iterator.next();
//                names.add(vendor.getName());
//            }
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//        return names;
//    }
//    
//    public int countAllItems() {
//        int count = 0;
//        try {
//            count = em.createNamedQuery(
//                    "findAllLineItems")
//                    .getResultList()
//                    .size();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//        return count;
//    }
//
//    public List<LineItem> getLineItems(int orderId) {
//        try {
//            return em.createNamedQuery("findLineItemsByOrderId")
//                    .setParameter("orderId", orderId)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
}
