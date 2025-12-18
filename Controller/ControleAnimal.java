package Controller;

import DAO.AnimalDAO;
import Model.*;
import View.View;

import javax.swing.*;
import java.util.ArrayList;

public class ControleAnimal {

    private final String caminho = "animais.csv";
    private View v;

    public ControleAnimal(View v) {
        this.v = v;
        AnimalDAO.criaArquivo();
    }

    public boolean addAnimal(Animal a){
        if (a.getNome().matches("^\\p{L}.*")) {
            AnimalDAO.addAnimal(a);
            return true;
        }
        JOptionPane.showMessageDialog(v.getPainelGeral(), "O nome precisa iniciar com letras.");
        return false;
    }

    public ArrayList<Animal> getAllAnimals() {return AnimalDAO.getAllAnimals();}


    public void removeAnimal(int id) {
        AnimalDAO.removeAnimal(id);
    }

    public void updateAnimal(Animal animalAtualizado) {
        AnimalDAO.updateAnimal(animalAtualizado);
    }

}
