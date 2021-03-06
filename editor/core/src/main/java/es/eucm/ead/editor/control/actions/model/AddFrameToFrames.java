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
package es.eucm.ead.editor.control.actions.model;

import com.badlogic.gdx.utils.Array;

import es.eucm.ead.editor.control.actions.ModelAction;
import es.eucm.ead.editor.control.commands.Command;
import es.eucm.ead.editor.control.commands.ListCommand.AddToListCommand;
import es.eucm.ead.schema.renderers.Frame;
import es.eucm.ead.schema.renderers.Frames;

/**
 * Adds a {@link Frame} to a specified {@link Frames} array.
 * <dl>
 * <dt><strong>Arguments</strong></dt>
 * <dd><strong>args[0]</strong> <em>{@link Array}</em> The frames list</dd>
 * <dd><strong>args[1]</strong> <em>{@link Frame}</em> The item to be added into
 * the provided position args[2]</dd>
 * <dd><strong>args[2]</strong> <em>{@link Integer}</em> The new position of the
 * frame args[1]</dd>
 * </dl>
 */
public class AddFrameToFrames extends ModelAction {

	public AddFrameToFrames() {
		super(true, false, Array.class, Frame.class, Integer.class);
	}

	@Override
	public Command perform(Object... args) {

		Array array = (Array) args[0];

		return new AddToListCommand(null, array, args[1], (Integer) args[2]);
	}

}
