/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.editor.view.widgets.editionview.variables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.view.widgets.IconButton;
import es.eucm.ead.editor.view.widgets.MultiStateButton;
import es.eucm.ead.engine.I18N;

public class VariableSelectorWidget extends Table {

	private static final float BUTTON_MARGIN = 75, PAD = 30;

	private static final String NOT_VAR = "edition.withoutVariable";

	private TextButton nameButton;

	private MultiStateButton stateButton;

	private Array<String> states;

	private Array<Color> colors;

	private I18N i18n;

	private Label labelTo;

	private Cell labelToCell;

	private Cell stateButtonCell;

	private boolean local;

	public VariableSelectorWidget(Controller controller, boolean allowOpossite) {
		this(controller, allowOpossite, "", "");
	}

	public VariableSelectorWidget(Controller controller, boolean allowOpossite,
			String varName, String state) {
		this(controller, allowOpossite, "", "", false);
	}

	public VariableSelectorWidget(Controller controller, boolean allowOpossite,
			String varName, String state, boolean isLocal) {
		super();

		Skin skin = controller.getApplicationAssets().getSkin();
		i18n = controller.getApplicationAssets().getI18N();

		local = isLocal;

		states = new Array<String>();
		states.add(i18n.m("edition.true"));
		states.add(i18n.m("edition.false"));
		colors = new Array<Color>();
		colors.add(Color.GREEN);
		colors.add(Color.RED);
		if (allowOpossite) {
			states.add(i18n.m("edition.opposite"));
			colors.add(Color.YELLOW);
		}

		labelTo = new Label("=", skin);
		stateButton = new MultiStateButton(skin, states, colors, BUTTON_MARGIN);

		String name = "";
		boolean visible = false;
		if (varName.equals("")) {
			name = i18n.m(NOT_VAR);
		} else {
			name = varName;
			visible = true;
		}

		nameButton = new TextButton(name, skin, "white_press");

		if (!state.equals("")) {
			setState(state);
		}

		IconButton removeButton = new IconButton("remove80x80", 0, skin,
				"white");
		removeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Group parent = VariableSelectorWidget.this.getParent();
				if (parent != null && parent.getChildren().size > 2) {
					VariableSelectorWidget.this.getParent().removeActor(
							VariableSelectorWidget.this);
				} else {
					selectedVariable(false);
				}
			}

		});

		add(nameButton).left();
		labelToCell = add().right();
		stateButtonCell = add().right();
		add(removeButton).size(nameButton.getHeight()).left()
				.pad(0, PAD, 0, PAD);

		selectedVariable(visible);
	}

	public void selectedVariable(boolean selected) {
		if (selected
				&& !i18n.m(NOT_VAR).equals(nameButton.getText().toString())) {
			stateButtonCell.setActor(stateButton);
			labelToCell.setActor(labelTo).pad(0, PAD, 0, PAD).expandX();
		} else {
			nameButton.setText(i18n.m(NOT_VAR));
			stateButtonCell.setActor(null);
			labelToCell.setActor(null).pad(0, 0, 0, 0).expand(false, false);
		}
	}

	public String getSelectedVariableName() {
		return nameButton.getText().toString();
	}

	public String getState() {
		if (stateButton.getText().toString().equals(i18n.m("edition.true"))) {
			return "btrue";
		} else if (stateButton.getText().toString()
				.equals(i18n.m("edition.false"))) {
			return "bfalse";
		} else {
			return "( not $" + getSelectedVariableName() + " )";
		}
	}

	public void setState(String state) {
		if (state.equals("btrue")) {
			stateButton.selectText(i18n.m("edition.true"));
		} else if (state.equals("bfalse")) {
			stateButton.selectText(i18n.m("edition.false"));
		} else {
			stateButton.selectText(i18n.m("edition.opposite"));
		}
	}

	public String getExpression() {
		String variable = getSelectedVariableName();
		if (variable != null && !variable.equals(i18n.m(NOT_VAR))) {
			return "( eq $" + variable + " " + getState() + " )";
		} else {
			return null;
		}
	}

	public void changeVarName(String text) {
		changeVarName(text, false);
	}

	public void changeVarName(String text, boolean local) {
		nameButton.setText(text);
		this.local = local;
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		fire(changeEvent);
		Pools.free(changeEvent);
	}

	public TextButton getVarNameButton() {
		return nameButton;
	}

	public MultiStateButton getVarStateButton() {
		return stateButton;
	}

	public boolean isLocal() {
		return local;
	}
}
