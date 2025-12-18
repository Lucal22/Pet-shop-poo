package View;

import Controller.ControleAnimal;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class View {
    private JPanel PainelGeral;
    private JPanel PainelTopo;
    private JPanel PainelCentral;
    private JPanel CriaAnimal;
    private JTextField NomeAnimal;
    private JButton Criar;
    private JLabel Nome;
    private JComboBox<String> SelectAnimal;
    private JPanel Titulo;
    private JLabel TituloString;
    private JComboBox<String> SelectPessoa;
    private ControleAnimal c;

    private static final Color BG_GERAL = new Color(245, 246, 250);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(52, 152, 219);
    private static final Color DANGER = new Color(231, 76, 60);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Dimension CARD_SIZE = new Dimension(180, 150);

    View() {
        PainelGeral.setPreferredSize(new Dimension(600, 400));
        PainelGeral.setBackground(BG_GERAL);

        PainelCentral.setLayout(new FlowLayout(
                FlowLayout.LEFT,
                10,
                10
        ));
        PainelCentral.setBackground(BG_GERAL);

        PainelTopo.setBackground(PRIMARY);
        TituloString.setOpaque(false);
        TituloString.setForeground(PRIMARY);
        TituloString.setFont(new Font("Segoe UI", Font.BOLD, 18));

        NomeAnimal.setPreferredSize(new Dimension(200, 28));
        NomeAnimal.setMaximumSize(new Dimension(200, 28));
        NomeAnimal.setMinimumSize(new Dimension(200, 28));
        NomeAnimal.setOpaque(true);
        NomeAnimal.setBackground(Color.WHITE);
        NomeAnimal.setForeground(Color.BLACK);
        NomeAnimal.setCaretColor(Color.BLACK);

        NomeAnimal.putClientProperty("caretWidth", 1);


        NomeAnimal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        NomeAnimal.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        NomeAnimal.setEditable(true);
        NomeAnimal.setEnabled(true);

        SelectAnimal.addItem("Cachorro");
        SelectAnimal.addItem("Gato");
        SelectAnimal.addItem("Papagaio");

        c = new ControleAnimal(this);

        for (Animal a : c.getAllAnimals()) {
            PainelCentral.add(criarCard(a));
        }

        init();
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_NORMAL);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private JPanel criarCard(Animal a) {
        JPanel card = getJPanel();

        JLabel label = new JLabel(a.getNome());
        label.setFont(FONT_TITULO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel especie = new JLabel(a.getEspecie());
        especie.setFont(FONT_NORMAL);
        especie.setForeground(Color.GRAY);
        especie.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton banho = criarBotao("Banho", PRIMARY);
        banho.setEnabled(!a.getBanho());
        banho.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton tosa = criarBotao("Tosa", PRIMARY);
        tosa.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (a instanceof Peludos) {
            tosa.setEnabled(!((Peludos) a).getTosa());
        }

        JButton remove = criarBotao("Remover", DANGER);
        remove.setEnabled(a.getLiberado());
        remove.setAlignmentX(Component.CENTER_ALIGNMENT);

        banho.addActionListener(e -> {
            a.banho();
            c.updateAnimal(a);
            banho.setEnabled(!a.getBanho());
            remove.setEnabled(a.getLiberado());
        });

        if (a instanceof Peludos) {
            tosa.addActionListener(e -> {
                ((Peludos) a).tosa();
                c.updateAnimal(a);
                tosa.setEnabled(!((Peludos) a).getTosa());
                remove.setEnabled(a.getLiberado());
            });
        }

        remove.addActionListener(e -> {
            c.removeAnimal(a.getPetID());
            PainelCentral.remove(card);
            PainelCentral.revalidate();
            PainelCentral.repaint();
        });

        card.add(label);
        card.add(especie);
        card.add(Box.createVerticalStrut(5));
        card.add(banho);

        if (a instanceof Peludos) {
            card.add(tosa);
        }

        card.add(Box.createVerticalStrut(5));
        card.add(remove);

        return card;
    }

    private static JPanel getJPanel() {
        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(CARD_SIZE);
        card.setMinimumSize(CARD_SIZE);
        card.setMaximumSize(CARD_SIZE);
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }


    private Animal criaObjetoAnimal(String especie, String nome) {
        return switch (especie) {
            case "Cachorro" -> new Cachorro(nome);
            case "Gato" -> new Gato(nome);
            case "Papagaio" -> new Papagaio(nome);
            default -> throw new IllegalArgumentException("Animal inválido");
        };
    }
    private Animal criaObjetoAnimal(String especie) {
        return switch (especie) {
            case "Cachorro" -> new Cachorro();
            case "Gato" -> new Gato();
            case "Papagaio" -> new Papagaio();
            default -> throw new IllegalArgumentException("Animal inválido");
        };
    }

    private void init() {
        Criar.addActionListener(e -> {

            String nome = NomeAnimal.getText();
            String animal = (String) SelectAnimal.getSelectedItem();
            assert animal != null;

            Animal objectAnimal;


            if (nome == null || nome.trim().isEmpty()) {
                objectAnimal = criaObjetoAnimal(animal);
            }else{
                objectAnimal = criaObjetoAnimal(animal, nome);
            }


            if(c.addAnimal(objectAnimal)){

                JPanel card = criarCard(objectAnimal);
                PainelCentral.add(card);

                PainelCentral.revalidate();
                PainelCentral.repaint();

                NomeAnimal.setText("");
            }
        });
    }

    public JPanel getPainelGeral(){
        return PainelGeral;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JFrame frame = new JFrame("PetShop");
            frame.setContentPane(new View().getPainelGeral());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
}
}
