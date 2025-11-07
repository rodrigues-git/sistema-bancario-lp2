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

    @Override
    public String getDescricao() {
        return "Cliente nº " + id + " - " + nome;
    }

    public void adicionarConta(conta conta) {
        if (conta != null) {
            contas.add(conta);
        }
    }

    public void getContas() {
        if (contas.isEmpty()) {
            System.out.println("Cliente " + nome + " não possui contas.");
            return;
        }

        System.out.println("Contas do cliente " + nome + ":");
        for (int i = 0; i < contas.size(); i++) {
            conta conta = contas.get(i);
            System.out.println(
                "Número: " + conta.getNumero() +
                " | Saldo: " + conta.getSaldo()
            );
        }
    }
}
