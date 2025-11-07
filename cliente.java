import java.util.ArrayList;

public class cliente extends pessoa{
    static int contador = 0;
    private int id;
    private String tipoCliente;
    private double renda;
    ArrayList<conta>contas = new ArrayList<>();

    public cliente(String nome, String cpf, String endereco, String telefone, String tipoCliente, double renda){
        super(nome, cpf, endereco, telefone);
        contador ++;
        id = contador;
        this.tipoCliente = tipoCliente;
        this.renda = renda;
    }

    public String getTipoCliente(){
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente){
        this.tipoCliente = tipoCliente;
    }

    public double getRenda(){
        return renda;
    }

    public void setRenda(double renda){
        this.renda = renda;
    }
}
