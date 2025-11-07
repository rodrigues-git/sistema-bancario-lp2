public class funcionario extends pessoa {
    private String matricula;
    private String cargo;
    private double salario;


    public funcionario(String nome, String cpf, String endereco, String telefone, String matricula, String cargo, double salario){
        super(nome, cpf, endereco, telefone);
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
    }
}
