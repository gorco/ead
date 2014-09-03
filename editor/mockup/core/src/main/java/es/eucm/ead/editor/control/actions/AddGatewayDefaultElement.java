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
package es.eucm.ead.editor.control.actions;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import es.eucm.ead.editor.control.ComponentId;
import es.eucm.ead.editor.control.Selection;
import es.eucm.ead.editor.control.actions.model.scene.NewScene;
import es.eucm.ead.editor.control.commands.Command;
import es.eucm.ead.editor.control.commands.CompositeCommand;
import es.eucm.ead.editor.control.commands.ListCommand.AddToListCommand;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.model.Q;
import es.eucm.ead.engine.assets.Assets.AssetLoadedCallback;
import es.eucm.ead.schema.components.ModelComponent;
import es.eucm.ead.schema.components.behaviors.Behavior;
import es.eucm.ead.schema.components.behaviors.events.Touch;
import es.eucm.ead.schema.editor.components.GameData;
import es.eucm.ead.schema.effects.GoScene;
import es.eucm.ead.schema.entities.ModelEntity;
import es.eucm.ead.schemax.entities.ResourceCategory;

public class AddGatewayDefaultElement extends ModelAction {
	
	public AddGatewayDefaultElement() {
		super(true, false, ModelEntity.class);
	}

	@Override
	public Command perform(Object... args) {
		final GameData gameData = Q.getComponent(controller.getModel().getGame(),
				GameData.class);
		
		Model model = controller.getModel();

		final ModelEntity editedScene = (ModelEntity) model.getSelection().getSingle(
				Selection.SCENE);

		// creates the new scene with a new element
		String id = model.createId(ResourceCategory.SCENE);
		CompositeCommand command;
		ModelEntity new_scene;

		new_scene = (ModelEntity) args[0];

		command = controller.getActions().getAction(NewScene.class)
				.perform("", id, new_scene);

		FileHandle internal = controller.getApplicationAssets().resolve(
				"predefined/door_in.png");
		ModelEntity exit = null;
		if (internal.exists()) {
			String path = controller.getEditorGameAssets()
					.copyToProjectIfNeeded(internal, Texture.class);
			exit = controller.getTemplates().createSceneElement(
					controller.getEditorGameAssets().getLoadingPath() + path);
		} else {
			return null;
		}
		command.addCommand(new AddToListCommand(new_scene, new_scene
				.getChildren(), exit));

		// add to old scene a new element
		FileHandle internal2 = controller.getApplicationAssets().resolve(
				"predefined/door_out.png");

		String path = controller.getEditorGameAssets()
				.copyToProjectIfNeeded(internal2, Texture.class);
		final ModelEntity entry = controller.getTemplates().createSceneElement(
				controller.getEditorGameAssets().getLoadingPath()
						+ path, 0 , 0);

		controller.getEditorGameAssets().get(path, Texture.class, new AssetLoadedCallback<Texture>() {
			@Override
			public void loaded(String fileName, Texture asset) {
				int lastExits = 0;
				for (ModelEntity elements : editedScene.getChildren()) {
					for (ModelComponent component : elements.getComponents()) {
						if (component.getId() != null && component.getId().equals(ComponentId.PREFAB_EXIT)) {
							lastExits++;
							break;
						}
					}
				}
				int x = lastExits*asset.getWidth()%gameData.getWidth();
				int row = MathUtils.floor((lastExits+1)*asset.getWidth()/gameData.getWidth());

				entry.setX(x <= gameData.getWidth()-asset.getWidth() ? x : 0);
				entry.setY(row*asset.getHeight());
			}
		}, true);

		
		command.addCommand(new AddToListCommand(editedScene, editedScene
				.getChildren(), entry));

		// creates and add the behavior with a GoScene effect
		Behavior behavior_out = new Behavior();
		behavior_out.setId(ComponentId.PREFAB_EXIT);
		behavior_out.setEvent(new Touch());
		GoScene go2 = new GoScene();
		go2.setSceneId(model.getIdFor(editedScene));
		behavior_out.getEffects().add(go2);

		command.addCommand(new AddToListCommand(exit, exit.getComponents(),
				behavior_out));

		// creates and add the behavior with a GoScene effect
		Behavior behavior_in = new Behavior();
		behavior_in.setId(ComponentId.PREFAB_EXIT);
		behavior_in.setEvent(new Touch());
		GoScene go = new GoScene();
		go.setSceneId(id);
		behavior_in.getEffects().add(go);
		command.addCommand(new AddToListCommand(entry, entry.getComponents(),
				behavior_in));

		return command;
	}

}
