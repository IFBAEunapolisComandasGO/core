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
import org.barzinhogo.model.Estabelecimento;

/**
 *
 * @author Jonathas
 */
@Named
@Stateful
@ConversationScoped
public class EstabelecimentoBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /*
    * Support creating and retrieving Estabelecimento entities
    */
    private Long id;
    private Estabelecimento estabelecimento;
    
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }    

    /**
     * @return the estabelecimento
     */
    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    /**
     * @param estabelecimento the estabelecimento to set
     */
    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
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
            this.estabelecimento = this.example;
        } else {
            this.estabelecimento = findById(getId());
        }
    }
    public Estabelecimento findById(Long id) {
        return this.entityManager.find(Estabelecimento.class, id);
    }
    /*
     * Support updating and deleting Cliente entities
     */
    public String update() {
        this.conversation.end();
        try {
            if (this.id == null) {
                this.entityManager.persist(this.estabelecimento);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.estabelecimento);
                return "view?faces-redirect=true&id=" + this.estabelecimento.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }
    public String delete() {
        this.conversation.end();
        try {
            Estabelecimento deletableEntity = findById(getId());
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
    private List<Estabelecimento> pageItems;
    
    private Estabelecimento example = new Estabelecimento();
    
    public int getPage() {
        return this.page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPageSize() {
        return 10;
    }
    public Estabelecimento getExample() {
        return this.example;
    }
    public void setExample(Estabelecimento example) {
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
        Root<Estabelecimento> root = countCriteria.from(Estabelecimento.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems
        CriteriaQuery<Estabelecimento> criteria = builder.createQuery(Estabelecimento.class);
        root = criteria.from(Estabelecimento.class);
        TypedQuery<Estabelecimento> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }
    private Predicate[] getSearchPredicates(Root<Estabelecimento> root) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	List<Predicate> predicatesList = new ArrayList<Predicate>();
        
        String nome = this.example.getNome();
        if (nome != null && !"".equals(nome)) {
            predicatesList.add(builder.like(builder.lower(root.<String> get("nome")),'%' + nome.toLowerCase() + '%'));
        }
        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
    public List<Estabelecimento> getPageItems() {
        return this.pageItems;
    }
    public long getCount() {
        return this.count;
    }
    /*
     * Support listing and POSTing back Cliente entities (e.g. from inside an
     * HtmlSelectOneMenu)
     */
    
    public List<Estabelecimento> getAll() {
        CriteriaQuery<Estabelecimento> criteria = this.entityManager.getCriteriaBuilder().createQuery(Estabelecimento.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Estabelecimento.class))).getResultList();
    }
    
    @Resource
    private SessionContext sessionContext;
    
    public Converter getConverter() {
        final EstabelecimentoBean ejbProxy = this.sessionContext.getBusinessObject(EstabelecimentoBean.class);
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
                return String.valueOf(((Estabelecimento) value).getId());
            }
        };
    }
    /*
     * Support adding children to bidirectional, one-to-many tables
     */
    private Estabelecimento add = new Estabelecimento();
    
    public Estabelecimento getAdd() {
        return this.add;
    }
    public Estabelecimento getAdded() {
        Estabelecimento added = this.add;
        this.add = new Estabelecimento();
        return added;
    }
}