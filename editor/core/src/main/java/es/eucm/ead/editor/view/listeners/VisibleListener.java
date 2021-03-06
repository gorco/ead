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
package es.eucm.ead.editor.view.listeners;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

public class VisibleListener {

	private static final float ANIM_TIME = 0.15f;

	private Actor actor;

	public VisibleListener(Actor actor) {
		this.actor = actor;
		if (actor instanceof Group) {
			((Group) actor).setTransform(true);
		}
		if (actor instanceof Layout) {
			((Layout) actor).pack();
		}
		actor.setOrigin(Align.center);
	}

	public void setVisible(boolean visible) {
		if (actor.getActions().size > 0 || visible != actor.isVisible()) {
			actor.clearActions();
			if (visible) {
				actor.addAction(Actions.sequence(Actions.visible(true), Actions
						.rotateTo(0, 0), Actions.scaleTo(0.5f, 0.5f), Actions
						.parallel(Actions.fadeIn(ANIM_TIME), Actions.scaleTo(1,
								1, ANIM_TIME), Actions.rotateBy(360, ANIM_TIME,
								Interpolation.pow2Out))));
			} else {
				actor.addAction(Actions.sequence(Actions.rotateTo(0, 0),
						Actions.parallel(Actions.fadeOut(ANIM_TIME), Actions
								.scaleTo(0.5f, 0.5f, ANIM_TIME),
								Actions.rotateBy(-360, ANIM_TIME,
										Interpolation.pow2In)), Actions
								.visible(false)));
			}
		}
	}

}
