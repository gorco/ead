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
package es.eucm.ead.editor.control.actions.editor;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.EditorAction;
import es.eucm.ead.editor.view.widgets.modals.ModalContainer;
import es.eucm.ead.editor.view.widgets.modals.SpinnerModal;
import es.eucm.ead.editor.view.widgets.modals.SpinnerModal.SpinnerModalListener;

public class ShowSpinner extends EditorAction {

	private SpinnerModal spinnerModal;

	private ModalContainer container;

	public ShowSpinner() {
		super(true, false, String.class, String.class, Integer.class,
				Integer.class, Integer.class, SpinnerModalListener.class);
	}

	@Override
	public void initialize(Controller controller) {
		super.initialize(controller);
		container = new ModalContainer(controller.getApplicationAssets()
				.getSkin(), spinnerModal = new SpinnerModal(controller
				.getApplicationAssets().getSkin(), controller
				.getApplicationAssets().getI18N()));
	}

	@Override
	public void perform(Object... args) {
		String prefix = (String) args[0];
		String suffix = (String) args[1];
		Integer value = (Integer) args[2];
		Integer minValue = (Integer) args[3];
		Integer maxValue = (Integer) args[4];

		spinnerModal.set(prefix, suffix, value, minValue, maxValue);
		spinnerModal.setModalListener((SpinnerModalListener) args[5]);

		controller.getViews().showModal(container, 0, 0);
	}
}
