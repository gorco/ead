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

import es.eucm.ead.schema.components.behaviors.Behavior;
import es.eucm.ead.schema.effects.Effect;

/**
 * Changes a {@link Effect} in a {@link Behavior} for other of the same type
 * </p>
 * <dl>
 * <dt><strong>Arguments</strong></dt>
 * <dd><strong>args[0]</strong> <em>{@link Behavior}</em></dd>
 * <dd><strong>args[1]</strong> <em>{@link Effect}</em> to change</dd>
 * </dl>
 */
public class ChangeBehaviorEffect extends EditorAction {

	public ChangeBehaviorEffect() {
		super(true, false, Behavior.class, Effect.class);
	}

	@Override
	public void perform(Object... args) {
		Behavior behavior = (Behavior) args[0];
		Effect effect = (Effect) args[1];
		Class c = effect.getClass();

		int i = 0;
		Effect old = null;
		for (Effect e : behavior.getEffects()) {
			if (e.getClass() == c) {
				old = e;
				break;
			}
			i++;
		}
		behavior.getEffects().insert(i, effect);

		if (old != null) {
			behavior.getEffects().removeValue(old, true);
		}
	}

}