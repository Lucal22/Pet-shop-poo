package Model;

public abstract class Animal {
    private String nome;
    private int petID;
    private String especie;
    private boolean banho;
    private boolean liberado;


    public Animal(String n, String e){
        nome = n;
        liberado = false;
        especie = e;
    }
    public Animal(String n){
        nome = n;
        liberado = false;
        especie = "desconhecida";
    }

    public abstract void banho();

    public String getEspecie(){
        return especie;
    }

    public String getNome(){
        return nome;
    }

    public boolean getBanho(){
        return banho;
    }

    public void setBanho(boolean b){
        banho = b;
    }

    public int getPetID(){
        return petID;
    }
     public void setPetID(int id){
        petID = id;
     }



    public boolean getLiberado(){
        return liberado;
    }

    public void setLiberado(boolean b){
        liberado = b;
    }



}
