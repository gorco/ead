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
package es.eucm.ead.editor.view.widgets.editionview.prefabs;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import es.eucm.ead.editor.control.ComponentId;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.irreversibles.scene.AddBehaviorEffect;
import es.eucm.ead.editor.control.actions.irreversibles.scene.AddBehaviorPrefab;
import es.eucm.ead.editor.control.actions.irreversibles.scene.ClearBehaviorEffects;
import es.eucm.ead.editor.control.actions.irreversibles.scene.RemoveBehavior;
import es.eucm.ead.editor.view.widgets.PositionedHiddenPanel.Position;
import es.eucm.ead.editor.view.widgets.ScrollPaneDif;
import es.eucm.ead.editor.view.widgets.editionview.variables.VariableSelectorWidget;
import es.eucm.ead.editor.view.widgets.editionview.variables.VariablesAndGroup;
import es.eucm.ead.editor.view.widgets.editionview.variables.VariablesTable;
import es.eucm.ead.schema.components.behaviors.Behavior;
import es.eucm.ead.schema.effects.ChangeVar;
import es.eucm.ead.schema.effects.ChangeVar.Context;
import es.eucm.ead.schema.effects.Effect;

public class ChangeVariablePanel extends PrefabComponentPanel {

	private VariablesTable variablesPanel;

	private VariablesAndGroup varOp;

	private ChangeVar changeVar;

	public ChangeVariablePanel(final Controller controller) {
		super("variable80x80", "edition.changeVariable",
				ComponentId.PREFAB_CHANGE_VAR, controller);

		variablesPanel = new VariablesTable(skin, Position.RIGHT, this,
				controller);
		changeVar = new ChangeVar();
		changeVar.setContext(Context.GLOBAL);

		ChangeListener varChanged = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateComponent();
				panel.updatePositionPanel();
			}
		};
		varOp = new VariablesAndGroup(controller, true, variablesPanel,
				varChanged) {
			@Override
			protected String getI18NKeyAddButton() {
				return "general.add";
			}
		};

		ScrollPaneDif sp = new ScrollPaneDif(varOp, skin, "fadeY");
		panel.add(sp);

	}

	@Override
	protected void trashClicked() {
		super.trashClicked();
		panel.updatePositionPanel();
	}

	@Override
	public void showPanel() {
		super.showPanel();
		variablesPanel.updatePanel();
	}

	@Override
	public void hidePanel() {
		if (variablesPanel != null) {
			variablesPanel.hide(fadeOut(OUT_DURATION, Interpolation.fade));
		}
		super.hidePanel();
	}

	@Override
	protected void actualizePanel() {
		if (component != null) {
			varOp.reset();
			Array<Effect> effects = ((Behavior) component).getEffects();
			for (Effect effect : effects) {
				if (effect instanceof ChangeVar) {
					ChangeVar changeVar = (ChangeVar) effect;
					varOp.addVariableWidget(varOp.variableWidget(
							changeVar.getVariable(), changeVar.getExpression(),
							changeVar.getContext().equals(Context.LOCAL)));
				}
			}
		} else {
			varOp.emptyWidget();
		}
	}

	public void updateComponent() {
		if (component != null) {
			controller.action(ClearBehaviorEffects.class, component);
		}
		Array<VariableSelectorWidget> arrayWidgets = varOp
				.getVariablesWidgets();
		if (component != null && arrayWidgets.size == 0) {
			controller.action(RemoveBehavior.class, component);
			setUsed(false);
		} else {
			if (component == null) {
				component = new Behavior();
				controller.action(AddBehaviorPrefab.class, component,
						componentId);
			}
			for (VariableSelectorWidget varWidget : arrayWidgets) {
				changeVar = new ChangeVar();
				changeVar.setContext(varWidget.isLocal() ? Context.LOCAL
						: Context.GLOBAL);
				changeVar.setVariable(varWidget.getVarNameButton().getText()
						.toString());
				changeVar.setExpression(varWidget.getState());

				controller
						.action(AddBehaviorEffect.class, component, changeVar);
				setUsed(true);
			}
		}
	}
}
