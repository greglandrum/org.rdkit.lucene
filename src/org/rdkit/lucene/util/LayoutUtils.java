/*
 * Copyright (C)2014, Novartis Institutes for BioMedical Research Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 * 
 * - Neither the name of Novartis Institutes for BioMedical Research Inc.
 *   nor the names of its contributors may be used to endorse or promote
 *   products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rdkit.lucene.util;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

/**
 * This class offers a collection of static utility methods for laying out and
 * formatting components. It cannot be instantiated.
 * 
 * @author Manuel Schwarze
 */
public final class LayoutUtils {

	//
	// Constants
	//

	/** See <code>java.awt.GridBagConstraints.RELATIVE</code>. */
	public static final int RELATIVE = GridBagConstraints.RELATIVE;

	/** See <code>java.awt.GridBagConstraints.REMAINDER</code>. */
	public static final int REMAINDER = GridBagConstraints.REMAINDER;

	/** See <code>java.awt.GridBagConstraints.NONE</code>. */
	public static final int NONE = GridBagConstraints.NONE;

	/** See <code>java.awt.GridBagConstraints.BOTH</code>. */
	public static final int BOTH = GridBagConstraints.BOTH;

	/** See <code>java.awt.GridBagConstraints.HORIZONTAL</code>. */
	public static final int HORIZONTAL = GridBagConstraints.HORIZONTAL;

	/** See <code>java.awt.GridBagConstraints.VERTICAL</code>. */
	public static final int VERTICAL = GridBagConstraints.VERTICAL;

	/** See <code>java.awt.GridBagConstraints.CENTER</code>. */
	public static final int CENTER = GridBagConstraints.CENTER;

	/** See <code>java.awt.GridBagConstraints.NORTH</code>. */
	public static final int NORTH = GridBagConstraints.NORTH;

	/** See <code>java.awt.GridBagConstraints.NORTHEAST</code>. */
	public static final int NORTHEAST = GridBagConstraints.NORTHEAST;

	/** See <code>java.awt.GridBagConstraints.EAST</code>. */
	public static final int EAST = GridBagConstraints.EAST;

	/** See <code>java.awt.GridBagConstraints.SOUTHEAST</code>. */
	public static final int SOUTHEAST = GridBagConstraints.SOUTHEAST;

	/** See <code>java.awt.GridBagConstraints.SOUTH</code>. */
	public static final int SOUTH = GridBagConstraints.SOUTH;

	/** See <code>java.awt.GridBagConstraints.SOUTHWEST</code>. */
	public static final int SOUTHWEST = GridBagConstraints.SOUTHWEST;

	/** See <code>java.awt.GridBagConstraints.WEST</code>. */
	public static final int WEST = GridBagConstraints.WEST;

	/** See <code>java.awt.GridBagConstraints.NORTHWEST</code>. */
	public static final int NORTHWEST = GridBagConstraints.NORTHWEST;

	/**
	 * Defines the default fall back color for backgrounds. The default is
	 * white.
	 */
	private static final Color DEFAULT_BACKGROUND = Color.white;

	/**
	 * Defines the default fall back color for foregrounds. The default is
	 * black.
	 */
	private static final Color DEFAULT_FOREGROUND = Color.black;

	/**
	 * Defines the default fall back font to be used when no other font is
	 * available. The default is Font("Helvetica", Font.PLAIN, 12).
	 */
	private static Font g_defaultFont = null;

	/** The logger instance. */
	private static final Logger LOGGER = Logger.getLogger(LayoutUtils.class.getName());

	static {
		try {
			g_defaultFont = new Font("Helvetica", Font.PLAIN, 12); // $NON-NLS-1$
		}
		catch (final ThreadDeath excThread) {
			throw excThread;
		}
		catch (final Throwable exc) { // Catching also errors cause my unavailable Graphic devices
			LOGGER.log(Level.WARNING, "Unable to initialize default font.", exc);
		}
	}

	//
	// Constructors
	//

	/**
	 * This class cannot be instantiated.
	 */
	private LayoutUtils() {
		// This class cannot be instantiated
	}

	//
	// Public Methods
	//

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Button comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Button.background", // $NON-NLS-1$
				"Button.foreground", // $NON-NLS-1$
				"Button.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JButton comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Button.background", // $NON-NLS-1$
				"Button.foreground", // $NON-NLS-1$
				"Button.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Choice comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ComboBox.background", // $NON-NLS-1$
				"ComboBox.foreground", // $NON-NLS-1$
				"ComboBox.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JComboBox comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ComboBox.background", // $NON-NLS-1$
				"ComboBox.foreground", // $NON-NLS-1$
				"ComboBox.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Label comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Label.background", // $NON-NLS-1$
				"Label.foreground", // $NON-NLS-1$
				"Label.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JLabel comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Label.background", // $NON-NLS-1$
				"Label.foreground", // $NON-NLS-1$
				"Label.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final List comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "List.background", // $NON-NLS-1$
				"List.foreground", // $NON-NLS-1$
				"List.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JList comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "List.background", // $NON-NLS-1$
				"List.foreground", // $NON-NLS-1$
				"List.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JMenuItem comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "MenuItem.background", // $NON-NLS-1$
				"MenuItem.foreground", // $NON-NLS-1$
				"MenuItem.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JMenu comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Menu.background", // $NON-NLS-1$
				"Menu.foreground", // $NON-NLS-1$
				"Menu.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JPopupMenu comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "PopupMenu.background", // $NON-NLS-1$
				"PopupMenu.foreground", // $NON-NLS-1$
				"PopupMenu.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JMenuBar comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "MenuBar.background", // $NON-NLS-1$
				"MenuBar.foreground", // $NON-NLS-1$
				"MenuBar.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JOptionPane comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "OptionPane.background", // $NON-NLS-1$
				"OptionPane.foreground", // $NON-NLS-1$
				"OptionPane.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Checkbox comp) {
		if (comp == null) {
			return;
		}

		if (comp.getCheckboxGroup() == null) {
			applyProperties(comp, "CheckBox.background", // $NON-NLS-1$
					"CheckBox.foreground", // $NON-NLS-1$
					"CheckBox.font"); // $NON-NLS-1$
		} else {
			// Checkbox is in group
			applyProperties(comp, "RadioButton.background", // $NON-NLS-1$
					"RadioButton.foreground", // $NON-NLS-1$
					"RadioButton.font"); // $NON-NLS-1$
		}
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JCheckBox comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "CheckBox.background", // $NON-NLS-1$
				"CheckBox.foreground", // $NON-NLS-1$
				"CheckBox.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JRadioButton comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "RadioButton.background", // $NON-NLS-1$
				"RadioButton.foreground", // $NON-NLS-1$
				"RadioButton.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Scrollbar comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ScrollBar.background", // $NON-NLS-1$
				"ScrollBar.foreground", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JScrollBar comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ScrollBar.background", // $NON-NLS-1$
				"ScrollBar.foreground", // $NON-NLS-1$
				null);
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final ScrollPane comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ScrollPane.background", // $NON-NLS-1$
				"ScrollPane.foreground", // $NON-NLS-1$
				"ScrollPane.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JScrollPane comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "ScrollPane.background", // $NON-NLS-1$
				"ScrollPane.foreground", // $NON-NLS-1$
				"ScrollPane.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JSeparator comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Separator.background", // $NON-NLS-1$
				"Separator.foreground", // $NON-NLS-1$
				"Separator.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JSplitPane comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "SplitPane.background", // $NON-NLS-1$
				null, null);
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTabbedPane comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "TabbedPane.background", // $NON-NLS-1$
				"TabbedPane.foreground", // $NON-NLS-1$
				"TabbedPane.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTable comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Table.background", // $NON-NLS-1$
				"Table.foreground", // $NON-NLS-1$
				"Table.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final TextArea comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "TextArea.background", // $NON-NLS-1$
				"TextArea.foreground", // $NON-NLS-1$
				"TextArea.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTextArea comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "TextArea.background", // $NON-NLS-1$
				"TextArea.foreground", // $NON-NLS-1$
				"TextArea.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final TextField comp) {
		if (comp == null) {
			return;
		}

		if (comp.getEchoChar() != (char) 0) {
			applyProperties(comp, "PasswordField.background", // $NON-NLS-1$
					"PasswordField.foreground", // $NON-NLS-1$
					"PasswordField.font"); // $NON-NLS-1$
		} else {
			// Normal text field
			applyProperties(comp, "TextField.background", // $NON-NLS-1$
					"TextField.foreground", // $NON-NLS-1$
					"TextField.font"); // $NON-NLS-1$
		}
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTextField comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "TextField.background", // $NON-NLS-1$
				"TextField.foreground", // $NON-NLS-1$
				"TextField.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JPasswordField comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "PasswordField.background", // $NON-NLS-1$
				"PasswordField.foreground", // $NON-NLS-1$
				"PasswordField.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTree comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Tree.background", // $NON-NLS-1$
				"Tree.foreground", // $NON-NLS-1$
				"Tree.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Panel comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Panel.background", // $NON-NLS-1$
				"Panel.foreground", // $NON-NLS-1$
				"Panel.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JPanel comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "Panel.background", // $NON-NLS-1$
				"Panel.foreground", // $NON-NLS-1$
				"Panel.font"); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Window comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "window", // $NON-NLS-1$
				"windowText", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Dialog comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "control", // $NON-NLS-1$
				"controlText", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JDialog comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "control", // $NON-NLS-1$
				"controlText", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final TextComponent comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "text", // $NON-NLS-1$
				"textText", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component. It uses values defined as UIManager
	 * properties.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final JTextComponent comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "text", // $NON-NLS-1$
				"textText", // $NON-NLS-1$
				null); // $NON-NLS-1$
	}

	/**
	 * Sets default background and foreground color as well as a default font
	 * for the specified component.
	 * 
	 * @param comp
	 *            Component to set default properties for. Can be
	 *            <code>null</code>.
	 */
	public static void applyDefaultProperties(final Component comp) {
		if (comp == null) {
			return;
		}

		applyProperties(comp, "control", // $NON-NLS-1$
				"controlText", // $NON-NLS-1$
				null);
	}

	/**
	 * Sets background and foreground color and a font for the specified
	 * component.
	 * 
	 * @param comp
	 *            Component to set properties for.
	 * @param colBack
	 *            Background color. Can be <code>null</code> to use parent color
	 *            (not suggested due to differences between Java VMs).
	 * @param colFore
	 *            Foreground color. Can be <code>null</code> to use parent color
	 *            (not suggested due to differences between Java VMs).
	 * @param font
	 *            Font. Can be <code>null</code> to use parent font (not
	 *            suggested due to differences between Java VMs).
	 */
	public static void applyProperties(final Component comp, final Color colBack,
			final Color colFore, final Font font) {
		if (comp != null) {
			comp.setBackground(colBack);
			comp.setForeground(colFore);
			comp.setFont(font);
		}
	}

	/**
	 * Sets background and foreground color and a font for the specified
	 * component.
	 * 
	 * @param comp
	 *            Component to set properties for. If <code>
	 * null</code> nothing happens.
	 * @param strKeyBackground
	 *            Key for background color defined in
	 *            <code>javax.swing.UIManager</code>. If it is <code>null</code>
	 *            or not found first the key "control" will be tried, and if
	 *            still <code>null</code>, a "fallback" color will be taken.
	 * @param strKeyForeground
	 *            Key for background color defined in
	 *            <code>javax.swing.UIManager</code>. If it is <code>null</code>
	 *            or not found first the key "textText" will be tried, and if
	 *            still <code>null</code>, a "fallback" color will be taken.
	 * @param strKeyFont
	 *            Key for font defined in <code>
	 * javax.swing.UIManager</code>. If it is <code>null</code> or not found
	 *            first the key "Label.font" will be tried, and if still
	 *            <code>null</code>, a "fallback" font will be taken.
	 */
	public static void applyProperties(final Component comp, final String strKeyBackground,
			final String strKeyForeground, final String strKeyFont) {
		if (comp != null) {
			Color colBack = null;
			Color colFore = null;
			Font font = null;

			if (strKeyBackground != null) {
				colBack = UIManager.getColor(strKeyBackground);
			}

			if (strKeyForeground != null) {
				colFore = UIManager.getColor(strKeyForeground);
			}

			if (strKeyFont != null) {
				font = UIManager.getFont(strKeyFont);
			}

			if (colBack == null) {
				colBack = UIManager.getColor("control"); // $NON-NLS-1$

				if (colBack == null) {
					colBack = DEFAULT_BACKGROUND; // Fall back default
				}
			}

			if (colFore == null) {
				colFore = UIManager.getColor("controlText"); // $NON-NLS-1$

				if (colFore == null) {
					colFore = DEFAULT_FOREGROUND; // Fall back default
				}
			}

			if (font == null) {
				font = UIManager.getFont("Label.font"); // $NON-NLS-1$

				if (font == null) {
					font = g_defaultFont; // Fall back default
				}
			}

			applyProperties(comp, colBack, colFore, font);
		} // end if
	} // end method applyProperties

	/**
	 * Call this method to calculate coordinates dependent on the screen size to
	 * center the specified window.
	 * 
	 * @param window
	 *            Window to center in the screen. Can be <code>
	 * null</code>.
	 */
	public static void centerWindow(final Window window) {
		if (window == null) {
			return;
		}

		Rectangle rect = window.getBounds();

		if ((rect.width <= 0) || (rect.height <= 0)) {
			window.pack();
			rect = window.getBounds();
		}

		if (rect.width <= 0) {
			rect.width = 400;
		}

		if (rect.height <= 0) {
			rect.height = 300;
		}

		final Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Point point;
		Dimension dim;

		point = new Point(0, 0);
		dim = dimScreen;

		rect.x = (point.x + (dim.width / 2)) - (rect.width / 2);
		rect.y = (point.y + (dim.height / 2)) - (rect.height / 2);

		if (rect.x > (dimScreen.width - rect.width)) {
			rect.x = dimScreen.width - rect.width;
		}

		if (rect.x < 0) {
			rect.x = 0;
		}

		if (rect.y > (dimScreen.height - rect.height)) {
			rect.y = dimScreen.height - rect.height;
		}

		if (rect.y < 0) {
			rect.y = 0;
		}

		window.setBounds(rect.x, rect.y, rect.width, rect.height);
	} // end method centerWindow

	/**
	 * Takes the specified color and makes it darker or lighter according to the
	 * specified difference.
	 * 
	 * @param color
	 *            Original color to be changed. Can be <code>
	 * null</code>.
	 * @param difference
	 *            Difference to be applied to the red, green and blue values of
	 *            the original color.
	 * 
	 * @return New color object build from the original color and the specified
	 *         differences. Returns <code>null</code>, if the passed in color
	 *         was <code>null</code>.
	 */
	public static Color changeColor(final Color color, final int difference) {
		Color retColor = null;

		if (color != null) {
			final int[] colors = new int[3];

			colors[0] = color.getRed();
			colors[1] = color.getGreen();
			colors[2] = color.getBlue();

			for (int i = 0; i < 3; i++) {
				colors[i] += difference;

				if (colors[i] < 0) {
					colors[i] = 0;
				}

				if (colors[i] > 255) {
					colors[i] = 255;
				}
			}

			retColor = new Color(colors[0], colors[1], colors[2]);
		}

		return retColor;
	}

	/**
	 * Helper function to work with GridBagConstraints in a
	 * GridBagLayoutManager. For the following constraints we use defaults:<BR>
	 * <code>iFill</code>= NONE<BR>
	 * <code>iAnchor</code>= NORTHWEST<BR>
	 * <code>dWeightX</code>= 0.0<BR>
	 * <code>dWeightY</code>= 0.0<BR>
	 * <code>iTop</code>= 0<BR>
	 * <code>iLeft</code>= 0<BR>
	 * <code>iBottom</code>= 0<BR>
	 * <code>iRight</code>= 0<BR>
	 * 
	 * @param cont
	 *            Container that has a <code>GridBagLayout</code> which the
	 *            component has to be added to. Cannot be <code>
	 * null</code>.
	 * @param comp
	 *            Component to be added and layed out.
	 * @param iGridX
	 *            Specifies the cell at the left of the component's display
	 *            area, where the leftmost cell has gridx�=�0. The value
	 *            RELATIVE specifies that the component be placed just to the
	 *            right of the component that was added to the container just
	 *            before this component was added.
	 * @param iGridY
	 *            Specifies the cell at the top of the component's display area,
	 *            where the topmost cell has gridy�=�0. The value RELATIVE
	 *            specifies that the component be placed just below the
	 *            component that was added to the container just before this
	 *            component was added.
	 * @param iGridWidth
	 *            Specifies the number of cells in a row for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its column. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its column.
	 * @param iGridHeight
	 *            Specifies the number of cells in a column for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its row. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its row.
	 */
	public static void constrain(final Container cont, final Component comp, final int iGridX,
			final int iGridY, final int iGridWidth, final int iGridHeight) {
		constrain(cont, comp, iGridX, iGridY, iGridWidth, iGridHeight, NONE,
				NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
	}

	/**
	 * Helper function to work with GridBagConstraints in a
	 * GridBagLayoutManager. For the following constraints we use defaults:<BR>
	 * <code>iFill</code>= NONE<BR>
	 * <code>iAnchor</code>= NORTHWEST<BR>
	 * <code>dWeightX</code>= 0.0<BR>
	 * <code>dWeightY</code>= 0.0<BR>
	 * 
	 * @param cont
	 *            Container that has a <code>GridBagLayout</code> which the
	 *            component has to be added to. Cannot be <code>
	 * null</code>.
	 * @param comp
	 *            Component to be added and layed out.
	 * @param iGridX
	 *            Specifies the cell at the left of the component's display
	 *            area, where the leftmost cell has gridx�=�0. The value
	 *            RELATIVE specifies that the component be placed just to the
	 *            right of the component that was added to the container just
	 *            before this component was added.
	 * @param iGridY
	 *            Specifies the cell at the top of the component's display area,
	 *            where the topmost cell has gridy�=�0. The value RELATIVE
	 *            specifies that the component be placed just below the
	 *            component that was added to the container just before this
	 *            component was added.
	 * @param iGridWidth
	 *            Specifies the number of cells in a row for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its column. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its column.
	 * @param iGridHeight
	 *            Specifies the number of cells in a column for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its row. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its row.
	 * @param iTop
	 *            Specifies the external padding of the component on top, the
	 *            minimum amount of space between the component and the edges of
	 *            its display area on top.
	 * @param iLeft
	 *            Specifies the external padding of the component on the left,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area on the left.
	 * @param iBottom
	 *            Specifies the external padding of the component at the bottom,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area at the bottom.
	 * @param iRight
	 *            Specifies the external padding of the component on the right,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area on the right.
	 */
	public static void constrain(final Container cont, final Component comp, final int iGridX,
			final int iGridY, final int iGridWidth, final int iGridHeight, final int iTop, final int iLeft,
			final int iBottom, final int iRight) {
		constrain(cont, comp, iGridX, iGridY, iGridWidth, iGridHeight, NONE,
				NORTHWEST, 0.0, 0.0, iTop, iLeft, iBottom, iRight);
	}

	/**
	 * Helper function to work with GridBagConstraints in a
	 * GridBagLayoutManager.
	 * 
	 * @param cont
	 *            Container that has a <code>GridBagLayout</code> which the
	 *            component has to be added to. Cannot be <code>
	 * null</code>.
	 * @param comp
	 *            Component to be added and layed out.
	 * @param iGridX
	 *            Specifies the cell at the left of the component's display
	 *            area, where the leftmost cell has gridx�=�0. The value
	 *            RELATIVE specifies that the component be placed just to the
	 *            right of the component that was added to the container just
	 *            before this component was added.
	 * @param iGridY
	 *            Specifies the cell at the top of the component's display area,
	 *            where the topmost cell has gridy�=�0. The value RELATIVE
	 *            specifies that the component be placed just below the
	 *            component that was added to the container just before this
	 *            component was added.
	 * @param iGridWidth
	 *            Specifies the number of cells in a row for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its column. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its column.
	 * @param iGridHeight
	 *            Specifies the number of cells in a column for the component's
	 *            display area. Use REMAINDER to specify that the component be
	 *            the last one in its row. Use RELATIVE to specify that the
	 *            component be the next-to-last one in its row.
	 * @param iFill
	 *            This field is used when the component's display area is larger
	 *            than the component's requested size. It determines whether to
	 *            resize the component, and if so, how. The following values are
	 *            valid for fill:<BR>
	 *            � NONE: Do not resize the component.<BR>
	 *            � HORIZONTAL: Make the component wide enough to fill its
	 *            display area horizontally, but do not change its height.<BR>
	 *            � VERTICAL: Make the component tall enough to fill its display
	 *            area vertically, but do not change its width.<BR>
	 *            � BOTH: Make the component fill its display area entirely.<BR>
	 * @param iAnchor
	 *            This field is used when the component is smaller than its
	 *            display area. It determines where, within the display area, to
	 *            place the component. Possible values are CENTER, NORTH,
	 *            NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, and
	 *            NORTHWEST.
	 * @param dWeightX
	 *            Specifies how to distribute extra horizontal space. The grid
	 *            bag layout manager calculates the weight of a column to be the
	 *            maximum weighty of all the components in a row. If the
	 *            resulting layout is smaller horizontally than the area it
	 *            needs to fill, the extra space is distributed to each column
	 *            in proportion to its weight. A column that has a weight zero
	 *            receives no extra space. If all the weights are zero, all the
	 *            extra space appears between the grids of the cell and the left
	 *            and right edges.
	 * @param dWeightY
	 *            Specifies how to distribute extra vertical space. The grid bag
	 *            layout manager calculates the weight of a row to be the
	 *            maximum weightx of all the components in a row. If the
	 *            resulting layout is smaller vertically than the area it needs
	 *            to fill, the extra space is distributed to each row in
	 *            proportion to its weight. A row that has a weight of zero
	 *            receives no extra space. If all the weights are zero, all the
	 *            extra space appears between the grids of the cell and the top
	 *            and bottom edges.
	 * @param iTop
	 *            Specifies the external padding of the component on top, the
	 *            minimum amount of space between the component and the edges of
	 *            its display area on top.
	 * @param iLeft
	 *            Specifies the external padding of the component on the left,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area on the left.
	 * @param iBottom
	 *            Specifies the external padding of the component at the bottom,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area at the bottom.
	 * @param iRight
	 *            Specifies the external padding of the component on the right,
	 *            the minimum amount of space between the component and the
	 *            edges of its display area on the right.
	 * 
	 * @throws IllegalArgumentException
	 *             The argument does not meet the specifications.
	 */
	public static void constrain(final Container cont, final Component comp, final int iGridX,
			final int iGridY, final int iGridWidth, final int iGridHeight, final int iFill,
			final int iAnchor, final double dWeightX, final double dWeightY,
			final int iTop, final int iLeft, final int iBottom, final int iRight) {
		// Ensure the parameters are valid
		if (cont == null || comp == null) {
			throw new IllegalArgumentException(
					"Parameters cont and comp must not be null.");
		}

		if (!(cont.getLayout() instanceof GridBagLayout)) {
			throw new IllegalArgumentException(
					"Container parameter \"cont\" must have a GridBagLayout.");
		}

		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = iGridX;
		constraint.gridy = iGridY;
		constraint.gridwidth = iGridWidth;
		constraint.gridheight = iGridHeight;
		constraint.fill = iFill;
		constraint.anchor = iAnchor;
		constraint.weightx = dWeightX;
		constraint.weighty = dWeightY;

		if ((iTop + iLeft + iBottom + iRight) > 0) {
			constraint.insets = new Insets(iTop, iLeft, iBottom, iRight);
		}

		((GridBagLayout) cont.getLayout()).setConstraints(comp, constraint);
		cont.add(comp);
	} // end method constrain

	/**
	 * Call this method to copy an image to the system clipboard.
	 * 
	 * @param pImage
	 *            The Image object to copy.
	 * 
	 * @return True if the save is successful; otherwise false.
	 */
	public static boolean copyImageToClipboard(final Image pImage) {
		boolean bResult = false;

		if (pImage != null) {
			try {
				final Clipboard clipboard = new Canvas().getToolkit()
						.getSystemClipboard();

				clipboard.setContents(new Transferable() {
					@Override
					public Object getTransferData(final DataFlavor flavor)
							throws UnsupportedFlavorException, IOException {
						return pImage;
					}

					@Override
					public DataFlavor[] getTransferDataFlavors() {
						return new DataFlavor[] { DataFlavor.imageFlavor };
					}

					@Override
					public boolean isDataFlavorSupported(final DataFlavor flavor) {
						return flavor.equals(DataFlavor.imageFlavor);
					}
				}, null);
			} catch (final Exception e) {
				bResult = false;
			}
		} // end if

		return bResult;
	} // end method copyImageToClipboard

	/**
	 * Call this method to create an image icon associated with the specified
	 * object.
	 * 
	 * @param objRoot
	 *            The object, which loads the image as resource. Can be
	 *            <code>null</code>.
	 * @param strPath
	 *            Resource path (absolute or relative to the specified root
	 *            object). Must not be <code>null</code>. An absolute example:
	 *            "/com/novartis/nibr/knime/images/loading.gif".
	 * @param strDescription
	 *            A description of the image to be loaded. Can be
	 *            <code>null</code>.
	 * 
	 * @return ImageIcon object containing the loaded image, or null, if image
	 *         could not be loaded.
	 */
	public static ImageIcon createImageIcon(final Object objRoot, final String strPath,
			final String strDescription) {
		return createImageIcon((objRoot == null) ? LayoutUtils.class : objRoot.getClass(),
				strPath, strDescription);
	}

	/**
	 * Call this method to create an image icon associated with the specified
	 * object.
	 * 
	 * @param rootClass
	 *            The class which loads the image as resource.
	 * @param strPath
	 *            Resource path (absolute or relative to the specified root
	 *            object). Must not be <code>null</code>. An absolute example:
	 *            "/com/novartis/nibr/knime/images/loading.gif".
	 * @param strDescription
	 *            A description of the image to be loaded. Can be
	 *            <code>null</code>.
	 * 
	 * @return ImageIcon object containing the loaded image, or null, if image
	 *         could not be loaded.
	 */
	public static ImageIcon createImageIcon(final Class<?> rootClass, final String strPath,
			final String strDescription) {
		// Ensure parameters are valid
		if (strPath == null) {
			throw new IllegalArgumentException(
					"Parameter strPath must not be null.");
		}

		ImageIcon img = null;

		try {
			final URL url = rootClass.getResource(strPath);
			if (url != null) {
				img = new ImageIcon(url, strDescription);
			}
			else {
				// Ignore (images not found)
				LOGGER.log(Level.WARNING, "Couldn't find image resource: " + strPath);
			}
		} catch (final Exception exc) {
			// Ignore (images not found)
			LOGGER.log(Level.WARNING, "Couldn't load image resource: " + strPath);
		}

		return img;
	}

	/**
	 * Call this method to load an image associated with the specified object.
	 * 
	 * @param objRoot
	 *            The object, which loads the image. Must not be
	 *            <code>null</code>.
	 * @param strResourcePath
	 *            Resource path starting from the root package of the Java
	 *            application. Must not be <code>null</code>. An example:
	 *            "/com/novartis/nibr/knime/images/loading.gif".
	 * 
	 * @return Image object containing the loaded image, or null, if image could
	 *         not be loaded.
	 */
	public static Image loadImage(final Object objRoot, final String strResourcePath) {
		// Ensure the parameters are valid
		if (objRoot == null || strResourcePath == null) {
			throw new IllegalArgumentException(
					"Parameters objRoot and strResourcePath must not be null.");
		}

		Image img = null;

		try {
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			img = toolkit.getImage(objRoot.getClass().getResource(strResourcePath));
		} catch (final Exception exc) {
			LOGGER.log(Level.WARNING, "Image resource '" + strResourcePath + "' not found. Ignored.");
			// Ignore (images not found)
		}

		return img;
	}

	/**
	 * Call this method to save an image as a JPEG.
	 * 
	 * @param pImage
	 *            The Image object to save.
	 * @param strPath
	 *            The Path, including filename where the Image will be saved. If
	 *            the file already exists it will be overwritten.
	 * 
	 * @return True if the save is successful; otherwise false.
	 */
	public static boolean saveImage(final Image pImage, final String strPath) {
		boolean bResult = false;
		File fImage = null;

		try {
			final BufferedImage bImage = (BufferedImage) pImage;
			fImage = new File(strPath);

			fImage.createNewFile();

			// Write the Image to disk
			ImageIO.write(bImage, "jpg", fImage);
			bResult = true;
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(null, "Error saving image to disk. "
					+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			if ((fImage != null && fImage.exists()) || bResult) {
				LOGGER.info("Image creation successful during the saveImage method.");
			} else {
				LOGGER.log(Level.WARNING, "Image creation failed during the saveImage method.");
			}
		}

		return bResult;
	} // end method saveImage

	/**
	 * Creates a unique combo box item.
	 * 
	 * @param item The name of the item to be shown in the combo box.
	 * 
	 * @return An object that wraps the passed in string item and returns it in
	 * 		when the toString() method gets called on that object.
	 */
	public static Object createUniqueComboBoxItem(final String item)  {
		return new Object() {
			@Override
			public String toString() { return item; }
		};
	}
}
