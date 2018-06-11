/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.barzinhogo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.barzinhogo.model.Mesa;

/**
 *
 * @author Jonathas
 */
@Named
@Stateful
@ConversationScoped
public class MesaBean implements Serializable {
    /*
     * Support creating and retrieving Estabelecimento entities
     */
    private Long id;
    private Mesa mesa;
    private boolean disponivel;
    
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }    

    /**
     * @return the mesa
     */
    public Mesa getEstabelecimento() {
        return mesa;
    }

    /**
     * @param mesa the mesa to set
     */
    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    /**
     * @return the disponivel
     */
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * @param disponivel the disponivel to set
     */
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    //////////////////////////////////////////////////////////////////////////
    @Inject
    private Conversation conversation;
    
    @PersistenceContext(unitName = "barzinhoGo-persistence-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    
    public String create() {
        this.conversation.begin();
        this.conversation.setTimeout(1800000L);
        return "create?faces-redirect=true";
    }
    public void retrieve() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }
        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }
        if (this.id == null) {
            this.mesa = this.example;
        } else {
            this.mesa = findById(getId());
        }
    }
    public Mesa findById(Long id) {
        return this.entityManager.find(Mesa.class, id);
    }
    /*
     * Support updating and deleting Cliente entities
     */
    public String update() {
        this.conversation.end();
        try {
            if (this.id == null) {
                this.entityManager.persist(this.mesa);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.mesa);
                return "view?faces-redirect=true&id=" + this.mesa.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }
    public String delete() {
        this.conversation.end();
        try {
            Mesa deletableEntity = findById(getId());
            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }
    
    /*
     * Support searching Cliente entities with pagination
     */
    
    private int page;
    private long count;
    private List<Mesa> pageItems;
    
    private Mesa example = new Mesa();
    
    public int getPage() {
        return this.page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPageSize() {
        return 10;
    }
    public Mesa getExample() {
        return this.example;
    }
    public void setExample(Mesa example) {
        this.example = example;
    }
    public String search() {
        this.page = 0;
        return null;
    }
    public void paginate() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        
        // Populate this.count
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Mesa> root = countCriteria.from(Mesa.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems
        CriteriaQuery<Mesa> criteria = builder.createQuery(Mesa.class);
        root = criteria.from(Mesa.class);
        TypedQuery<Mesa> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }
    private Predicate[] getSearchPredicates(Root<Mesa> root) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	List<Predicate> predicatesList = new ArrayList<Predicate>();
        
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
    public List<Mesa> getPageItems() {
        return this.pageItems;
    }
    public long getCount() {
        return this.count;
    }
    /*
     * Support listing and POSTing back Cliente entities (e.g. from inside an
     * HtmlSelectOneMenu)
     */
    
    public List<Mesa> getAll() {
        CriteriaQuery<Mesa> criteria = this.entityManager.getCriteriaBuilder().createQuery(Mesa.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Mesa.class))).getResultList();
    }
    
    @Resource
    private SessionContext sessionContext;
    
    public Converter getConverter() {
        final MesaBean ejbProxy = this.sessionContext.getBusinessObject(MesaBean.class);
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                return ejbProxy.findById(Long.valueOf(value));
            }
            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null) {
                    return "";
                }
                return String.valueOf(((Mesa) value).getId());
            }
        };
    }
    /*
     * Support adding children to bidirectional, one-to-many tables
     */
    private Mesa add = new Mesa();
    
    public Mesa getAdd() {
        return this.add;
    }
    public Mesa getAdded() {
        Mesa added = this.add;
        this.add = new Mesa();
        return added;
    }
}