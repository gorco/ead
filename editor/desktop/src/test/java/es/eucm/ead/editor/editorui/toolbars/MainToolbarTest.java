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
package es.eucm.ead.editor.editorui.toolbars;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import es.eucm.ead.editor.editorui.EditorUITest;
import es.eucm.ead.editor.ui.maintoolbar.MainToolbar;
import es.eucm.ead.editor.view.widgets.PlaceHolder;

public class MainToolbarTest extends EditorUITest {
	@Override
	protected void builUI(Group root) {
		Skin skin = controller.getApplicationAssets().getSkin();
		MainToolbar toolbar = new MainToolbar(controller);

		PlaceHolder placeHolder = new PlaceHolder();
		placeHolder.setBackground(skin.getDrawable("blank"));
		placeHolder.setFillParent(true);

		placeHolder.setContent(toolbar);
		root.addActor(placeHolder);
	}

	public static void main(String[] args) {
		new LwjglApplication(new MainToolbarTest(), "Scene Editor test", 300,
				70);
	}
}
