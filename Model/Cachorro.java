package Model;

public class Cachorro extends Animal implements Peludos{
    private boolean tosa;

    public Cachorro(String n){
        super(n, "Cachorro");
        tosa = false;
    }

    public void tosa(){
        System.out.println("Cachorro "+getNome()+" foi tosado!");
        if(getBanho()){
            setLiberado(true);
        }
        tosa = true;
    }

    public void banho(){
        System.out.println("Cachorro "+getNome()+" tomou banho!");
        if(tosa){
            setLiberado(true);
        }
        setBanho(true);
    }

    public boolean getTosa(){
        return tosa;
    }

    public void setTosa(boolean b){
        tosa = b;
    }
}
