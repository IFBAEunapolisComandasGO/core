/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.barzinhogo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;


/**
 *
 * @author Jonathas
 */
@Entity
public class Mesa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;
    
    @Column(nullable = false)
    private int qrCode;
    
    private Map<Cliente, Pedido> pedidos;
    private List<Cliente> clientes;
    
    private boolean disponivel;

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
     * @return the qrCode
     */
    public int getQrCode() {
        return qrCode;
    }

    /**
     * @param qrCode the qrCode to set
     */
    public void setQrCode(int qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * @return the pedidos
     */
    public Map<Cliente, Pedido> getPedidos() {
        return pedidos;
    }

    /**
     * @param pedidos the pedidos to set
     */
    public void setPedidos(Map<Cliente, Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    /**
     * @param clientes the clientes to set
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Mesa)) {
            return false;
        }
        Mesa other = (Mesa) obj;
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
        return null;
    }
}
