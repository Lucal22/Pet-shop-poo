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

**Métodos**:

- `abstract void banho()`: Método abstrato que deve ser implementado pelas subclasses
- Getters e Setters para todos os atributos
- Construtor que inicializa nome, espécie e define `liberado = false`

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

**Implementação**:

```java
public void banho() {
    System.out.println(getNome() + " tomou banho!");
    if(tosa) {
        setLiberado(true);  // Liberado apenas se já foi tosado
    }
    setBanho(true);
}

public void tosa() {
    System.out.println(getNome() + " foi tosado!");
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
- **Implementação**: Idêntica ao Cachorro (animais peludos compartilham comportamento)

**Regra de Negócio**: Gato só é liberado após banho E tosa.

---

##### `Papagaio`

- **Localização**: `Model/Papagaio.java`
- **Herança**: `extends Animal`
- **Diferencial**: NÃO implementa a interface `Peludos`

**Implementação**:

```java
public void banho() {
    System.out.println(getNome() + " tomou banho!");
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

### 3. Padrão MVC (Model-View-Controller)

#### 📁 Model (Modelo)

**Responsabilidade**: Lógica de negócio e estrutura de dados

**Classes**:

- `Animal.java` - Classe abstrata base
- `Cachorro.java`, `Gato.java`, `Papagaio.java` - Entidades concretas
- `Peludos.java` - Interface para comportamento de tosa

**Características**:

- ❌ Não conhece a View
- ❌ Não conhece o Controller
- ✅ Contém apenas regras de negócio
- ✅ Reutilizável e testável independentemente

---

#### 🎮 Controller (Controlador)

**Responsabilidade**: Gerenciamento de operações e persistência

**Classe**: `ControleAnimal.java`

**Métodos Principais**:

1. **`addCSV(Animal a)`**

   - Adiciona novo animal ao arquivo CSV
   - Gera ID automático incremental
   - Trata campos específicos de cada tipo

2. **`getAllCSV()`**

   - Lê todos os animais do arquivo
   - Recria objetos com base na espécie
   - Retorna ArrayList de animais

3. **`updateCSV(Animal animalAtualizado)`**

   - Atualiza dados de um animal existente
   - Mantém integridade do arquivo
   - Lida com BOM e espaços invisíveis

4. **`removerPorID(int idRemover)`**

   - Remove animal do sistema
   - Reescreve arquivo sem a linha removida

5. **`getUltimoID()`**
   - Retorna o último ID usado
   - Permite geração de IDs únicos

**Aplicação de POO**:

- **Encapsulamento**: Lógica de persistência isolada
- **Single Responsibility**: Única responsabilidade é gerenciar dados
- **Factory Pattern**: Método `criarAnimal()` cria objetos baseado em tipo

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

**Métodos Principais**:

1. **`criarCard(Animal a)`**

   - Cria card visual para cada animal
   - Botões dinâmicos baseados no tipo e estado
   - Event listeners para ações

2. **`criarBotao(String texto, Color cor)`**

   - Factory method para botões padronizados
   - Consistência visual

3. **`criaObjetoAnimal(String especie, String nome)`**
   - Factory method para criação de animais
   - Usa Switch Expression (Java 14+)

**Design Pattern Aplicado**:

- **Observer Pattern**: Event Listeners respondem a ações do usuário
- **Factory Method**: Criação de cards e botões padronizados

---

## Implementação dos Conceitos de POO

### 1. ✅ Abstração

**Classe `Animal`**:

```java
public abstract class Animal {
    // Atributos comuns
    private String nome;
    private int petID;

    // Método abstrato - contrato para subclasses
    public abstract void banho();
}
```

**Benefício**: Garante que todas as subclasses implementem o comportamento de banho, mas permite implementações específicas.

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

#### Polimorfismo de Sobrescrita

**Método `banho()` implementado diferentemente**:

```java
// Em Cachorro/Gato
public void banho() {
    if(tosa) {
        setLiberado(true);  // Só libera se já foi tosado
    }
    setBanho(true);
}

// Em Papagaio
public void banho() {
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

- Flexibilidade no código
- Facilita extensão do sistema
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
│ + banho(): void*        │
│ + getters/setters       │
└─────────────────────────┘
           △
           │
    ┌──────┼──────┐
    │      │      │
┌───▼────┐ │  ┌───▼────────┐
│Cachorro│ │  │  Papagaio  │
├────────┤ │  ├────────────┤
│- tosa  │ │  │            │
├────────┤ │  ├────────────┤
│+banho()│ │  │ + banho()  │
│+tosa() │ │  └────────────┘
└────────┘ │
     △     │
     │     │
┌────┴─────▼───┐
│   Peludos    │
│ <<interface>>│
├──────────────┤
│ + tosa()     │
│ + getTosa()  │
│ + setTosa(b) │
└──────────────┘
     △
     │
┌────▼────┐
│  Gato   │
├─────────┤
│ - tosa  │
├─────────┤
│+banho() │
│+tosa()  │
└─────────┘
```

---

## Persistência de Dados

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

### Operações de Persistência

#### 1. **Adicionar Animal**

```java
public void addCSV(Animal a) {
    a.setPetID(getUltimoID() + 1);  // Gera ID único

    Object tosa;
    if(a instanceof Cachorro || a instanceof Gato) {
        tosa = ((Peludos) a).getTosa();
    } else {
        tosa = "n/a";  // Papagaio não tem tosa
    }

    writer.append(dados...);  // Escreve no arquivo
}
```

#### 2. **Carregar Animais**

```java
public ArrayList<Animal> getAllCSV() {
    // Lê linha por linha
    // Recria objetos baseado na espécie
    Animal animal = criarAnimal(especie, nome);
    animal.setPetID(id);
    animal.setBanho(Boolean.parseBoolean(dados[3]));
    // ...
    return animais;
}
```

#### 3. **Atualizar Animal**

```java
public void updateCSV(Animal animalAtualizado) {
    // Lê todas as linhas
    // Substitui apenas a linha do animal atualizado
    // Reescreve o arquivo
}
```

#### 4. **Remover Animal**

```java
public void removerPorID(int idRemover) {
    // Lê todas as linhas exceto a que tem o ID
    // Reescreve o arquivo sem ela
}
```

### Tratamento de Problemas

**BOM (Byte Order Mark)**:

```java
linha = linha.replace("\uFEFF", "").trim();
```

**Linhas Vazias**:

```java
if (linha.isEmpty()) continue;
```

**Validação de Dados**:

```java
if (dados.length < 6) {
    linhas.add(linha);  // Mantém linha original
    continue;
}
```

---

## Interface Gráfica

### Design System

**Paleta de Cores**:

```java
private static final Color BG_GERAL = new Color(245, 246, 250);  // Cinza claro
private static final Color BG_CARD = Color.WHITE;                // Branco
private static final Color PRIMARY = new Color(52, 152, 219);    // Azul
private static final Color DANGER = new Color(231, 76, 60);      // Vermelho
private static final Color SUCCESS = new Color(46, 204, 113);    // Verde
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
    a.banho();                 // Executa serviço
    c.updateCSV(a);           // Atualiza CSV
    banho.setEnabled(false);   // Desabilita botão
    remove.setEnabled(a.getLiberado());  // Atualiza botão remover
});
```

**Botão Tosa**:

```java
tosa.addActionListener(e -> {
    ((Peludos) a).tosa();
    c.updateCSV(a);
    tosa.setEnabled(false);
    remove.setEnabled(a.getLiberado());
});
```

**Botão Remover**:

```java
remove.addActionListener(e -> {
    c.removerPorID(a.getPetID());  // Remove do CSV
    PainelCentral.remove(card);     // Remove do painel
    PainelCentral.revalidate();     // Atualiza layout
    PainelCentral.repaint();        // Redesenha
});
```

**Botão Criar**:

```java
Criar.addActionListener(e -> {
    String nome = NomeAnimal.getText();
    String animal = (String) SelectAnimal.getSelectedItem();

    if (nome == null || nome.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite o nome do animal!");
        return;
    }

    Animal objectAnimal = criaObjetoAnimal(animal, nome);
    c.addCSV(objectAnimal);

    JPanel card = criarCard(objectAnimal);
    PainelCentral.add(card);

    PainelCentral.revalidate();
    PainelCentral.repaint();

    NomeAnimal.setText("");  // Limpa campo
});
```

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
[Usuário preenche nome]
   ↓
[Usuário seleciona espécie]
   ↓
[Clica em "Criar"]
   ↓
[Validação do nome]
   ↓
[criaObjetoAnimal()] → Cria objeto apropriado
   ↓
[addCSV()] → Salva no arquivo
   ↓
[criarCard()] → Cria card visual
   ↓
[Adiciona ao painel]
   ↓
[Atualiza interface]
```

### 3. Realizar Serviço de Banho

```
[Usuário clica "Banho"]
   ↓
[Executa a.banho()]
   ↓
[Atualiza atributos do objeto]
   ↓
[Verifica se pode liberar]
   ↓
[updateCSV()] → Atualiza arquivo
   ↓
[Desabilita botão Banho]
   ↓
[Atualiza botão Remover se liberado]
```

### 4. Realizar Serviço de Tosa

```
[Usuário clica "Tosa"]
   ↓
[Verifica se é Peludo]
   ↓
[Executa ((Peludos) a).tosa()]
   ↓
[Atualiza atributos]
   ↓
[Verifica se pode liberar]
   ↓
[updateCSV()] → Atualiza arquivo
   ↓
[Desabilita botão Tosa]
   ↓
[Atualiza botão Remover se liberado]
```

### 5. Remover Animal

```
[Usuário clica "Remover"]
   ↓
[Verifica se está liberado]
   ↓
[removerPorID()] → Remove do CSV
   ↓
[Remove card do painel]
   ↓
[revalidate() + repaint()]
   ↓
[Interface atualizada]
```

---

## Decisões de Design

### 1. Por que MVC?

**Separação de Responsabilidades**:

- Model: Regras de negócio isoladas
- View: Interface independente da lógica
- Controller: Mediador entre camadas

**Benefícios**:

- ✅ Testabilidade: Cada camada pode ser testada independentemente
- ✅ Manutenibilidade: Mudanças isoladas não afetam outras camadas
- ✅ Reusabilidade: Model pode ser usado em outras interfaces

### 2. Por que CSV?

**Simplicidade**:

- Fácil de ler e editar manualmente
- Não requer banco de dados
- Portável entre sistemas

**Limitações Conhecidas**:

- ❌ Não suporta transações
- ❌ Performance limitada para grandes volumes
- ❌ Sem controle de concorrência

**Alternativa Futura**: Migrar para SQLite ou outro banco leve

### 3. Por que Interface `Peludos`?

**Segregação de Comportamento**:

- Nem todo animal precisa de tosa
- Interface define contrato claro
- Facilita adição de novos tipos peludos

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
✅ **Polimorfismo**: Comportamentos específicos por tipo  
✅ **Interface**: Segregação de responsabilidades com `Peludos`  
✅ **MVC**: Separação clara de responsabilidades  
✅ **Persistência**: Dados salvos em CSV  
✅ **GUI**: Interface gráfica funcional e intuitiva

O sistema está funcional, atende todos os requisitos do trabalho prático e serve como base sólida para futuras expansões.

---

**Desenvolvido por**: Lucaldev
**Disciplina**: Programação Orientada a Objetos I  
**Instituição**: Instituto Federal de Minas Gerais (IFMG)  
**Data**: Dezembro de 2025
