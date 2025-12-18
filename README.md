# 🐾 Sistema de Gerenciamento de Pet Shop

Sistema desenvolvido em Java para gerenciamento de atendimentos em um Pet Shop, implementando conceitos fundamentais de Programação Orientada a Objetos.

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como Trabalho Prático 2 da disciplina de Programação Orientada a Objetos I (POO I) do IFMG. O sistema permite o cadastro e gerenciamento de diferentes tipos de animais (Cachorros, Gatos e Papagaios), oferecendo serviços de banho e tosa, com persistência de dados em arquivo CSV.

## ✨ Funcionalidades

- **Cadastro de Animais**: Registre cachorros, gatos e papagaios no sistema
- **Serviços de Banho**: Realize o serviço de banho para qualquer animal
- **Serviços de Tosa**: Disponível apenas para animais peludos (cachorros e gatos)
- **Controle de Liberação**: Animal só pode ser removido após completar todos os serviços necessários
- **Persistência de Dados**: Todos os dados são salvos automaticamente em arquivo CSV
- **Interface Gráfica Intuitiva**: Interface moderna e responsiva desenvolvida com Swing

## 🏗️ Arquitetura

O projeto segue o padrão arquitetural **MVC (Model-View-Controller)** com camada **DAO**:

```
Pet-shop-poo/
├── Model/              # Camada de modelo (regras de negócio)
│   ├── Animal.java     # Classe abstrata base
│   ├── Cachorro.java   # Implementação concreta
│   ├── Gato.java       # Implementação concreta
│   ├── Papagaio.java   # Implementação concreta
│   └── Peludos.java    # Interface para animais que podem ser tosados
├── View/               # Camada de apresentação
│   ├── View.java       # Interface gráfica principal
│   └── View.form       # Layout da interface
├── Controller/         # Camada de controle
│   └── ControleAnimal.java # Lógica de negócio e validações
├── DAO/                # Camada de acesso a dados
│   └── AnimalDAO.java  # Persistência e operações CRUD
├── docs/               # Documentação
│   ├── TP2.pdf         # Especificação do trabalho
│   └── docs.md         # Documentação técnica
├── animais.csv         # Arquivo de persistência
└── README.md           # Este arquivo
```

## 🎯 Conceitos de POO Aplicados

- **Abstração**: Classe `Animal` como modelo abstrato
- **Encapsulamento**: Atributos privados com getters e setters
- **Herança**: Classes `Cachorro`, `Gato` e `Papagaio` herdam de `Animal`
- **Polimorfismo**: Método `banho()` implementado de forma diferente em cada classe
- **Interface**: `Peludos` define comportamento para tosa
- **Composição**: `ControleAnimal` gerencia coleção de objetos `Animal`

## 🚀 Como Executar

### Pré-requisitos

- Java Development Kit (JDK) 8 ou superior
- IDE Java (Eclipse, IntelliJ IDEA, NetBeans ou VS Code com extensão Java)

### Passos para Executar

1. Clone o repositório:

```bash
git clone https://github.com/Lucal22/Pet-shop-poo.git
```

2. Abra o projeto na sua IDE preferida

3. Compile e execute a classe `View.java`:

```bash
cd Pet-shop-poo
javac View/View.java
java View.View
```

Ou execute diretamente pela IDE através do método `main()` na classe `View`.

## 💻 Uso do Sistema

1. **Adicionar Animal**:

   - Digite o nome do animal no campo "Nome"
   - Selecione o tipo (Cachorro, Gato ou Papagaio)
   - Clique em "Criar"

2. **Realizar Serviços**:

   - Clique no botão "Banho" para dar banho no animal
   - Clique no botão "Tosa" para tosar (apenas cachorros e gatos)

3. **Remover Animal**:
   - O botão "Remover" só é habilitado quando o animal completar todos os serviços
   - Cachorros e Gatos: precisam de banho E tosa
   - Papagaios: precisam apenas de banho

## 📊 Estrutura de Dados

Os dados são persistidos em formato CSV com a seguinte estrutura:

```csv
ID,Nome,Especie,Banho,Tosa,Liberado
1,Rex,Cachorro,true,true,true
2,Mimi,Gato,true,false,false
3,Loro,Papagaio,true,n/a,true
```

## 🎨 Interface

A interface gráfica apresenta:

- Cards individuais para cada animal cadastrado
- Cores indicativas para diferentes ações (azul para serviços, vermelho para remoção)
- Botões habilitados/desabilitados conforme status dos serviços
- Design moderno e responsivo

## 📚 Documentação Técnica

Para informações detalhadas sobre a implementação dos requisitos do trabalho e decisões de design, consulte a [documentação técnica](docs/docs.md).

## 👨‍💻 Autor

**Luís Carlos** - [Lucal22](https://github.com/Lucal22)

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais como parte da disciplina de POO I do IFMG.

---

**Instituto Federal de Minas Gerais (IFMG)**  
_Programação Orientada a Objetos I_  
_Trabalho Prático 2 - 2025_
