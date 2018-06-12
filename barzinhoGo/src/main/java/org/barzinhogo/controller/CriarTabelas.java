/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.barzinhogo.controller;

import javax.persistence.Persistence;

/**
 * Classe para a criação e atualização das tabelas no banco de dados por meio da
 * EntityManangerFactory 
 *
 */
public class CriarTabelas {

    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("comandasGoCore");
    }
}
