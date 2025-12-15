package Model;

public class Gato extends Animal implements Peludos{
    private boolean tosa;

    public Gato(String n){
        super(n, "Gato");
        tosa = false;
    }

    public void tosa(){
        System.out.println(getNome()+" foi tosado!");
        if(getBanho()){
            setLiberado(true);
        }
        setTosa(true);
    }

    public void banho(){
        System.out.println(getNome()+" foi tosado!");
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
