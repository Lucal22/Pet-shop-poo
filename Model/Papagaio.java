package Model;

public class Papagaio extends Animal{
    public Papagaio(String n){
        super(n, "Papagaio");
    }

    public Papagaio(){
        super("Papagaio");
    }

    public void banho(){
        System.out.println("Papagaio "+getNome()+" tomou banho");
        setBanho(true);
        setLiberado(true);
    }
}
