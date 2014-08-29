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
package es.eucm.ead.editor.view.widgets.editionview;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.view.widgets.IconButton;
import es.eucm.ead.editor.view.widgets.Toolbar;
import es.eucm.ead.editor.view.widgets.editionview.prefabs.ChangeVariablePanel;
import es.eucm.ead.editor.view.widgets.editionview.prefabs.DeparturePanel;
import es.eucm.ead.editor.view.widgets.editionview.prefabs.TweensPanel;
import es.eucm.ead.editor.view.widgets.editionview.prefabs.VisibilityPanel;

public class LeftEditionToolbar extends Toolbar {

	private float width;

	public LeftEditionToolbar(Controller controller, String style, float width,
			float iconSize, float PAD) {
		super(controller.getApplicationAssets().getSkin(), style);

		Skin skin = controller.getApplicationAssets().getSkin();

		this.width = width;

		align(Align.center);

		addInNewRow(
				new DeparturePanel("gateway_reverse80x80", iconSize,
						controller, this)).padBottom(PAD);
		addInNewRow(
				new ChangeVariablePanel("variable80x80", iconSize, controller,
						this)).padBottom(PAD);

		// TODO change
		IconButton sound = new IconButton("sound80x80", 0, skin);
		addInNewRow(sound).size(iconSize).padBottom(PAD);

		// TODO change
		IconButton conversations = new IconButton("conversation80x80", 0, skin);
		addInNewRow(conversations).size(iconSize).padBottom(PAD);

		addInNewRow(
				new VisibilityPanel("visibility80x80", iconSize, controller,
						this)).padBottom(PAD);

		// TODO change
		IconButton lock = new IconButton("lock80x80", 0, skin);
		addInNewRow(lock).size(iconSize).padBottom(PAD);

		addInNewRow(new TweensPanel("tween80x80", iconSize, controller, this))
				.padBottom(PAD);
	}

	@Override
	public float getPrefWidth() {
		return width;
	}

}