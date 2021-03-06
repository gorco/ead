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
package es.eucm.ead.editor.view.widgets;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import es.eucm.ead.editor.control.Views;
import es.eucm.ead.editor.view.Modal;

/**
 * A panel with a scale animation.
 */
public class ContextMenu extends ScrollPane implements Modal {

	protected static final float FADE = .5F;

	protected static final float CHILD_DELAY_OFFSET = .035F;

	protected static final float CHILD_FADE = .15F;

	protected static final float HIDE_OFFSET = .05F;

	private Array<Runnable> hideRunnables = new Array<Runnable>();

	private Table table;

	public ContextMenu() {
		super(new Table());
		table = (Table) getWidget();
		table.setTransform(true);
	}

	public Cell add(Actor actor) {
		return table.add(actor);
	}

	public Cell row() {
		return table.row();
	}

	public void setBackground(Drawable background) {
		table.setBackground(background);
	}

	public ContextMenu background(Drawable background) {
		table.background(background);
		return this;
	}

	public ContextMenu pad(float pad) {
		table.pad(pad);
		return this;
	}

	public void addHideRunnable(Runnable runnable) {
		hideRunnables.add(runnable);
	}

	/**
	 * The default animation used to show the {@link ContextMenu}.
	 * 
	 * @param views
	 */
	@Override
	public void show(Views views) {
		float xDuration, yDuration;
		float w = getPrefWidth(), h = getPrefHeight();
		if (w > h) {
			yDuration = FADE * h / w;
			xDuration = FADE;
		} else {
			xDuration = FADE * w / h;
			yDuration = FADE;
		}
		float minDuration = Math.min(xDuration, yDuration);
		setScale(0, 0);
		clearActions();
		getColor().a = 0f;
		addAction(Actions.sequence(Actions.parallel(
				Actions.fadeIn(minDuration, Interpolation.fade),
				Actions.scaleBy(1f, 0f, xDuration, Interpolation.pow2Out),
				Actions.scaleBy(0f, 1f, yDuration, Interpolation.pow2Out)),
				Actions.scaleTo(1.0f, 1.0f)));

		Array<Cell> cells = table.getCells();
		for (int i = 0; i < cells.size; ++i) {
			Actor actor = cells.get(i).getActor();
			if (actor != null) {
				actor.getColor().a = 0.0f;
				actor.addAction(Actions.delay(minDuration + i
						* CHILD_DELAY_OFFSET,
						Actions.fadeIn(CHILD_FADE, Interpolation.fade)));
			}
		}
	}

	@Override
	public void hide(Runnable runnable) {
		addAction(getHideAction(runnable));
	}

	protected SequenceAction getHideAction(Runnable runnable) {
		SequenceAction preHide = Actions.sequence();
		for (Runnable r : hideRunnables) {
			preHide.addAction(Actions.run(r));
		}
		SequenceAction hide = Actions.sequence(Actions.parallel(Actions
				.fadeOut(FADE, Interpolation.fade), Actions.moveBy(0f,
				getHeight() * HIDE_OFFSET, FADE, Interpolation.exp5Out)));

		if (runnable != null) {
			hide.addAction(Actions.run(runnable));
		}
		preHide.addAction(hide);
		return preHide;
	}

	@Override
	public boolean hideAlways() {
		return true;
	}

}
