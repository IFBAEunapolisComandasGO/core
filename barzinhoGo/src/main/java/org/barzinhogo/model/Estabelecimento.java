/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.barzinhogo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Jonathas
 */
@Entity
public class Estabelecimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "version")
    private int version;
    
    @Column(nullable = false)
    private String nome;
    
    /*private List<Funcionario> funcionarios;*/
    
    private List<Item> produtos;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final int version) {
        this.version = version;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * @return the funcionarios
     */
    /*
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    /**
     * @param funcionarios the funcionarios to set
     */
    /*
    public void setProdutos(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
    */
    //
    /**
     * @return the produtos
     */
    public List<Item> getProdutos() {
        return produtos;
    }

    /**
     * @param produtos the produtos to set
     */
    public void setProdutos(List<Item> produtos) {
        this.produtos = produtos;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cliente)) {
            return false;
        }
        Estabelecimento other = (Estabelecimento) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (nome != null && !nome.trim().isEmpty())
        result += "nome: " + nome;
        return result;
    }
}
