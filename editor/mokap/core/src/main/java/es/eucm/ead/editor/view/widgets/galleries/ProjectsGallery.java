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
package es.eucm.ead.editor.view.widgets.galleries;

import com.badlogic.gdx.scenes.scene2d.Actor;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.editor.AddProject;
import es.eucm.ead.editor.control.actions.editor.ExecuteWorker;
import es.eucm.ead.editor.control.actions.editor.OpenProject;
import es.eucm.ead.editor.control.workers.LoadProjects;
import es.eucm.ead.editor.control.workers.Worker.WorkerListener;
import es.eucm.ead.editor.view.widgets.WidgetBuilder;

public class ProjectsGallery extends ThumbnailsGallery implements
		WorkerListener {

	private Controller controller;

	public ProjectsGallery(float rowHeight, int columns, Controller controller) {
		super(rowHeight, columns, controller.getApplicationAssets(), controller
				.getApplicationAssets().getSkin(), controller
				.getApplicationAssets().getI18N());
		this.controller = controller;
	}

	public void prepare() {
		controller.action(ExecuteWorker.class, LoadProjects.class, this);
	}

	@Override
	protected void prepareAddButton(Actor actor) {
		WidgetBuilder.actionOnClick(actor, AddProject.class);
	}

	@Override
	protected void prepareGalleryItem(Actor actor, String id) {
		WidgetBuilder.actionOnClick(actor, OpenProject.class, id);
	}

	@Override
	public void start() {
		clear();
	}

	@Override
	public void result(Object... results) {
		addTile((String) results[0], (String) results[1], (String) results[2]);
	}

	@Override
	public void done() {

	}

	@Override
	public void error(Throwable ex) {

	}

	@Override
	public void cancelled() {

	}
}
