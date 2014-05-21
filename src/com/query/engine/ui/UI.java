package com.query.engine.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.query.engine.exceptions.KBFileWriteException;
import com.query.engine.infra.KnowledgeBase;
import com.query.engine.infra.QueryResultTable;

public class UI {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private KnowledgeBase kb;
	private JTable table;
	private static DefaultTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 600, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnNewButton = new JButton("Load Database");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kb = new KnowledgeBase(textField.getText());
			}
		});

		textField = new JTextField();

		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Path");
		textArea = new JTextArea();

		JButton btnNewButton_1 = new JButton("Run Query");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					QueryResultTable result = kb.run_query(textArea.getText(),
							"C:\\resul1");
					HashMap<String, ArrayList<String>> map = result
							.getTableContents();
					table.setVisible(true);
				
					tableModel = new DefaultTableModel();
					for(Entry<String, ArrayList<String>> entry: map.entrySet()){
						tableModel.addColumn(entry.getKey(), entry.getValue().toArray());						
					}
					
					

					table.setModel(tableModel);

					// textArea_1.setText("Executed Man");
				} catch (KBFileWriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		tableModel = new DefaultTableModel();
		table = new JTable(tableModel);
		table.setVisible(false);

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				table,
																				GroupLayout.DEFAULT_SIZE,
																				564,
																				Short.MAX_VALUE)
																		.addContainerGap())
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								textArea,
																								Alignment.LEADING,
																								GroupLayout.DEFAULT_SIZE,
																								415,
																								Short.MAX_VALUE)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												lblNewLabel)
																										.addGap(18)
																										.addComponent(
																												textField,
																												GroupLayout.DEFAULT_SIZE,
																												375,
																												Short.MAX_VALUE)))
																		.addGap(18)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(
																								btnNewButton_1,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								btnNewButton,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addGap(36)))));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(25)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																textField,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																btnNewButton)
														.addComponent(
																lblNewLabel))
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(38)
																		.addComponent(
																				textArea,
																				GroupLayout.PREFERRED_SIZE,
																				62,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(51)
																		.addComponent(
																				btnNewButton_1)))
										.addGap(45)
										.addComponent(table,
												GroupLayout.PREFERRED_SIZE,
												177, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(292, Short.MAX_VALUE)));
		frame.getContentPane().setLayout(groupLayout);
	}
}
