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
package es.eucm.ead.editor.view.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.editor.assets.EditorGameAssets;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.Selection;
import es.eucm.ead.editor.control.actions.model.ChangeSelectionText;
import es.eucm.ead.editor.model.Q;
import es.eucm.ead.editor.platform.Platform;
import es.eucm.ead.editor.view.builders.scene.SceneEditor;
import es.eucm.ead.editor.view.widgets.draw.SlideColorPicker.ColorEvent;
import es.eucm.ead.editor.view.widgets.draw.SlideColorPicker.ColorListener;
import es.eucm.ead.schema.entities.ModelEntity;

public class LabelTextEditor extends TextEditor implements
		Input.TextInputListener {

	private Controller controller;

	private Label labelActor;

	private EditorGameAssets editorGameAssets;

	private es.eucm.ead.schema.components.controls.Label modelLabel;

	public LabelTextEditor(final Skin skin, Controller c) {
		super(skin, c.getApplicationAssets().getI18N(), c.getPreferences());

		this.controller = c;
		this.editorGameAssets = c.getEditorGameAssets();

		top.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Platform) controller.getPlatform()).getMultilineTextInput(
						LabelTextEditor.this, i18n.m("toolbar.text.input"),
						labelActor.getText().toString(), i18n);
			}
		});

		addListener(new ColorListener() {
			@Override
			public void colorChanged(ColorEvent event) {
				if (!event.getColor().equals(
						Q.toLibgdxColor(modelLabel.getColor()))) {
					labelActor.setColor(event.getColor());
					if (!event.isDragging()) {
						controller.action(ChangeSelectionText.class,
								event.getColor());
					}
				}
			}
		});

		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (event.getTarget() instanceof SelectBox) {
					if (!modelLabel.getStyle().equals(getStyleName())) {
						controller.action(ChangeSelectionText.class,
								getStyleName(), false);
					}
				}
			}
		});

		pack();
	}

	@Override
	public void input(String text) {
		if (text != null && !text.trim().isEmpty()) {
			updateText(text);
			labelActor.setText(text);
			controller.action(ChangeSelectionText.class, text, true);
		}
	}

	@Override
	public void canceled() {

	}

	public void prepare(SceneEditor sceneEditor) {
		ModelEntity element = (ModelEntity) controller.getModel()
				.getSelection().getSingle(Selection.SCENE_ELEMENT);
		if (Q.hasComponent(element,
				es.eucm.ead.schema.components.controls.Label.class)) {
			modelLabel = Q.getComponent(element,
					es.eucm.ead.schema.components.controls.Label.class);
			for (Actor actor : ((Group) sceneEditor.getGroupEditor().findActor(
					element)).getChildren()) {
				if (actor instanceof Label) {
					labelActor = (Label) actor;
					break;
				}
			}
			Color color = Q.toLibgdxColor(modelLabel.getColor());
			labelActor.setColor(color);
			labelActor.setStyle(editorGameAssets.getSkin().get(
					modelLabel.getStyle(), LabelStyle.class));
			updatePaneText(modelLabel.getText(), color);
			updateSelectedStyle(modelLabel.getStyle());
		}
	}

}
