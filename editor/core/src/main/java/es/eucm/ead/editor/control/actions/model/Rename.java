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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import es.eucm.ead.editor.control.actions.EditorActionException;
import es.eucm.ead.editor.control.actions.ModelAction;
import es.eucm.ead.editor.control.commands.Command;
import es.eucm.ead.editor.control.commands.FieldCommand;
import es.eucm.ead.schemax.FieldName;
import es.eucm.ead.schema.entities.ModelEntity;

/**
 * Generic class for renaming any object that has a "name" property (
 * {@link es.eucm.ead.schemax.FieldName#NAME}). This action can receive a
 * variable number of arguments greater than zero. If it receives zero
 * arguments, an exception is thrown. Valid arguments are: args[0]: The object
 * to be renamed. It can be either an object of a type that declares a
 * {@link es.eucm.ead.schemax.FieldName#NAME} field, or a string representing
 * the id of an object of a type that declares a
 * {@link es.eucm.ead.schemax.FieldName#NAME} field. Since this class is
 * abstract, the actual implementation of this behaviour is a responsibility of
 * the child class.
 * 
 * Example: {@link RenameScene}. This action accepts either a
 * {@link ModelEntity} object representing a scene or a scene id (e.g.
 * "scene0"). If args[0] is a String, {@link RenameScene} searches the scene
 * that matches the given id=args[0] by overriding
 * {@link #findObjectById(String)}.
 * 
 * args[1]: The string with the new name to replace the old one. This argument
 * is optional. Either if it is missing (args.length==0) or if it is null, this
 * action does not modify the model nor throws an exception. However, subclasses
 * can override this behavior by overriding {@link #getNewValue()}. This is
 * useful, for example, if any subclass may want to ask the user to provide the
 * new name when this is null.
 * 
 * Created by Javier Torrente on 8/03/14.
 */
public abstract class Rename extends ModelAction {

	/**
	 * 0: object to be renamed 1: new name
	 * 
	 * @param args
	 */
	@Override
	public Command perform(Object... args) {
		// There should be at least one argument
		if (args.length == 0) {
			throw new EditorActionException("Error in action "
					+ this.getClass().getCanonicalName()
					+ ": cannot rename with zero arguments");
		}

		Object objectToRename = null;
		// IF first argument is null or a String, it has to be found. In this
		// case, findObjectById is invoked. Subclasses should override this
		// method
		// If they want to support this feature
		if (args[0] == null || args[0] instanceof String) {
			objectToRename = findObjectById(args[0] == null ? null
					: (String) args[0]);
		}
		// If the first argument is not to be found, it should have a name
		// attribute declared. Otherwise, throw exception
		else {
			objectToRename = args[0];
			if (!objectHasName(objectToRename)) {
				throw new EditorActionException(
						"Error in action "
								+ this.getClass().getCanonicalName()
								+ ": cannot rename an element if it does not declare a "
								+ FieldName.NAME + " attribute");
			}
		}

		// At this point, if objectToRename is still null, return without doing
		// anything
		if (objectToRename == null)
			return null;

		// Get the oldValue.
		String oldValue = null;
		try {
			Class objectClass = objectToRename.getClass();
			Field nameField = ClassReflection.getDeclaredField(objectClass,
					FieldName.NAME);
			nameField.setAccessible(true);
			oldValue = (String) nameField.get(objectToRename);
		} catch (ReflectionException e) {
			Gdx.app.error(this.getClass().getCanonicalName(),
					"Unexpected exception: This should never happen", e);
		}

		// Now check the second argument.
		String newValue = null;
		if (args.length >= 2 && args[1] != null) {
			// If second argument exists and it's not null, it should be a
			// String
			if (!(args[1] instanceof String)) {
				throw new EditorActionException(
						"Error in action "
								+ this.getClass().getCanonicalName()
								+ ": newValue has incompatible type (String supported only)");
			}
			// If it is a new String, the whole value is replaced
			if (args[1] instanceof String) {
				newValue = (String) args[1];
			}
		}
		// If second argument is missing, or it's null, ask for new Value.
		// Subclasses should override this if they want to support this feature
		else {
			newValue = getNewValue();
		}

		if (newValue != null
				&& (oldValue == null || !oldValue.equals(newValue))) {
			return new FieldCommand(objectToRename, FieldName.NAME, newValue,
					true);
		}
		return null;
	}

	/**
	 * Checks that the class of the object that has to be renamed has a
	 * {@link es.eucm.ead.schemax.FieldName#NAME} property.
	 * 
	 * @param object
	 *            The object that should have a "name" field
	 * @return True if {@code object} has a "name" field that can be accessed by
	 *         reflection, false otherwise.
	 */
	private boolean objectHasName(Object object) {
		try {
			return ClassReflection.getDeclaredField(object.getClass(),
					FieldName.NAME) != null;
		} catch (ReflectionException e) {
			return false;
		}
	}

	/**
	 * This method finds the object that has to be renamed given its id (e.g.
	 * "scene0"). {@link #findObjectById(String)} is only invoked when the first
	 * argument of the {@link Rename} action is not the object that has to be
	 * renamed, but its identifier. If {@link #findObjectById(String)} returns
	 * null, no exception is thrown and the action does not modify the model.
	 * 
	 * By default, this method returns {@code null} and has no effect on the
	 * action. It is provided as a convenient stub for subclasses that may want
	 * to feed the action with identifiers instead of with the whole object. See
	 * {@link RenameScene} for more details.
	 * 
	 * @param id
	 *            The identifier for seeking the object that has to be renamed
	 *            (e.g. "scene0"). It may be null if the id is unknown.
	 *            Subclasses overriding this method may want to ask the user to
	 *            provide the id of the object if {@code id} is null.
	 * @return The object whose id matches {@code id} ({@code null} by default).
	 *         It may be null if the object is not found. If the object returned
	 *         is null, then nothing happens (neither exception nor modification
	 *         of the model).
	 */
	protected Object findObjectById(String id) {
		return null;
	}

	/**
	 * This method gets the new name for the object being renamed. It is only
	 * invoked if the new value is not fed to this action, or if it is null.
	 * 
	 * By default, this method returns {@code null} and has no effect on the
	 * action. It is provided as a convenient stub for subclasses that may want
	 * to ask the user to provide the new name through a modal dialog, for
	 * example, instead of feeding the action with it.
	 * 
	 * @return The new name to be set ({@code null} by default).
	 */
	protected String getNewValue() {
		return null;
	}
}
