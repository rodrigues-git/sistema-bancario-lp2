import java.util.ArrayList;

public class Cliente extends Pessoa {
    private static int contador = 0;
    private int id;
    private String tipoCliente;
    private double renda;
    private ArrayList<Conta> contas = new ArrayList<>();

    public Cliente(String nome, String cpf, String endereco, String telefone, String tipoCliente, double renda) {
        super(nome, cpf, endereco, telefone);
        contador++;
        this.id = contador;
        this.tipoCliente = tipoCliente;
        this.renda = renda;
    }

    public int getId() {
        return id;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    @Override
    public String getDescricao() {
        return "Cliente nº " + this.id + " - " + this.getNome();
    }

    public void adicionarConta(Conta conta) {
        if (conta != null) {
            this.contas.add(conta);
        }
    }

    public ArrayList<Conta> getContas() {
        return this.contas;
    }
}