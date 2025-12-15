package Controller;

import Model.*;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ControleAnimal {

    private final String caminho = "animais.csv";

    public ControleAnimal() {
        try (FileWriter writer = new FileWriter(caminho, true)) {
            if (new java.io.File(caminho).length() == 0) {
                writer.append("ID,Nome,Especie,Banho,Tosa,Liberado\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao inicializar CSV");
            e.printStackTrace();
        }
    }

    private Animal criarAnimal(String tipo, String nome) {
        return switch (tipo) {
            case "Cachorro" -> new Cachorro(nome);
            case "Gato" -> new Gato(nome);
            case "Papagaio" -> new Papagaio(nome);
            default -> throw new IllegalArgumentException("Animal inválido no CSV");
        };
    }

    public void addCSV(Animal a) {
        Object tosa;
        try (FileWriter writer = new FileWriter(caminho, true)) {
            a.setPetID(getUltimoID()+1);
            if(a instanceof Cachorro || a instanceof Gato){
                tosa = ((Peludos) a).getTosa();
            }else{
                tosa = "n/a";
            }

            writer.append(a.getPetID() + "," + a.getNome() + "," + a.getEspecie() + "," + a.getBanho()+ "," +tosa+ "," + a.getLiberado() +"\n");
        } catch (IOException e) {
            System.out.println("Não foi possível adicionar no arquivo.");
            e.printStackTrace();
        }
    }


    public ArrayList<Animal> getAllCSV() {
        ArrayList<Animal> animais = new ArrayList<>();

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
                if(animal instanceof Cachorro || animal instanceof Gato){
                    ((Peludos) animal).setTosa(Boolean.parseBoolean(dados[4]));
                }
                animais.add(animal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return animais;
    }

    public void removerPorID(int idRemover) {
        ArrayList<String> linhas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                // mantém cabeçalho
                if (linha.startsWith("ID,")) {
                    linhas.add(linha);
                    continue;
                }

                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(",");
                int idAtual = Integer.parseInt(dados[0]);

                if (idAtual != idRemover) {
                    linhas.add(linha);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // reescreve o arquivo
        try (FileWriter writer = new FileWriter(caminho, false)) {
            for (String l : linhas) {
                writer.write(l + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getUltimoID() {
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

    public void updateCSV(Animal animalAtualizado) {
        ArrayList<String> linhas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;

            while ((linha = reader.readLine()) != null) {

                // REMOVE BOM + espaços invisíveis
                linha = linha.replace("\uFEFF", "").trim();

                if (linha.isEmpty()) continue;

                if (linha.startsWith("ID,")) {
                    linhas.add(linha);
                    continue;
                }

                String[] dados = linha.split(",");

                // segurança extra
                if (dados.length < 6) {
                    linhas.add(linha);
                    continue;
                }

                int idAtual = Integer.parseInt(dados[0]);

                if (idAtual == animalAtualizado.getPetID()) {
                    String tosa;

                    if (animalAtualizado instanceof Cachorro || animalAtualizado instanceof Gato) {
                        tosa = Boolean.toString(((Peludos) animalAtualizado).getTosa());
                    } else {
                        tosa = "n/a";
                    }

                    String novaLinha =
                            animalAtualizado.getPetID() + "," +
                                    animalAtualizado.getNome() + "," +
                                    animalAtualizado.getEspecie() + "," +
                                    animalAtualizado.getBanho() + "," +
                                    tosa + "," +
                                    animalAtualizado.getLiberado();

                    linhas.add(novaLinha);
                } else {
                    linhas.add(linha);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(caminho, false)) {
            for (String l : linhas) {
                writer.write(l + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
