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
import es.eucm.ead.editor.control.actions.editor.ExecuteWorker;
import es.eucm.ead.editor.control.actions.editor.Play;
import es.eucm.ead.editor.control.workers.LoadLibraryEntities;
import es.eucm.ead.editor.control.workers.Worker;
import es.eucm.ead.editor.view.widgets.WidgetBuilder;
import es.eucm.ead.editor.view.widgets.galleries.basegalleries.ThumbnailsGallery;
import es.eucm.ead.editor.view.widgets.layouts.Gallery.GalleryStyle;
import es.eucm.ead.schema.editor.components.repo.RepoCategories;
import es.eucm.ead.schema.editor.components.repo.RepoElement;
import es.eucm.ead.schemax.ModelStructure;

public class CategoryLibrary extends ThumbnailsGallery implements
		Worker.WorkerListener {

	protected Controller controller;

	protected String category;

	public CategoryLibrary(float rows, int columns, Controller controller) {
		this(rows, columns, "all", controller);
	}

	public CategoryLibrary(float rows, int columns, String category,
			Controller controller) {
		super(rows, columns, controller.getApplicationAssets(), controller
				.getApplicationAssets().getSkin(), controller
				.getApplicationAssets().getI18N(), controller
				.getApplicationAssets().getSkin().get(GalleryStyle.class));
		this.controller = controller;
		this.category = category;
	}

	public void changeCategory(String newCategory) {
		this.category = newCategory;
	}

	@Override
	public void loadContents(String string) {
		clear();
		controller.action(ExecuteWorker.class, LoadLibraryEntities.class, this,
				category);
	}

	@Override
	public void start() {
	}

	@Override
	public void result(Object... results) {
		addTile(results[0], (String) results[1], (String) results[2]);
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

    @Override
    protected void prepareActionButton(Actor actor) {

    }

    @Override
    protected void prepareGalleryItem(Actor actor, Object id) {
        if (id instanceof RepoElement) {
            RepoElement selected = (RepoElement) id;
            if (selected.getCategoryList()
                    .contains(RepoCategories.MOKAPS, true)) {
                WidgetBuilder
                        .actionsOnClick(
                                actor,
                                new Class[] { Play.class },
                                new Object[][] {
                                        new Object[] { controller
                                                .getLibraryManager()
                                                .getRepoElementLibraryFolder(
                                                        (RepoElement) id)
                                                .file().getAbsolutePath()
                                                + "/"
                                                + ModelStructure.CONTENTS_FOLDER },
                                        new Object[] {} });
            }
        }
    }


}
