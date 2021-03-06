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
package es.eucm.ead.editor.control.views;

import com.badlogic.gdx.scenes.scene2d.Actor;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.model.EditScene;
import es.eucm.ead.editor.ui.scenes.DesktopSceneEditor;
import es.eucm.ead.editor.ui.scenes.ribbon.SceneRibbon;
import es.eucm.ead.editor.ui.scenes.ribbon.interaction.BehaviorWidget;
import es.eucm.ead.editor.view.builders.ViewBuilder;
import es.eucm.ead.editor.view.widgets.layouts.LinearLayout;
import es.eucm.ead.editor.view.widgets.scenes.SceneEditor;

/**
 * View to show scene edition.
 * <dl>
 * <dt><strong>Arguments</strong></dt>
 * <dd><strong>args[0]</strong> <em>String</em> the identifier of edited scene</dd>
 * </dl>
 */
public class SceneView implements ViewBuilder {

	private Controller controller;

	private LinearLayout sceneView;

	private SceneEditor sceneEditor;

	@Override
	public void initialize(Controller controller) {
		this.controller = controller;
		sceneView = new LinearLayout(false);
		sceneEditor = new DesktopSceneEditor(controller);
		sceneView.add(new SceneRibbon(controller)).expandX();

		LinearLayout bottomContainer = new LinearLayout(true);
		sceneView.add(bottomContainer).expand(true, true).getActor().toBack();

		bottomContainer.add(new BehaviorWidget(controller)).expandY().top();
		bottomContainer.add(sceneEditor).expand(true, true).getActor().toBack();

		sceneView.setFillParent(true);
	}

	@Override
	public Actor getView(Object... args) {
		controller.action(EditScene.class, args);
		sceneEditor.prepare();
		return sceneView;
	}

	@Override
	public void release(Controller controller) {
		sceneEditor.release();
	}
}
