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
package es.eucm.ead.editor.view.widgets.baseview;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import es.eucm.ead.editor.control.MokapController.BackListener;

/**
 * Right panel in base view
 */
public class SelectionContext extends Panel implements BackListener {

	private Actor selectionContext;

	public void setSelectionContext(Actor selectionContext) {
		if (this.selectionContext != null) {
			this.selectionContext.addAction(Actions.sequence(
					Actions.moveTo(getWidth(), 0, 0.2f, Interpolation.exp5Out),
					Actions.removeActor()));
		}
		this.selectionContext = selectionContext;
		if (selectionContext != null) {
			addActor(selectionContext);
		}
	}

	@Override
	public void displace(float deltaX, float deltaY) {
		selectionContext.setX(Math.min(Math.max(
				getWidth() - selectionContext.getWidth(),
				selectionContext.getX() + deltaX), getWidth()));
	}

	@Override
	public void show() {
		super.show();
		if (selectionContext != null) {
			BaseView.moveTo(selectionContext,
					getWidth() - selectionContext.getWidth(),
					selectionContext.getY());
		}
	}

	@Override
	public void hide() {
		super.hide();
		if (selectionContext != null) {
			BaseView.moveTo(selectionContext, getWidth(),
					selectionContext.getY());
		}
	}

	@Override
	public boolean isVelocityToHide(float velocityX, float velocityY) {
		return velocityX > cmToXPixels(BaseView.FLING_MIN_VELOCITY_CM);
	}

	@Override
	public boolean isHalfShown() {
		return selectionContext.getX() < getWidth()
				- selectionContext.getWidth() / 2.0f;
	}

	@Override
	public boolean hasContent() {
		return selectionContext != null;
	}

	@Override
	public void layout() {
		super.layout();
		if (selectionContext != null) {
			float newWidth = getPrefWidth(selectionContext);
			if (!MathUtils.isEqual(newWidth, selectionContext.getWidth(), 1.1f)) {
				float height = Math.max(getPrefHeight(selectionContext),
						getHeight());
				setBounds(selectionContext, getWidth(), getHeight() - height,
						getPrefWidth(selectionContext), height);
			}
		}
	}

	@Override
	public boolean onBackPressed() {
		if (!hidden) {
			hide();
			return true;
		}
		return false;
	}
}
