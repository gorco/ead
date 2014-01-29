/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
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
package es.eucm.ead.engine.tests;

import es.eucm.ead.engine.Assets;
import es.eucm.ead.engine.GameLoop;
import es.eucm.ead.engine.SceneView;
import es.eucm.ead.engine.mock.MockGame;
import es.eucm.ead.schema.actors.Scene;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameLoopTest {

	private GameLoop gameLoop;

	private Assets assets;

	private SceneView sceneView;

	@Before
	public void setUp() {
		MockGame mockGame = new MockGame();
		gameLoop = mockGame.getGameLoop();
		assets = gameLoop.getAssets();
		assets.setGamePath("schema", true);
		sceneView = gameLoop.getSceneView();
	}

	@Test
	public void testLoadGame() {
		gameLoop.loadGame();
		testSceneLoaded();
	}

	@Test
	public void testLoadScene() {
		gameLoop.loadGame();
		gameLoop.loadScene("initial");
		testSceneLoaded();
	}

	@Test
	public void testReloadScene() {
		gameLoop.loadGame();
		gameLoop.loadScene("initial");
		testSceneLoaded();
		Scene currentScene = sceneView.getCurrentScene().getSchema();
		gameLoop.reloadCurrentScene();
		assets.finishLoading();
		gameLoop.act(0);
		Scene newScene = sceneView.getCurrentScene().getSchema();
		// if pointers are different, the scene has been reloaded in a new
		// object
		assertTrue(currentScene != newScene);
	}

	private void testSceneLoaded() {
		assertEquals(gameLoop.getCurrentScene(), "initial");
		assertTrue(gameLoop.isLoading());
		assets.finishLoading();
		gameLoop.act(0);
		Scene currentScene = sceneView.getCurrentScene().getSchema();
		assertNotNull(currentScene);
		assertEquals(currentScene.getChildren().size(), 1);
	}
}