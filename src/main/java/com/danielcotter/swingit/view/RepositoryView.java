package com.danielcotter.swingit.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

public class RepositoryView {

	private JPanel panelTop = new JPanel();
	private JPanel panelLeft = new JPanel();
	private JPanel panelRight = new JPanel();
	private JTextArea diffArea = new JTextArea();
	private DefaultListModel<String> unstagedList = new DefaultListModel<>();
	private DefaultListModel<String> stagedList = new DefaultListModel<>();
	private JList<String> unstagedJlist = new JList<>(unstagedList);
	private JList<String> stagedJlist = new JList<>(stagedList);
	private JButton button1 = new JButton("Branch");
	private JButton button2 = new JButton("Commit");
	private JButton button3 = new JButton("Push");
	private JButton button4 = new JButton("Merge");
	private JButton newFeatureButton = new JButton("New Feature");
	private JButton mergeFeatureButton = new JButton("Merge Feature");
	private JButton button7 = new JButton("Stage All");
	private JButton button8 = new JButton("Unstage all");

	public void render() {
		initPanels();
		initToolbar();
		initUnstagedList();
		initDiffArea();
		initStagedList();

		panelTop.add(panelRight);
	}

	private void initPanels() {
		panelTop.setLayout(new BorderLayout());
		panelLeft.setLayout(new GridLayout(2, 1, 10, 10));
		panelRight.setLayout(new GridBagLayout());
		panelRight.add(panelLeft, rightPanelConstraints());
	}

	private void initToolbar() {
		JToolBar toolbar = new JToolBar("Hello");
		toolbar.add(newFeatureButton);
		toolbar.add(mergeFeatureButton);
		toolbar.addSeparator();
		toolbar.add(button7);
		toolbar.add(button8);
		toolbar.addSeparator();
		toolbar.add(button1);
		toolbar.add(button2);
		toolbar.add(button3);
		toolbar.add(button4);

		panelRight.add(toolbar, toolbarConstraints());
	}

	private void initStagedList() {
		stagedJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelLeft.add(new JScrollPane(stagedJlist));
	}

	private void initDiffArea() {
		diffArea.setEditable(false);
		diffArea.setLineWrap(false);
		panelRight.add(new JScrollPane(diffArea), diffAreaConstraints());
	}

	private void initUnstagedList() {
		unstagedJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelLeft.add(new JScrollPane(unstagedJlist));
	}

	private GridBagConstraints toolbarConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.2;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;

		return c;
	}

	private GridBagConstraints rightPanelConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.2;
		c.weighty = 0.2;
		c.gridx = 0;
		c.gridy = 1;

		return c;
	}

	private GridBagConstraints diffAreaConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 1;

		return c;
	}

	/**
	 * @return the panelTop
	 */
	public JPanel getPanelTop() {
		return panelTop;
	}

	/**
	 * @param panelTop
	 *            the panelTop to set
	 */
	public void setPanelTop(JPanel panelTop) {
		this.panelTop = panelTop;
	}

	/**
	 * @return the panelLeft
	 */
	public JPanel getPanelLeft() {
		return panelLeft;
	}

	/**
	 * @param panelLeft
	 *            the panelLeft to set
	 */
	public void setPanelLeft(JPanel panelLeft) {
		this.panelLeft = panelLeft;
	}

	/**
	 * @return the panelRight
	 */
	public JPanel getPanelRight() {
		return panelRight;
	}

	/**
	 * @param panelRight
	 *            the panelRight to set
	 */
	public void setPanelRight(JPanel panelRight) {
		this.panelRight = panelRight;
	}

	/**
	 * @return the diffArea
	 */
	public JTextArea getDiffArea() {
		return diffArea;
	}

	/**
	 * @param diffArea
	 *            the diffArea to set
	 */
	public void setDiffArea(JTextArea diffArea) {
		this.diffArea = diffArea;
	}

	/**
	 * @return the unstagedList
	 */
	public DefaultListModel<String> getUnstagedList() {
		return unstagedList;
	}

	/**
	 * @param unstagedList
	 *            the unstagedList to set
	 */
	public void setUnstagedList(DefaultListModel<String> unstagedList) {
		this.unstagedList = unstagedList;
	}

	/**
	 * @return the stagedList
	 */
	public DefaultListModel<String> getStagedList() {
		return stagedList;
	}

	/**
	 * @param stagedList
	 *            the stagedList to set
	 */
	public void setStagedList(DefaultListModel<String> stagedList) {
		this.stagedList = stagedList;
	}

	/**
	 * @return the unstagedJlist
	 */
	public JList<String> getUnstagedJlist() {
		return unstagedJlist;
	}

	/**
	 * @param unstagedJlist
	 *            the unstagedJlist to set
	 */
	public void setUnstagedJlist(JList<String> unstagedJlist) {
		this.unstagedJlist = unstagedJlist;
	}

	/**
	 * @return the stagedJlist
	 */
	public JList<String> getStagedJlist() {
		return stagedJlist;
	}

	/**
	 * @param stagedJlist
	 *            the stagedJlist to set
	 */
	public void setStagedJlist(JList<String> stagedJlist) {
		this.stagedJlist = stagedJlist;
	}

	/**
	 * @return the button1
	 */
	public JButton getButton1() {
		return button1;
	}

	/**
	 * @param button1
	 *            the button1 to set
	 */
	public void setButton1(JButton button1) {
		this.button1 = button1;
	}

	/**
	 * @return the button2
	 */
	public JButton getButton2() {
		return button2;
	}

	/**
	 * @param button2
	 *            the button2 to set
	 */
	public void setButton2(JButton button2) {
		this.button2 = button2;
	}

	/**
	 * @return the button3
	 */
	public JButton getButton3() {
		return button3;
	}

	/**
	 * @param button3
	 *            the button3 to set
	 */
	public void setButton3(JButton button3) {
		this.button3 = button3;
	}

	/**
	 * @return the button4
	 */
	public JButton getButton4() {
		return button4;
	}

	/**
	 * @param button4
	 *            the button4 to set
	 */
	public void setButton4(JButton button4) {
		this.button4 = button4;
	}

	/**
	 * @return the newFeatureButton
	 */
	public JButton getNewFeatureButton() {
		return newFeatureButton;
	}

	/**
	 * @param newFeatureButton
	 *            the newFeatureButton to set
	 */
	public void setNewFeatureButton(JButton newFeatureButton) {
		this.newFeatureButton = newFeatureButton;
	}

	/**
	 * @return the mergeFeatureButton
	 */
	public JButton getMergeFeatureButton() {
		return mergeFeatureButton;
	}

	/**
	 * @param mergeFeatureButton
	 *            the mergeFeatureButton to set
	 */
	public void setMergeFeatureButton(JButton mergeFeatureButton) {
		this.mergeFeatureButton = mergeFeatureButton;
	}

	/**
	 * @return the button7
	 */
	public JButton getButton7() {
		return button7;
	}

	/**
	 * @param button7
	 *            the button7 to set
	 */
	public void setButton7(JButton button7) {
		this.button7 = button7;
	}

	/**
	 * @return the button8
	 */
	public JButton getButton8() {
		return button8;
	}

	/**
	 * @param button8
	 *            the button8 to set
	 */
	public void setButton8(JButton button8) {
		this.button8 = button8;
	}
}
