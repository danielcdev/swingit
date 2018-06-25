package com.danielcotter.swingit.utility;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.springframework.stereotype.Component;

@Component
public class ModalUtility {

	public String getInput(String text) {
		JLabel label = new JLabel(text);
		final JTextField input = new JTextField();
		Object[] ob = { label, input };
		int result = JOptionPane.showConfirmDialog(null, ob, "SwinGit", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION)
			return input.getText().trim();

		return null;
	}

	public void error(String error) {
		JOptionPane.showMessageDialog(null, "Error\n" + error, "SwinGit", JOptionPane.ERROR_MESSAGE);
	}

	public void info(String info) {
		JOptionPane.showMessageDialog(null, info, "SwinGit", JOptionPane.INFORMATION_MESSAGE);
	}
}
