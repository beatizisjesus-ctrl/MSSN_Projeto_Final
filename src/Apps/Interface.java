package Apps;

import javax.swing.*;

import globais.ProcessingSetup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {
    private JFrame frame;
    private static Interface instance;

    public Interface() {
        if (instance == null) {
            instance = this;
            mostrarMenuInicial();
        }
    }

    public void mostrarMenuInicial() {
        frame = new JFrame("OnceanApps");
        frame.setSize(550, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setLocationRelativeTo(null);
        
        BackgroundPanel background = new BackgroundPanel("/mar.jpg");
        frame.setContentPane(background);

        //TITULO
        JLabel titulo = new JLabel("OceanApps", JLabel.CENTER);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 40));
        titulo.setForeground(new Color(0, 105, 180));
        titulo.setOpaque(false);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // 50 px de espaço em cima
        background.add(titulo, BorderLayout.NORTH);

        
        
        //BOTOES
        
        JPanel botoesPanel = new JPanel();
        botoesPanel.setOpaque(false);
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.Y_AXIS));

        JButton btn1 = new JButton("Ocean Game");
        JButton btn2 = new JButton("Life in the Ocean");
        
        btn1.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn2.setFont(new Font("Tahoma", Font.BOLD, 13));

        //tamanho do sbotoes
        btn1.setMaximumSize(new Dimension(200, 50));
        btn2.setMaximumSize(new Dimension(200, 50));
        
        //contorno branco
        btn1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        btn2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));

        btn1.setFocusPainted(false);//para tirar o retangulo que ficava a volta das letras
        
        //cor das letras e do hover
        Color corNormal = new Color(0, 180, 255); // azul oceânico
        Color corHover = new Color(0, 128, 255);  // azul mais claro

        btn1.setBackground(corNormal);
        btn2.setBackground(corNormal);
        btn1.setForeground(Color.WHITE);
        btn2.setForeground(Color.WHITE);
        
        btn1.setAlignmentX(Component.CENTER_ALIGNMENT); //no eixo do x, poe no meio
        btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
    
        //hover
        btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn1.setBackground(corHover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn1.setBackground(corNormal);
            }
        });
        
        
        btn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn2.setBackground(corHover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn2.setBackground(corNormal);
            }
        });
        
        
        //Açoes dos botoes
        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                ProcessingSetup.startApp(0);
            }
        });

        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                ProcessingSetup.startApp(1);
            }
        });

        botoesPanel.add(Box.createVerticalStrut(100)); // espaço superior
        botoesPanel.add(btn1);
        botoesPanel.add(Box.createVerticalStrut(70)); // espaço entre botões
        botoesPanel.add(btn2);
        background.add(botoesPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        
        
    }
    
    public void mostrarMenu() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        abrirMenu(); 
    }
    
    public static void abrirMenu() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (instance == null) {
                new Interface();
            } else {
                instance.mostrarMenu();
            }
        });
    }
}