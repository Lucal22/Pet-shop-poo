# 📖 Documentação Técnica - Sistema Pet Shop

## Sumário

1. [Visão Geral](#visão-geral)
2. [Requisitos Implementados](#requisitos-implementados)
3. [Arquitetura do Sistema](#arquitetura-do-sistema)
4. [Implementação dos Conceitos de POO](#implementação-dos-conceitos-de-poo)
5. [Estrutura de Classes](#estrutura-de-classes)
6. [Persistência de Dados](#persistência-de-dados)
7. [Interface Gráfica](#interface-gráfica)
8. [Fluxo de Operações](#fluxo-de-operações)

---

## Visão Geral

O Sistema de Gerenciamento de Pet Shop é uma aplicação desktop desenvolvida em Java que implementa conceitos fundamentais de Programação Orientada a Objetos (POO) para gerenciar o atendimento de diferentes tipos de animais em um pet shop. O sistema oferece serviços de banho e tosa, com controle de estado dos serviços e persistência de dados.

### Tecnologias Utilizadas

- **Linguagem**: Java (JDK 8+)
- **Interface Gráfica**: Swing
- **Persistência**: Arquivo CSV
- **Padrão Arquitetural**: MVC (Model-View-Controller)

---

## Requisitos Implementados

### 1. Classes e Herança

#### ✅ Classe Abstrata `Animal`

- **Localização**: `Model/Animal.java`
- **Descrição**: Classe base abstrata que define os atributos e comportamentos comuns a todos os animais

**Atributos**:

```java
private String nome;        // Nome do animal
private int petID;         // Identificador único
private String especie;    // Tipo do animal (Cachorro, Gato, Papagaio)
private boolean banho;     // Status do serviço de banho
private boolean liberado;  // Status de liberação para remoção
```

**Construtores**:

- `Animal(String n, String e)`: Construtor principal que recebe nome e espécie
- `Animal(String e)`: Construtor sobrecarregado que define nome como "Desconhecido"

**Métodos**:

- `abstract void banho()`: Método abstrato que deve ser implementado pelas subclasses
- Getters e Setters para todos os atributos

**Aplicação de POO**:

- **Abstração**: Define o "contrato" que todas as subclasses devem seguir
- **Encapsulamento**: Atributos privados protegem os dados
- **Polimorfismo**: Método `banho()` é implementado diferentemente em cada subclasse

---

#### ✅ Classes Concretas

##### `Cachorro`

- **Localização**: `Model/Cachorro.java`
- **Herança**: `extends Animal implements Peludos`
- **Atributo adicional**: `private boolean tosa`

**Construtores**:

```java
public Cachorro(String n) {       // Com nome específico
    super(n, "Cachorro");
    tosa = false;
}

public Cachorro() {               // Sem nome (usa "Desconhecido")
    super("Cachorro");
    tosa = false;
}
```

**Implementação**:

```java
public void banho() {
    System.out.println("Cachorro " + getNome() + " tomou banho!");
    if(tosa) {
        setLiberado(true);  // Liberado apenas se já foi tosado
    }
    setBanho(true);
}

public void tosa() {
    System.out.println("Cachorro " + getNome() + " foi tosado!");
    if(getBanho()) {
        setLiberado(true);  // Liberado apenas se já tomou banho
    }
    tosa = true;
}
```

**Regra de Negócio**: Cachorro só é liberado após banho E tosa.

---

##### `Gato`

- **Localização**: `Model/Gato.java`
- **Herança**: `extends Animal implements Peludos`
- **Atributo adicional**: `private boolean tosa`

**Construtores**:

```java
public Gato(String n) {           // Com nome específico
    super(n, "Gato");
    tosa = false;
}

public Gato() {                   // Sem nome (usa "Desconhecido")
    super("Gato");
    tosa = false;
}
```

**Implementação**: Comportamento semelhante ao Cachorro (animais peludos compartilham lógica)

**Regra de Negócio**: Gato só é liberado após banho E tosa.

---

##### `Papagaio`

- **Localização**: `Model/Papagaio.java`
- **Herança**: `extends Animal`
- **Diferencial**: NÃO implementa a interface `Peludos`

**Construtores**:

```java
public Papagaio(String n) {       // Com nome específico
    super(n, "Papagaio");
}

public Papagaio() {               // Sem nome (usa "Desconhecido")
    super("Papagaio");
}
```

**Implementação**:

```java
public void banho() {
    System.out.println("Papagaio " + getNome() + " tomou banho");
    setBanho(true);
    setLiberado(true);  // Liberado imediatamente após o banho
}
```

**Regra de Negócio**: Papagaio só precisa de banho para ser liberado.

---

### 2. Interface `Peludos`

- **Localização**: `Model/Peludos.java`
- **Propósito**: Definir comportamento exclusivo para animais que podem ser tosados

**Métodos**:

```java
public interface Peludos {
    public void tosa();
    public boolean getTosa();
    public void setTosa(boolean b);
}
```

**Aplicação de POO**:

- **Contrato**: Garante que todos os animais peludos implementem os métodos de tosa
- **Separação de Responsabilidades**: Papagaio não implementa, pois não precisa de tosa
- **Polimorfismo de Interface**: Permite tratar cachorros e gatos genericamente

**Uso no Sistema**:

```java
// No ControllerAnimal
if(a instanceof Cachorro || a instanceof Gato) {
    tosa = ((Peludos) a).getTosa();
}

// Na View
if (a instanceof Cachorro || a instanceof Gato) {
    tosa.setEnabled(!((Peludos) a).getTosa());
}
```

---

### 3. Padrão MVC (Model-View-Controller) com DAO

O projeto implementa o padrão MVC com uma camada adicional DAO (Data Access Object) para separar completamente a lógica de persistência.

#### 📁 Model (Modelo)

**Responsabilidade**: Lógica de negócio e estrutura de dados

**Classes**:

- `Animal.java` - Classe abstrata base
- `Cachorro.java`, `Gato.java`, `Papagaio.java` - Entidades concretas
- `Peludos.java` - Interface para comportamento de tosa

**Características**:

- ❌ Não conhece a View
- ❌ Não conhece o Controller
- ❌ Não conhece o DAO
- ✅ Contém apenas regras de negócio
- ✅ Reutilizável e testável independentemente

---

#### 🎮 Controller (Controlador)

**Responsabilidade**: Lógica de negócio, validações e mediação entre View e DAO

**Classe**: `ControleAnimal.java`

**Atributos**:

- `private View v`: Referência à View para exibir mensagens
- `private final String caminho`: Caminho do arquivo CSV (mantido por compatibilidade)

**Construtor**:

```java
public ControleAnimal(View v) {
    this.v = v;
    AnimalDAO.criaArquivo();  // Inicializa arquivo CSV via DAO
}
```

**Métodos Principais**:

1. **`addAnimal(Animal a): boolean`**

   - **Validação**: Verifica se o nome inicia com letra usando regex `^\\p{L}.*`
   - Se válido: chama `AnimalDAO.addAnimal(a)` e retorna `true`
   - Se inválido: exibe mensagem de erro via `JOptionPane` e retorna `false`
   - **Responsabilidade**: Validação de dados antes de persistir

2. **`getAllAnimals(): ArrayList<Animal>`**

   - Delega ao DAO: `return AnimalDAO.getAllAnimals()`
   - Retorna lista de todos os animais carregados do CSV

3. **`updateAnimal(Animal animalAtualizado)`**

   - Delega ao DAO: `AnimalDAO.updateAnimal(animalAtualizado)`
   - Atualiza animal após serviços de banho/tosa

4. **`removeAnimal(int id)`**
   - Delega ao DAO: `AnimalDAO.removeAnimal(id)`
   - Remove animal do sistema

**Aplicação de POO**:

- **Encapsulamento**: Controller não conhece detalhes de persistência
- **Single Responsibility**: Apenas validação e orquestração
- **Delegation**: Delega operações CRUD para o DAO

---

#### 💾 DAO (Data Access Object)

**Responsabilidade**: Persistência de dados e operações CRUD no arquivo CSV

**Classe**: `AnimalDAO.java`

**Atributos**:

- `private static final String caminho = "animais.csv"`: Caminho do arquivo
- `private static ArrayList<Animal> animais`: Lista em memória (cache)

**Métodos Principais**:

1. **`criaArquivo()` (público estático)**

   - Inicializa o ArrayList `animais`
   - Cria arquivo CSV se não existir
   - Escreve cabeçalho: `ID,Nome,Especie,Banho,Tosa,Liberado`
   - Usa `BufferedWriter` com append mode

2. **`addAnimal(Animal a)` (público estático)**

   - Gera ID automático: `a.setPetID(getUltimoID() + 1)`
   - Adiciona à lista em memória: `animais.add(a)`
   - Verifica se implementa `Peludos` para campo tosa
   - Escreve linha no CSV com `BufferedWriter` (append)

3. **`getAllAnimals(): ArrayList<Animal>` (público estático)**

   - Limpa cache: `animais.clear()`
   - Lê arquivo CSV linha por linha com `BufferedReader`
   - Usa `criarAnimal()` factory para recriar objetos
   - Restaura estado de cada animal (ID, banho, tosa, liberado)
   - Retorna lista completa

4. **`updateAnimal(Animal animalAtualizado)` (público estático)**

   - Busca animal na lista por `petID`
   - Substitui objeto: `animais.set(i, animalAtualizado)`
   - Chama `updateCSV()` para sincronizar arquivo

5. **`removeAnimal(int idRemover)` (público estático)**

   - Busca e remove da lista por `petID`
   - Exibe mensagem: `"[Especie] [Nome] removido"`
   - Chama `updateCSV()` para sincronizar arquivo

6. **`updateCSV()` (privado estático)**

   - Reescreve arquivo completamente
   - Escreve cabeçalho + todas as linhas da lista
   - Usa `BufferedWriter` com `false` (sobrescrever)

7. **`criarAnimal(String tipo, String nome)` (privado estático)**

   - Factory method para criar instâncias
   - Usa switch expression para determinar tipo
   - Lança exceção se tipo inválido

8. **`getUltimoID()` (privado estático)**
   - Lê última linha do arquivo
   - Extrai e retorna o ID
   - Retorna 0 se arquivo vazio

**Vantagens do DAO**:

- ✅ Separação completa de responsabilidades
- ✅ Controller não conhece detalhes de persistência
- ✅ Fácil substituir CSV por banco de dados
- ✅ Métodos estáticos permitem acesso global
- ✅ Cache em memória para performance

---

#### 🖥️ View (Visão)

**Responsabilidade**: Interface gráfica e interação com usuário

**Classe**: `View.java`

**Componentes**:

- `JPanel PainelGeral` - Container principal
- `JPanel PainelTopo` - Área de cadastro
- `JPanel PainelCentral` - Grid de cards dos animais
- `JTextField NomeAnimal` - Input do nome
- `JComboBox SelectAnimal` - Seletor de espécie
- `JButton Criar` - Botão de cadastro

**Construtor**:

```java
View() {
    // ... configurações de layout e estilo ...

    c = new ControleAnimal(this);  // Passa referência da View

    for (Animal a : c.getAllAnimals()) {  // Carrega animais via Controller
        PainelCentral.add(criarCard(a));
    }

    init();  // Configura event listeners
}
```

**Métodos Principais**:

1. **`criarCard(Animal a): JPanel`**

   - Cria card visual para cada animal
   - Chama `getJPanel()` para criar estrutura base
   - Botões habilitados/desabilitados conforme estado
   - Usa `instanceof Peludos` para verificar se exibe botão tosa
   - Event listeners para interações

2. **`getJPanel(): JPanel` (privado estático)**

   - Factory method que retorna JPanel configurado
   - Define layout, tamanho, cores e bordas
   - Código extraído para reutilização

3. **`criarBotao(String texto, Color cor): JButton`**

   - Factory method para botões padronizados
   - Garante consistência visual

4. **`criaObjetoAnimal(String especie, String nome): Animal`**

   - Factory sobrecarregado com nome específico
   - Usa Switch Expression (Java 14+)

5. **`criaObjetoAnimal(String especie): Animal`**

   - Factory sobrecarregado SEM nome (usa construtor padrão)
   - Permite criar animais com nome "Desconhecido"

6. **`init()`**

   - Configura event listener do botão "Criar"
   - **Lógica**: Se campo nome vazio → usa construtor sem nome
   - **Validação**: Chama `c.addAnimal()` que retorna boolean
   - Só adiciona card à interface se validação passar

7. **`getPainelGeral(): JPanel`**
   - Retorna painel principal para ser exibido no JFrame

**Design Patterns Aplicados**:

- **Observer Pattern**: Event Listeners respondem a ações do usuário
- **Factory Method**: Criação de cards, botões e animais padronizados
- **MVC**: View conhece Controller, não conhece Model diretamente

---

## Implementação dos Conceitos de POO

### 1. ✅ Abstração

**Classe `Animal`**:

```java
public abstract class Animal {
    // Atributos comuns
    private String nome;
    private int petID;
    private String especie;
    private boolean banho;
    private boolean liberado;

    // Construtores sobrecarregados
    public Animal(String n, String e) {    // Com nome específico
        nome = n;
        liberado = false;
        especie = e;
    }

    public Animal(String e) {              // Nome padrão "Desconhecido"
        nome = "Desconhecido";
        liberado = false;
        especie = e;
    }

    // Método abstrato - contrato para subclasses
    public abstract void banho();
}
```

**Benefícios**:

- Garante que todas as subclasses implementem o comportamento de banho
- **Sobrecarga de Construtor**: Oferece flexibilidade na criação de objetos (com ou sem nome)
- Permite implementações específicas do método abstrato em cada subclasse

---

### 2. ✅ Encapsulamento

**Proteção de Dados**:

```java
private String nome;        // Atributo privado
private boolean liberado;   // Atributo privado

public String getNome() {   // Acesso controlado
    return nome;
}

public void setLiberado(boolean b) {  // Modificação controlada
    liberado = b;
}
```

**Benefícios**:

- Controle de acesso aos dados
- Validação nas modificações
- Manutenibilidade

---

### 3. ✅ Herança

**Hierarquia de Classes**:

```
        Animal (abstract)
           /    |    \
          /     |     \
    Cachorro  Gato  Papagaio
        \      /
         \    /
        Peludos (interface)
```

**Reutilização de Código**:

```java
// Cachorro herda todos os atributos e métodos de Animal
public class Cachorro extends Animal implements Peludos {
    // Adiciona apenas comportamento específico
    private boolean tosa;
}
```

**Benefícios**:

- Código DRY (Don't Repeat Yourself)
- Hierarquia lógica
- Facilita manutenção

---

### 4. ✅ Polimorfismo

#### Polimorfismo de Sobrecarga

**Construtores sobrecarregados em todas as classes**:

```java
// Classe Animal
public Animal(String n, String e) { ... }  // Com nome
public Animal(String e) { ... }            // Sem nome (padrão)

// Classe Cachorro
public Cachorro(String n) {                // Com nome específico
    super(n, "Cachorro");
    tosa = false;
}
public Cachorro() {                        // Sem nome
    super("Cachorro");
    tosa = false;
}
```

**Benefício**: Permite criar animais com ou sem nome, oferecendo flexibilidade ao desenvolvedor.

#### Polimorfismo de Sobrescrita

**Método `banho()` implementado diferentemente**:

```java
// Em Cachorro/Gato
public void banho() {
    System.out.println("Cachorro " + getNome() + " tomou banho!");
    if(tosa) {
        setLiberado(true);  // Só libera se já foi tosado
    }
    setBanho(true);
}

// Em Papagaio
public void banho() {
    System.out.println("Papagaio " + getNome() + " tomou banho");
    setBanho(true);
    setLiberado(true);  // Libera imediatamente
}
```

#### Polimorfismo de Interface

**Tratamento genérico de animais peludos**:

```java
if(a instanceof Cachorro || a instanceof Gato) {
    ((Peludos) a).tosa();  // Mesma interface, comportamento específico
}
```

**Benefícios**:

- **Sobrecarga**: Múltiplas formas de criar objetos
- **Sobrescrita**: Comportamentos específicos para cada tipo
- **Interface**: Tratamento genérico de tipos semelhantes
- Flexibilidade e extensibilidade do código
- Código mais expressivo

---

### 5. ✅ Interface

**`Peludos` define contrato**:

```java
public interface Peludos {
    public void tosa();
    public boolean getTosa();
    public void setTosa(boolean b);
}
```

**Implementação em Cachorro e Gato**:

```java
public class Cachorro extends Animal implements Peludos {
    private boolean tosa;

    @Override
    public void tosa() {
        System.out.println(getNome() + " foi tosado!");
        if(getBanho()) {
            setLiberado(true);
        }
        setTosa(true);
    }

    // Implementação dos outros métodos da interface
}
```

**Benefícios**:

- Segregação de comportamentos
- Múltiplas interfaces possíveis
- Contratos claros

---

## Estrutura de Classes

### Diagrama de Classes Simplificado

```
                    ┌─────────────────────────┐
                    │      Animal             │
                    │      <<abstract>>       │
                    ├─────────────────────────┤
                    │ - nome: String          │
                    │ - petID: int            │
                    │ - especie: String       │
                    │ - banho: boolean        │
                    │ - liberado: boolean     │
                    ├─────────────────────────┤
                    │ + Animal(n, e)          │
                    │ + Animal(e)             │  ← Sobrecarga
                    │ + banho(): void*        │
                    │ + getters/setters       │
                    └─────────────────────────┘
                               △
                               │ (herança)
                               │
          ┌────────────────────┼───────────────────┐
          │                    │                   │
    ┌─────▼──────┐      ┌──────▼─────┐      ┌──────▼────────┐
    │  Cachorro  │      │    Gato    │      │   Papagaio    │
    ├────────────┤      ├────────────┤      ├───────────────┤
    │ - tosa     │      │ - tosa     │      │               │
    ├────────────┤      ├────────────┤      ├───────────────┤
    │ +Cachorro()│      │ + Gato()   │      │ + Papagaio()  │
    │+Cachorro(n)│      │ + Gato(n)  │      │+ Papagaio(n)  │
    │ + banho()  │      │ + banho()  │      │ + banho()     │
    │ + tosa()   │      │ + tosa()   │      └───────────────┘
    └────────────┘      └────────────┘
          △                    △
          │                    │
          │  (implementa)      │ (implementa)
          │                    │
          └──────────┬─────────┘
                     │
              ┌──────▼──────────┐
              │    Peludos      │
              │  <<interface>>  │
              ├─────────────────┤
              │ + tosa()        │
              │ + getTosa()     │
              │ + setTosa(b)    │
              └─────────────────┘
```

**Legenda**:

- **△** (seta vazia): Herança (extends)
- **△** (seta tracejada): Implementação de interface (implements)
- **Papagaio** não implementa `Peludos` pois não possui tosa

---

## Persistência de Dados

### Arquitetura de Persistência

O sistema utiliza o padrão **DAO (Data Access Object)** para separar a lógica de persistência:

```
View → Controller → DAO → CSV File
  ↑        ↓          ↓
  └────────┴──────────┘
   (validação e fluxo)
```

- **View**: Captura dados do usuário
- **Controller**: Valida dados e orquestra operações
- **DAO**: Responsável exclusivo por operações CRUD no CSV
- **CSV**: Arquivo de persistência

### Formato CSV

**Estrutura do Arquivo `animais.csv`**:

```csv
ID,Nome,Especie,Banho,Tosa,Liberado
1,Rex,Cachorro,true,true,true
2,Mimi,Gato,false,false,false
3,Loro,Papagaio,true,n/a,true
```

**Campos**:

- `ID`: Identificador único (auto-incremento)
- `Nome`: Nome do animal
- `Especie`: Tipo (Cachorro, Gato, Papagaio)
- `Banho`: Status do serviço (true/false)
- `Tosa`: Status da tosa (true/false/n/a para papagaios)
- `Liberado`: Se pode ser removido (true/false)

### Operações de Persistência (AnimalDAO)

**Estratégia**: ArrayList em memória sincronizado com arquivo CSV

#### 1. **Inicializar Sistema**

```java
public static void criaArquivo() {
    animais = new ArrayList<>();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
        if (new File(caminho).length() == 0) {
            writer.write("ID,Nome,Especie,Banho,Tosa,Liberado\n");
        }
    }
}
```

- Chamado no construtor do Controller
- Cria arquivo se não existir
- Inicializa ArrayList

#### 2. **Adicionar Animal**

```java
public static void addAnimal(Animal a) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
        a.setPetID(getUltimoID() + 1);  // Gera ID único
        animais.add(a);                 // Adiciona ao cache

        Object tosa;
        if(a instanceof Peludos) {      // Verifica interface, não classes
            tosa = ((Peludos) a).getTosa();
        } else {
            tosa = "n/a";
        }

        // Escreve no arquivo (append mode)
        writer.write(a.getPetID() + "," + a.getNome() + "," +
                     a.getEspecie() + "," + a.getBanho() + "," +
                     tosa + "," + a.getLiberado() + "\n");
    }
}
```

- **Melhoria**: Usa `instanceof Peludos` em vez de verificar classes específicas
- Adiciona ao cache E ao arquivo simultaneamente

#### 3. **Carregar Animais**

```java
public static ArrayList<Animal> getAllAnimals() {
    animais.clear();  // Limpa cache

    try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
        String linha;
        boolean primeiraLinha = true;

        while ((linha = reader.readLine()) != null) {
            if (primeiraLinha) {
                primeiraLinha = false;
                continue;  // Pula cabeçalho
            }

            String[] dados = linha.split(",");

            // Factory method recria objetos
            Animal animal = criarAnimal(dados[2], dados[1]);
            animal.setPetID(Integer.parseInt(dados[0]));
            animal.setBanho(Boolean.parseBoolean(dados[3]));
            animal.setLiberado(Boolean.parseBoolean(dados[5]));

            if(animal instanceof Peludos) {
                ((Peludos) animal).setTosa(Boolean.parseBoolean(dados[4]));
            }

            animais.add(animal);  // Popula cache
        }
    }
    return animais;
}
```

- Chamado uma vez na inicialização da View
- Reconstrói cache a partir do arquivo

#### 4. **Atualizar Animal**

```java
public static void updateAnimal(Animal animalAtualizado) {
    // Atualiza no cache
    for(int i = 0; i < animais.size(); i++) {
        if(animais.get(i).getPetID() == animalAtualizado.getPetID()) {
            animais.set(i, animalAtualizado);
            break;
        }
    }
    updateCSV();  // Sincroniza com arquivo
}
```

- Chamado após banho/tosa
- Atualiza cache e sincroniza arquivo

#### 5. **Remover Animal**

```java
public static void removeAnimal(int idRemover) {
    for(int i = 0; i < animais.size(); i++) {
        if(animais.get(i).getPetID() == idRemover) {
            System.out.println(animais.get(i).getEspecie() + " " +
                             animais.get(i).getNome() + " removido");
            animais.remove(i);
            break;
        }
    }
    updateCSV();  // Sincroniza com arquivo
}
```

- Exibe mensagem com espécie e nome
- Remove do cache e sincroniza

#### 6. **Sincronizar CSV (privado)**

```java
private static void updateCSV() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, false))) {
        // Escreve cabeçalho
        writer.write("ID,Nome,Especie,Banho,Tosa,Liberado\n");

        // Escreve todos os animais do cache
        for (Animal a : animais) {
            Object tosa;
            if(a instanceof Peludos) {
                tosa = ((Peludos) a).getTosa();
            } else {
                tosa = "n/a";
            }

            writer.write(a.getPetID() + "," + a.getNome() + "," +
                        a.getEspecie() + "," + a.getBanho() + "," +
                        tosa + "," + a.getLiberado() + "\n");
        }
    }
}
```

- Reescreve arquivo completamente (modo sobrescrever)
- Fonte única de verdade: cache em memória

### Vantagens da Arquitetura com DAO

**Separação de Responsabilidades**:

- ✅ Controller não conhece detalhes de CSV
- ✅ Fácil trocar CSV por banco de dados (só mudar DAO)
- ✅ Código mais testável e manutenível

**Métodos Estáticos**:

- ✅ Acesso global ao cache de animais
- ✅ Não precisa instanciar DAO
- ✅ Compartilhamento de estado entre classes

**ArrayList em Memória (Cache)**:
**ArrayList em Memória (Cache)**:

- ✅ Operações de busca e atualização mais rápidas
- ✅ Menos leituras/escritas no arquivo
- ✅ Código mais limpo e manutenível
- ✅ Facilita operações em lote

**BufferedWriter/BufferedReader**:

- ✅ Leitura e escrita eficiente com buffer
- ✅ Melhor performance para arquivos grandes
- ✅ Gerenciamento automático de recursos (try-with-resources)

**Uso de instanceof Peludos**:

- ✅ Mais genérico que verificar `Cachorro || Gato`
- ✅ Facilita adição de novos animais peludos (ex: Coelho)
- ✅ Segue princípio da interface

---

## Interface Gráfica

### Design System

**Paleta de Cores**:

```java
private static final Color BG_GERAL = new Color(245, 246, 250);  // Cinza claro
private static final Color BG_CARD = Color.WHITE;                // Branco
private static final Color PRIMARY = new Color(52, 152, 219);    // Azul
private static final Color DANGER = new Color(231, 76, 60);      // Vermelho
```

**Tipografia**:

```java
private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 14);
private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
```

**Dimensões**:

```java
private static final Dimension CARD_SIZE = new Dimension(180, 150);
```

### Componentes

#### Card de Animal

Cada animal é representado por um card contendo:

- **Label Nome**: Nome do animal (negrito)
- **Label Espécie**: Tipo do animal (cinza)
- **Botão Banho**: Azul, desabilitado após uso
- **Botão Tosa**: Azul, apenas para peludos, desabilitado após uso
- **Botão Remover**: Vermelho, habilitado apenas quando liberado

**Lógica de Habilitação**:

```java
banho.setEnabled(!a.getBanho());  // Desabilita se já tomou banho

if (a instanceof Cachorro || a instanceof Gato) {
    tosa.setEnabled(!((Peludos) a).getTosa());  // Desabilita se já foi tosado
}

remove.setEnabled(a.getLiberado());  // Só habilita se estiver liberado
```

#### Área de Cadastro

- **TextField**: Input do nome (fundo branco, borda cinza)
- **ComboBox**: Seleção da espécie (Cachorro, Gato, Papagaio)
- **Botão Criar**: Adiciona novo animal

### Layout

**FlowLayout no Painel Central**:

```java
PainelCentral.setLayout(new FlowLayout(
    FlowLayout.LEFT,  // Alinhamento à esquerda
    10,               // Gap horizontal
    10                // Gap vertical
));
```

**BoxLayout nos Cards**:

```java
card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
```

### Eventos

**Botão Banho**:

```java
banho.addActionListener(e -> {
    a.banho();                          // Executa serviço no Model
    c.updateAnimal(a);                  // Controller → DAO → CSV
    banho.setEnabled(false);            // Desabilita botão
    remove.setEnabled(a.getLiberado()); // Atualiza botão remover
});
```

- Chama método do Model
- Delega atualização ao Controller (que usa DAO)

**Botão Tosa**:

```java
if (a instanceof Peludos) {
    tosa.addActionListener(e -> {
        ((Peludos) a).tosa();              // Executa tosa no Model
        c.updateAnimal(a);                 // Controller → DAO → CSV
        tosa.setEnabled(false);             // Desabilita botão
        remove.setEnabled(a.getLiberado()); // Atualiza botão remover
    });
}
```

- Verifica interface `Peludos` antes de criar listener
- Delega atualização ao Controller

**Botão Remover**:

```java
remove.addActionListener(e -> {
    c.removeAnimal(a.getPetID());       // Controller → DAO → remove do CSV
    PainelCentral.remove(card);         // Remove card da interface
    PainelCentral.revalidate();         // Atualiza layout
    PainelCentral.repaint();            // Redesenha
});
```

- Remove primeiro da persistência (via Controller/DAO)
- Depois remove da interface

**Botão Criar** (atualizado):

```java
Criar.addActionListener(e -> {
    String nome = NomeAnimal.getText();
    String animal = (String) SelectAnimal.getSelectedItem();
    assert animal != null;

    Animal objectAnimal;

    // Sobrecarga de construtor
    if (nome == null || nome.trim().isEmpty()) {
        objectAnimal = criaObjetoAnimal(animal);      // Sem nome → "Desconhecido"
    } else {
        objectAnimal = criaObjetoAnimal(animal, nome); // Com nome específico
    }

    // Validação no Controller
    if(c.addAnimal(objectAnimal)) {  // Retorna boolean
        JPanel card = criarCard(objectAnimal);
        PainelCentral.add(card);
        PainelCentral.revalidate();
        PainelCentral.repaint();
        NomeAnimal.setText("");  // Limpa campo
    }
    // Se falhar validação, mensagem já foi exibida pelo Controller
});
```

- **Novidade 1**: Usa sobrecarga de `criaObjetoAnimal`
- **Novidade 2**: Controller valida e retorna boolean
- **Novidade 3**: Só adiciona card se validação passar

---

## Fluxo de Operações

### 1. Inicialização do Sistema

```
[Main] → [View Constructor]
   ↓
[ControleAnimal Constructor]
   ↓
[Verifica/Cria animais.csv]
   ↓
[getAllCSV()]
   ↓
[Cria cards para animais existentes]
   ↓
[Exibe Interface]
```

### 2. Cadastro de Novo Animal

```
### 1. Inicialização do Sistema

```

[Main → View Constructor]
↓
[new ControleAnimal(this)]
↓
[AnimalDAO.criaArquivo()] → Inicializa CSV e ArrayList
↓
[c.getAllAnimals()] → Controller delega ao DAO
↓
[AnimalDAO.getAllAnimals()] → Lê CSV e popula cache
↓
[for each animal → criarCard()] → Cria cards visuais
↓
[Exibe Interface]

```

### 2. Cadastro de Novo Animal

```

[Usuário preenche nome (ou deixa vazio)]
↓
[Usuário seleciona espécie]
↓
[Clica em "Criar"]
↓
[View verifica se nome está vazio]
↓ ↓
[Vazio] [Preenchido]
↓ ↓
[criaObjetoAnimal(especie)] [criaObjetoAnimal(especie, nome)]
↓ ↓
[Animal com "Desconhecido"] [Animal com nome específico]
└───────┬───────┘
↓
[c.addAnimal(animal)] → Controller valida
↓
[Validação: nome inicia com letra?]
↓ ↓
[SIM] [NÃO]
↓ ↓
[AnimalDAO.addAnimal()] [JOptionPane erro + return false]
↓
[Adiciona ao cache + escreve no CSV]
↓
[View recebe true → cria card e adiciona ao painel]
↓
[Interface atualizada]

```

### 3. Realizar Serviço de Banho

```

[Usuário clica "Banho"]
↓
[a.banho()] → Executa no Model
↓
[Atualiza atributos (setBanho, setLiberado)]
↓
[c.updateAnimal(a)] → Controller delega ao DAO
↓
[AnimalDAO.updateAnimal(a)]
↓
[Busca no cache por petID e substitui]
↓
[updateCSV()] → Reescreve arquivo completo
↓
[View: desabilita botão Banho]
↓
[View: atualiza estado botão Remover]

```

### 4. Realizar Serviço de Tosa

```

[Usuário clica "Tosa"]
↓
[((Peludos) a).tosa()] → Executa no Model
↓
[Atualiza atributos (setTosa, setLiberado)]
↓
[c.updateAnimal(a)] → Controller delega ao DAO
↓
[AnimalDAO.updateAnimal(a)]
↓
[Busca no cache por petID e substitui]
↓
[updateCSV()] → Reescreve arquivo completo
↓
[View: desabilita botão Tosa]
↓
[View: atualiza estado botão Remover]

```

### 5. Remover Animal

```

[Usuário clica "Remover"]
↓
[Botão só está habilitado se liberado = true]
↓
[c.removeAnimal(petID)] → Controller delega ao DAO
↓
[AnimalDAO.removeAnimal(id)]
↓
[Busca no cache e remove (animais.remove(i))]
↓
[System.out: "[Especie] [Nome] removido"]
↓
[updateCSV()] → Reescreve arquivo sem o animal
↓
[View: remove card do painel]
↓
[View: revalidate() + repaint()]
↓
[Interface atualizada]

```

---

## Decisões de Design

### 1. Por que MVC + DAO?

**Separação em Camadas**:
- **Model**: Apenas regras de negócio (banho, tosa, liberação)
- **View**: Apenas interface e eventos
- **Controller**: Validação e orquestração
- **DAO**: Apenas persistência em CSV

**Benefícios**:
- ✅ Testabilidade: Cada camada testável independentemente
- ✅ Manutenibilidade: Mudanças isoladas não afetam outras camadas
- ✅ Reusabilidade: Model pode ser usado com outras interfaces
- ✅ Extensibilidade: Fácil trocar CSV por banco de dados

**Fluxo de Dados**:
```

View → Controller (valida) → DAO (persiste) → CSV
**Fluxo de Dados**:

```
View → Controller (valida) → DAO (persiste) → CSV
View ← Controller (retorna) ← DAO (carrega) ← CSV
```

### 2. Por que DAO Separado do Controller?

**Antes** (Controller fazia tudo):

- ❌ Controller conhecia detalhes do CSV
- ❌ Misturava validação com persistência
- ❌ Difícil trocar mecanismo de persistência

**Depois** (DAO separado):

- ✅ Controller só valida e orquestra
- ✅ DAO encapsula 100% da persistência
- ✅ Trocar CSV por DB: só mudar DAO
- ✅ Código mais limpo e testável

### 3. Por que CSV?

**Simplicidade**:

- Fácil de ler e editar manualmente
- Não requer banco de dados
- Portável entre sistemas

**Limitações Conhecidas**:

- ❌ Não suporta transações
- ❌ Performance limitada para grandes volumes
- ❌ Sem controle de concorrência

**Alternativa Futura**: Migrar para SQLite (só mudar AnimalDAO)

### 4. Por que Interface `Peludos`?

**Segregação de Comportamento**:

- Nem todo animal precisa de tosa
- Interface define contrato claro
- Facilita adição de novos tipos peludos

**Uso de `instanceof Peludos`**:

- Mais genérico que `instanceof Cachorro || instanceof Gato`
- Se criar `Coelho implements Peludos`, código já funciona
- Segue princípio "Programe para interface, não implementação"

**Exemplo de Extensão**:

```java
public class Coelho extends Animal implements Peludos {
    // Facilmente extensível
}
```

## Conclusão

Este projeto demonstra a aplicação prática dos principais conceitos de Programação Orientada a Objetos:

✅ **Abstração**: Classe `Animal` define modelo abstrato  
✅ **Encapsulamento**: Atributos privados com acesso controlado  
✅ **Herança**: Hierarquia de classes bem definida  
✅ **Abstração**: Classe `Animal` define modelo abstrato  
✅ **Encapsulamento**: Atributos privados com acesso controlado  
✅ **Herança**: Hierarquia de classes bem definida  
✅ **Polimorfismo (Sobrecarga)**: Construtores sobrecarregados em todas as classes  
✅ **Polimorfismo (Sobrescrita)**: Método `banho()` implementado especificamente por tipo  
✅ **Interface**: Segregação de responsabilidades com `Peludos`  
✅ **MVC + DAO**: Separação clara de responsabilidades em camadas  
✅ **Persistência**: Dados salvos em CSV via DAO com cache em memória  
✅ **Validação**: Controller valida dados antes de persistir  
✅ **GUI**: Interface gráfica funcional e intuitiva

### Destaques da Implementação

**Arquitetura em Camadas (MVC + DAO)**:

- **Model**: Apenas lógica de negócio (Animal, Cachorro, Gato, Papagaio, Peludos)
- **View**: Interface Swing com event listeners e validação visual
- **Controller**: Validação de dados (regex para nome) e orquestração
- **DAO**: Camada dedicada à persistência (CRUD no CSV)
- **Benefício**: Fácil manutenção e possibilidade de trocar CSV por banco de dados

**Sobrecarga de Construtor**:

- Todas as classes possuem construtores com e sem nome
- Permite criar animais com nome "Desconhecido" se campo vazio
- View usa factory methods `criaObjetoAnimal(especie)` e `criaObjetoAnimal(especie, nome)`

**Validação no Controller**:

- Método `addAnimal()` retorna boolean (sucesso/falha)
- Valida se nome inicia com letra usando regex `^\\p{L}.*`
- Exibe mensagem de erro via `JOptionPane` se inválido
- View só adiciona card se validação passar

**Sistema de Persistência (DAO)**:

- ArrayList estático em memória (cache compartilhado)
- Métodos estáticos para acesso global
- `instanceof Peludos` em vez de verificar classes específicas
- Sincronização automática entre cache e CSV

**Uso Avançado de instanceof**:

- `instanceof Peludos` permite extensibilidade
- Se criar `Coelho implements Peludos`, código já funciona
- Segue princípio "Programe para interface"

O sistema está funcional, atende todos os requisitos do trabalho prático, implementa boas práticas de POO e arquitetura, e serve como base sólida para futuras expansões.

---

**Desenvolvido por**: Lucas  
**Disciplina**: Programação Orientada a Objetos I  
**Instituição**: Instituto Federal de Minas Gerais (IFMG)  
**Data**: Dezembro de 2025
