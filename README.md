# PIX Seguro - Sistema Anti-Golpe PIX

> Projeto A3 - Desafio Bradesco:
> Solução para combater ao golpe do PIX


---

## Sobre o projeto

O **PIX Seguro** é um sistema bancário simulado desenvolvido em java puro que propõe uma solução para o **Golpe do PIX**, um dos golpes financeiros mais comuns do Brasil.

### O Golpe
O golpista envia um PIX para a vítima e pede a devolução para outra conta. Em seguida, aciona o banco alegando que a vítima não devolveu o dinheiro. O banco então retira o valor da conta da vítima, fazendo o golpista ficar com o dobro.

### Nossa solução
O PIX **não cai imediatamente** na conta do destinatário. Ele fica em estado **PENDENTE** até que o destinatário aceite ou rejeite. Sem receber o dinheiro, a vítima não tem nada a devolver e o golpe não é concretizado.

### Fluxo da solução ao golpe
Golpista envia PIX ➔ PIX fica pendente ➔ Vítima vê: nome, CPF e valor da transação do remetente ➔ Vítima rejeita (Pois é um PIX vindo de uma conta desconhecida) ➔ Golpe não concretizado!

---

## Como executar

### Pré-requisito
Ter o java instalado. Verifique com:
```bash
java -version
```

### Opção 1 - Executável (mais simples)
Baixe o `pix-seguro.jar` na seção [Releases](https://github.com/lau8708/pix-seguro/releases) e rode:
```bash
java -jar pix-seguro.jar
```

---

## Arquitetura

O projeto segue uma **arquitetura em camadas**:

| UI (Console) | entrada e saída de dados |
|--------------|-------------------------|
| Service      | regras de negócio       |
| Repository   | acesso aos dados em memória|
 | Model        | entidades do sistema    |   

### Estrutura de pacotes

src/main/java  
├── exception/  
│   ├── PixException.java  
│   └── ... (14 exceptions personalizadas)  
├── model/  
│   └── enums/  
│       ├── PerfilUsuario.java  
│       └── StatusTransacao.java  
│   ├── Conta.java  
│   ├── Transacao.java  
│   ├── Usuario.java  
├── repository/  
│   ├── ContaRepository.java  
│   ├── TransacaoRepository.java  
│   └── UsuarioRepository.java  
├── service/  
│   ├── AdminService.java  
│   ├── AuthService.java  
│   ├── ContaService.java  
│   ├── PixService.java  
│   └── UsuarioService.java  
├── ui/  
│   ├── AdminConsole.java  
│   ├── AuthConsole.java  
│   ├── ContaConsole.java   
│   ├── Cores.java   
│   ├── MenuPrincipal.java  
│   └── PixConsole.java  
└── Main.java

---

## Estrutura de dados e complexidade

Todos os repositórios usam `HashMap` para garantir performance:

| Operação | Estrutura | Complexidade |
|----------|-----------|-------------|
| Buscar usuário por CPF | `HashMap` | O(1) |
| Buscar conta por CPF | `HashMap` | O(1) |
| Buscar transação por ID | `HashMap` | O(1) |
| Listar pendentes | filtro em `HashMap` | O(n) |
| Listar histórico | filtro em `HashMap` | O(n) |

---

## Requisitos funcionais

| Código | Requisito |
|--------|-----------|
| RF01 | Cadastro de usuário com nome, CPF e senha |
| RF02 | Login por CPF e senha |
| RF03 | Enviar PIX — fica pendente, saldo é reservado |
| RF04 | Caixa de entrada com PIX pendentes |
| RF05 | Aceitar ou rejeitar PIX pendente |
| RF06 | Histórico de transações do usuário |

## Regras de negócio

| Código | Regra |
|--------|-------|
| RN01 | Saldo não pode ficar negativo |
| RN02 | PIX pendente reserva o valor imediatamente |
| RN03 | PIX pendente expira em 24h sem resposta |
| RN04 | Só o destinatário aceita ou rejeita |
| RN05 | CPF deve ser único no sistema |
| RN06 | Valor do PIX deve ser maior que zero |
| RN07 | Admin pode visualizar todas as transações e contas |

## Testes unitários

O projeto conta com **45 testes unitários** cobrindo todos os services:

| Classe               | Testes    |
|----------------------|-----------|
| `UsuarioServiceTest` | 9 testes  |
| `AuthServiceTest`    | 10 testes |
| `PixServiceTest`     | 15 testes |
| `ContaServiceTest`   | 11 testes |

Para rodar os testes:
```bash
mvn test
```

---

## Decisões técnicas

**BigDecimal** - usado para valores financeiros evitando imprecisão do `double`

**UUID** - identificadores únicos gerados automáticamente para contas e transações

**HashMap** - estrutura principal dos repositories garantindo busca O(1)

**Exceptions personalizadas** - hierarquia própria herdando de `PixException` para tratamento de erros precisos

**Primeiro usuário como Admin** - o primeiro cadastro recebe perfil administrador automaticamente

**PIX pendente por 24h** - transações não respondidas expiram automaticamente

## Equipe

| Integrantes  |
|--------------|
| Laudson Euller |
| Ingryd Moura |
| Lawrence Ewerton |
| Evany Maria  |
| Ivandro Junior |

## Tecnologias

![Java](https://img.shields.io/badge/Java-25-orange)
![Maven](https://img.shields.io/badge/Maven-3.x-red)
![JUnit](https://img.shields.io/badge/JUnit-5.12.2-green)

---

> Desenvolvido para o projeto A3 - Desafio Bradesco


