public class Funcionario extends Pessoa {
    private int matricula;
    private String cargo;
    private double salario;

    public Funcionario(String nome, String cpf, String endereco, String telefone, int matricula, String cargo, double salario) {
        super(nome, cpf, endereco, telefone);
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double calcularBonificacao() {
        return this.salario * 0.10;
    }

    @Override
    public String getDescricao() {
        return "Funcionário (" + this.cargo + ") - " + this.getNome();
    }
}