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
package es.eucm.ead.editor.view.widgets.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import es.eucm.ead.editor.view.widgets.FixedButton;
import es.eucm.ead.editor.view.widgets.StretchableButton;

/**
 * Horizontal {@link LinearLayout} with drag and drop feature. Allow to move its
 * children. If share the {@link DragAndDrop} you can make that can move the
 * actors to other {@link TrackLayout}.
 * 
 * Its children have a left margin respected to origin. This margin corresponds
 * to X coordinate of child in the layout.
 * 
 * The layout allow that a {@link FixedButton} is overlapped with other
 * children.
 * 
 */
public class TrackLayout extends LinearLayout {

	private DragAndDrop dragNDrop;

	/**
	 * Creates a new {@link TrackLayout} with a new {@link DragAndDrop} and
	 * without background.
	 * 
	 */
	public TrackLayout() {
		this(null, new DragAndDrop());
	}

	/**
	 * Creates a new {@link TrackLayout} with a dragNDrop {@link DragAndDrop}
	 * and without background.
	 * 
	 * @param dragNDrop
	 */
	public TrackLayout(DragAndDrop dragNDrop) {
		this(null, dragNDrop);
	}

	/**
	 * Creates a new {@link TrackLayout} with a new {@link DragAndDrop} and a
	 * background.
	 * 
	 * @param background
	 */
	public TrackLayout(Drawable background) {
		this(background, new DragAndDrop());
	}

	/**
	 * Creates a new {@link TrackLayout} with a dragNDrop {@link DragAndDrop}
	 * and a background.
	 * 
	 * @param background
	 * @param dragNDrop
	 */
	public TrackLayout(Drawable background, DragAndDrop dragNDrop) {
		super(true, background);

		this.dragNDrop = dragNDrop;

		dragNDrop.addTarget(new Target(this) {
			@Override
			public boolean drag(Source source, Payload payload, float x,
					float y, int pointer) {
				return true;
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y,
					int pointer) {
				int index = 0;
				for (Actor actor : TrackLayout.this.getChildren()) {
					if (x > getLeftMargin(actor) + actor.getWidth() / 2) {
						index++;
					}
				}
				TrackLayout.this.add(index, source.getActor(), x);
				layout();
			}
		});
	}

	@Override
	public TrackConstraints add(int index, Actor actor) {
		return add(index, actor, 0);
	}

	@Override
	public TrackConstraints add(Actor actor) {
		return add(-1, actor, 0);
	}

	/**
	 * Adds a widget with left margin to the container
	 * 
	 * @param actor
	 *            the widget to add
	 * @param leftMargin
	 *            the left margin to the widget
	 * @return the constraints for the widget
	 */
	public TrackConstraints add(Actor actor, float leftMargin) {
		return add(-1, actor, leftMargin);
	}

	/**
	 * Adds a widget with left margin to the container
	 * 
	 * @param index
	 *            position to add the actor
	 * @param actor
	 *            the widget to add
	 * @param leftMargin
	 *            the left margin to the widget
	 * @return the constraints for the widget
	 */
	public TrackConstraints add(int index, final Actor actor, float leftMargin) {
		TrackConstraints c = new TrackConstraints(actor);
		c.margin.setLeft(leftMargin);
		c.setWidth(actor.getWidth());
		if (index == -1) {
			constraints.add(c);
			addActor(actor);
		} else {
			constraints.insert(index, c);
			addActorAt(index, actor);
		}

		dragNDrop.addSource(new Source(actor) {

			private float firstX = getLeftMargin(actor);
			private int index = TrackLayout.this.getChildren().indexOf(actor,
					true);

			@Override
			public Payload dragStart(InputEvent event, float x, float y,
					int pointer) {

				Payload payload = new Payload();

				if (actor instanceof StretchableButton) {
					StretchableButton aux = (StretchableButton) actor;
					if (aux.isDragLeft() || aux.isDragRight()) {
						return null;
					}
				}

				payload.setObject(index);
				payload.setDragActor(actor);
				return payload;
			}

			@Override
			public void dragStop(InputEvent event, float x, float y,
					int pointer, Payload payload, Target target) {
				if (target == null) {
					TrackLayout.this.add(index, actor, firstX);
					layout();
				}
			}
		});

		return c;
	}

	public float getLeftMargin(Actor actor) {
		for (Constraints c : constraints) {
			if (c.actor == actor) {
				return marginLeft(c);

			}
		}
		return 0;
	}

	public void setLeftMargin(Actor actor, float leftMargin) {
		for (Constraints c : constraints) {
			if (c.actor == actor) {
				((TrackConstraints) c).marginLeft(leftMargin);
			}
		}
	}

	@Override
	public void layout() {
		// Check if there is enough width to layout all children without problem
		float childrenWidth = prefWidth() - paddingWidth();
		float availableWidth = availableWidth();

		// If there is no space, ignore container padding and check again
		boolean ignorePadding = childrenWidth > availableWidth;
		float beforeX = 0;
		float lastW = 0;

		if (ignorePadding) {
			availableWidth += paddingWidth();
		}

		float leftX = ignorePadding ? 0.0f : paddingLeft();

		for (Constraints c : constraints) {
			if (c.getActor().isVisible() || computeInvisibles) {
				TrackConstraints tCons = (TrackConstraints) c;

				Actor actor = tCons.getActor();
				float width = actorWidth(actor) + 0.0f;

				float x = 0;

				float auxX = leftX + marginLeft(tCons);

				if (actor instanceof StretchableButton
						&& ((StretchableButton) actor).isDragLeft()) {
					if (beforeX + lastW <= auxX + tCons.getWidth() - width) {
						tCons.marginLeft(marginLeft(tCons)
								+ (tCons.getWidth() - width));
					}
				} else if (auxX < beforeX + lastW
						&& !(actor instanceof FixedButton)) {
					tCons.marginLeft(beforeX + lastW);
				}

				if (actor instanceof FixedButton) {
					actor.toFront();
					x = marginLeft(tCons);
				} else {
					x = leftX + marginLeft(tCons);
					beforeX = x;
					lastW = width;
					tCons.setWidth(width);
				}

				float height = expandY(tCons) ? containerHeight()
						- paddingHeight() - marginHeight(tCons)
						: actorHeight(actor);

				float y = getYAligned(tCons, height);

				setBoundsForActor(actor, x, y, width, height);
			}
		}
	}

	/**
	 * Holds contraints for a widget inside a TrackLayout container
	 */
	public static class TrackConstraints extends Constraints {

		private float w;

		public TrackConstraints(Actor actor) {
			super(actor);
		}

		public TrackConstraints setWidth(float m) {
			w = m;
			return this;
		}

		public float getWidth() {
			return w;
		}

		public TrackConstraints marginLeft(float m) {
			margin.setLeft(m);
			return this;
		}
	}
}