package Ventanas;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;



import BaseDatos.BaseDatos;
import Clases.Cliente;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class VentanaInicioSesion2 extends JFrame {

	
	private JPanel contentPane;
	private JPanel panelSur;
	private JPanel panelCentro;
	private JLabel lblNombre;
	private JTextField textNombre;
	private JLabel lblContrasenia;
	private JPasswordField textContrasenia;
	private static Logger logger = Logger.getLogger( "VentanaInicioSesion2" );
	private JButton btnInicioSesion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicioSesion2 frame = new VentanaInicioSesion2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaInicioSesion2() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaPrincipal.class.getResource("/LOGO/logo_small_icon_only_inverted.png")));
		BaseDatos.initBD("Basedatos.db");
		BaseDatos.crearTablas();
		BaseDatos.closeBD();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelSur = new JPanel();
		contentPane.add(panelSur, BorderLayout.SOUTH);
		
		btnInicioSesion = new JButton("Iniciar sesión");
		panelSur.add(btnInicioSesion);
		
		panelCentro = new JPanel();
		contentPane.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblNombre = new JLabel("Introduce tu nombre:");
		panelCentro.add(lblNombre);
		
		textNombre = new JTextField();
		panelCentro.add(textNombre);
		textNombre.setColumns(10);
		
		lblContrasenia = new JLabel("Introduce la contraseña:");
		panelCentro.add(lblContrasenia);
		
		textContrasenia = new JPasswordField();
		panelCentro.add(textContrasenia);
		textContrasenia.setColumns(10);
		
		/*EVENTOS*/
		btnInicioSesion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String nom = textNombre.getText();
				String con = textContrasenia.getText();
				if(nom.equals("") || con.equals("")) {
					JOptionPane.showMessageDialog(null, "Tienes que rellenar todos los campos");
					logger.log(Level.INFO, "Parte de los datos introducidos son nulos");
				}else {
					BaseDatos.initBD("BaseDatos.db");
					int resul = BaseDatos.existeCliente(nom, con);
					if(resul == 2) {
						try {
							Cliente c = BaseDatos.ObtenerCliente(nom);
							VentanaPrincipal.clientesesion=c;
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "Iniciada la sesión correctamente");
						logger.log(Level.INFO, "Sesión iniciada");
						new VentanaProducto();
						JOptionPane.showMessageDialog(null,"Presione ALT+Click o el botón 'AÑADIR' para añadir a su carrito el producto");
						setVisible(false);
					}else if(resul==1) {
						JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
					}else {
						JOptionPane.showMessageDialog(null, "No te puedes registrar, no existe ese nombre de usuario");
					}
					BaseDatos.closeBD();
				}
			}
		});
		
		setVisible(true);
	}

}