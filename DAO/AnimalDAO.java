package DAO;

import Model.*;

import java.io.*;
import java.util.ArrayList;

public class AnimalDAO {

    private static final String caminho = "animais.csv";
    private static ArrayList<Animal> animais;

    public static void criaArquivo(){
        animais = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true)) ) {
            if (new java.io.File(caminho).length() == 0) {
                writer.write("ID,Nome,Especie,Banho,Tosa,Liberado\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao inicializar CSV");
            e.printStackTrace();
        }
    }

    private static Animal criarAnimal(String tipo, String nome) {
        return switch (tipo) {
            case "Cachorro" -> new Cachorro(nome);
            case "Gato" -> new Gato(nome);
            case "Papagaio" -> new Papagaio(nome);
            default -> throw new IllegalArgumentException("Animal inválido no CSV");
        };
    }

    public static void addAnimal(Animal a) {
        Object tosa;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true)) ) {
            a.setPetID(getUltimoID()+1);
            animais.add(a);
            if(a instanceof Peludos){
                tosa = ((Peludos) a).getTosa();
            }else{
                tosa = "n/a";
            }
            writer.write(a.getPetID() + "," + a.getNome() + "," + a.getEspecie() + "," + a.getBanho()+ "," +tosa+ "," + a.getLiberado() +"\n");
        } catch (IOException e) {
            System.out.println("Não foi possível adicionar no arquivo.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Animal> getAllAnimals() {
        animais.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(",");

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String especie = dados[2];

                Animal animal = criarAnimal(especie, nome);
                animal.setPetID(id);
                animal.setBanho(Boolean.parseBoolean(dados[3]));
                animal.setLiberado(Boolean.parseBoolean(dados[5]));
                if(animal instanceof Peludos){
                    ((Peludos) animal).setTosa(Boolean.parseBoolean(dados[4]));
                }
                animais.add(animal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return animais;
    }

    public static void removeAnimal(int idRemover) {
        for(int i = 0;i < animais.size(); i++){
            if(animais.get(i).getPetID() == idRemover){
                System.out.println(animais.get(i).getEspecie()+" "+ animais.get(i).getNome()+" removido");
                animais.remove(i);
                break;
            }
        }
        updateCSV();
    }

    public static void updateAnimal(Animal animalAtualizado) {
        for(int i = 0;i < animais.size(); i++){
            if(animais.get(i).getPetID() == animalAtualizado.getPetID()){
                animais.set(i, animalAtualizado);
                break;
            }
        }
        updateCSV();
    }

    private static void updateCSV(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, false)) ) {
            writer.write("ID,Nome,Especie,Banho,Tosa,Liberado\n");
            for (Animal a : animais) {
                Object tosa;
                if(a instanceof Cachorro || a instanceof Gato){
                    tosa = ((Peludos) a).getTosa();
                }else{
                    tosa = "n/a";
                }

                writer.write(a.getPetID() + "," + a.getNome() + "," + a.getEspecie() + "," + a.getBanho()+ "," +tosa+ "," + a.getLiberado() +"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getUltimoID() {
        String ultimaLinha = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;

            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    ultimaLinha = linha;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ultimaLinha == null) {
            return 0;
        }

        String[] dados = ultimaLinha.split(",");
        return Integer.parseInt(dados[0]);
    }




}

