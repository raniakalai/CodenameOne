/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

/**
 *
 * @author USER
 */
public class Voiture {
    int id;
    String immatricule;
    String marque;
    String modele;
    int age;
    String couleur;
    int cout;

    public Voiture() {
    }

    public Voiture(int id, String immatricule, String marque, String modele, int age, String couleur, int cout) {
        this.id = id;
        this.immatricule = immatricule;
        this.marque = marque;
        this.modele = modele;
        this.age = age;
        this.couleur = couleur;
        this.cout = cout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImmatricule() {
        return immatricule;
    }

    public void setImmatricule(String immatricule) {
        this.immatricule = immatricule;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    @Override
    public String toString() {
        return "Voiture{" + "id=" + id + ", immatricule=" + immatricule + ", marque=" + marque + ", modele=" + modele + ", age=" + age + ", couleur=" + couleur + ", cout=" + cout + '}';
    }
    
}
