package Model;

public class Papagaio extends Animal{
    public Papagaio(String n){
        super(n, "Papagaio");
    }
    public void banho(){
        System.out.println();
        setBanho(true);
        setLiberado(true);
    }
}
