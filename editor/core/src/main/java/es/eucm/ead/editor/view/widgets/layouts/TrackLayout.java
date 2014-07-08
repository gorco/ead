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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import es.eucm.ead.editor.view.widgets.AbstractWidget;
import es.eucm.ead.editor.view.widgets.StretchableButton;

public class TrackLayout extends AbstractWidget {

	private boolean computeInvisibles;

	private Drawable background;

	protected Array<Constraints> constraints;

	protected Insets padding;

	private DragAndDrop dragNDrop;

	public TrackLayout() {
		this(null, new DragAndDrop());
	}

	public TrackLayout(DragAndDrop dragNDrop) {
		this(null, dragNDrop);
	}

	public TrackLayout(Drawable background) {
		this(background, new DragAndDrop());
	}

	public TrackLayout(Drawable background, DragAndDrop dragNDrop) {
		this.dragNDrop = dragNDrop;
		this.background = background;
		this.constraints = new Array<Constraints>();
		this.padding = new Insets();

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

	/**
	 * Sets if invisible widgets should be taken into account for the layout
	 */
	public void setComputeInvisibles(boolean computeInvisibles) {
		this.computeInvisibles = computeInvisibles;
	}

	/**
	 * Sets background for the widget
	 */
	public TrackLayout background(Drawable background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the padding for the widget
	 */
	public TrackLayout pad(float padding) {
		this.padding.set(padding);
		return this;
	}

	/**
	 * Sets the padding for the widget
	 */
	public TrackLayout pad(float left, float top, float right, float bottom) {
		this.padding.set(left, top, right, bottom);
		return this;
	}

	/**
	 * Adds a widget to the container
	 * 
	 * @param index
	 *            position to add the actor
	 * @param actor
	 *            the widget to add
	 * @return the constraints for the widget
	 */
	public Constraints add(int index, Actor actor) {
		Constraints c = add(index, actor, 0);
		return c;
	}

	/**
	 * Adds a widget with margin to the container
	 * 
	 * @param index
	 *            position to add the actor
	 * @param actor
	 *            the widget to add
	 * @return the constraints for the widget
	 */
	public Constraints add(int index, final Actor actor, float margin) {
		Constraints c = new Constraints(actor);
		c.margin.setLeft(margin);
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

	/**
	 * Adds a widget to the container
	 * 
	 * @param actor
	 *            the widget to add
	 * @return the constraints for the widget
	 */
	public Constraints add(Actor actor) {
		return add(-1, actor);
	}

	/**
	 * Adds a widget with margin to the container
	 * 
	 * @param actor
	 *            the widget to add
	 * @param margin
	 *            the margin to the widget
	 * @return the constraints for the widget
	 */
	public Constraints add(Actor actor, float margin) {
		return add(-1, actor, margin);
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
		batch.setColor(Color.WHITE);
		if (background != null) {
			background.draw(batch, 0, 0, getWidth(), getHeight());
		}
		super.drawChildren(batch, parentAlpha);
	}

	@Override
	public float getPrefWidth() {
		return prefWidth();
	}

	@Override
	public float getPrefHeight() {
		return prefHeight();
	}

	@Override
	public boolean removeActor(Actor actor) {
		for (Constraints c : constraints) {
			if (c.actor == actor) {
				constraints.removeValue(c, true);
				break;
			}
		}
		return super.removeActor(actor);
	}

	public float getLeftMargin(Actor actor) {
		for (Constraints c : constraints) {
			if (c.actor == actor) {
				return marginLeft(c);

			}
		}
		return -1;
	}

	public void setLeftMargin(Actor actor, float margin) {
		for (Constraints c : constraints) {
			if (c.actor == actor) {
				c.marginLeft(margin);
			}
		}
	}

	@Override
	public void clearChildren() {
		constraints.clear();
		super.clearChildren();
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
			if (c.actor.isVisible() || computeInvisibles) {
				Actor actor = c.actor;
				float width = actorWidth(actor) + 0.0f;

				float x = 0;

				float auxX = leftX + marginLeft(c);

				if (actor instanceof StretchableButton
						&& ((StretchableButton) actor).isDragLeft()) {
					if (beforeX + lastW <= auxX + c.getWidth() - width) {
						c.marginLeft(marginLeft(c) + (c.getWidth() - width));
					}
				} else if (auxX < beforeX + lastW) {
					c.marginLeft(beforeX + lastW);
				}
				x = leftX + marginLeft(c);

				float height = expandY(c) ? containerHeight() - paddingHeight()
						- marginHeight(c) : actorHeight(actor);

				float y = getYAligned(c, height);

				setBoundsForActor(actor, x, y, width, height);
				beforeX = x;
				lastW = width;
				c.setWidth(width);
			}
		}
	}

	/**
	 * 
	 * @param c
	 *            constrains
	 * @param height
	 *            widget height
	 * @return the y coordinate according to the alignment and height of the
	 *         widget, calculated from the container size.
	 */
	private float getYAligned(Constraints c, float height) {
		switch (verticalAlign(c)) {
		case Align.top:
		case Align.right:
			return (containerHeight() - height - paddingTop() - marginTop(c));
		case Align.left:
		case Align.bottom:
			return paddingBottom() + marginBottom(c);
		default:
			// Align.center
			return (containerHeight() - height - paddingHeight() - marginHeight(c))
					/ 2.0f + paddingBottom() + marginBottom(c);
		}
	}

	public DragAndDrop getDragAndDrop() {
		return dragNDrop;
	}

	public float prefWidth() {
		float prefWidth = 0.0f;
		for (Constraints c : constraints) {
			if (c.actor.isVisible() || computeInvisibles) {
				prefWidth += marginWidth(c) + actorWidth(c.actor);
			}
		}
		return prefWidth + paddingWidth();
	}

	public float prefHeight() {
		float prefHeight = 0.0f;
		for (Constraints c : constraints) {
			if (c.actor.isVisible() || computeInvisibles) {
				prefHeight = Math.max(prefHeight, marginHeight(c)
						+ actorHeight(c.actor));
			}
		}
		return prefHeight + paddingHeight();
	}

	private float containerHeight() {
		return getHeight();
	}

	private float availableWidth() {
		return getWidth() - padding.getWidth();
	}

	private float paddingWidth() {
		return padding.getWidth();
	}

	private float actorWidth(Actor actor) {
		return getPrefWidth(actor);
	}

	private float actorHeight(Actor actor) {
		return getPrefHeight(actor);
	}

	private float paddingLeft() {
		return padding.left;
	}

	private float paddingBottom() {
		return padding.bottom;
	}

	private float paddingHeight() {
		return padding.getHeight();
	}

	private float paddingTop() {
		return padding.top;
	}

	private float marginWidth(Constraints c) {
		return c.margin.getWidth();
	}

	private float marginHeight(Constraints c) {
		return c.margin.getHeight();
	}

	private float marginLeft(Constraints c) {
		return c.margin.left;
	}

	private float marginTop(Constraints c) {
		return c.margin.top;
	}

	private float marginBottom(Constraints c) {
		return c.margin.bottom;
	}

	private boolean expandY(Constraints c) {
		return c.expandY;
	}

	private int verticalAlign(Constraints c) {
		return c.verticalAlign;
	}

	private void setBoundsForActor(Actor actor, float x, float y, float width,
			float height) {
		super.setBounds(actor, x, y, width, height);
	}

	/**
	 * Holds contraints for a widget inside a LinearLayout container
	 */
	public static class Constraints {

		private Actor actor;

		private Insets margin = new Insets();

		private boolean expandX = false;

		private boolean expandY = false;

		private int verticalAlign;

		private int horizontalAlign;

		private float w;

		public Constraints(Actor actor) {
			this.actor = actor;
		}

		public Actor getActor() {
			return actor;
		}

		public Constraints setWidth(float m) {
			w = m;
			return this;
		}

		public float getWidth() {
			return w;
		}

		public Constraints margin(float m) {
			margin.set(m);
			return this;
		}

		public Constraints marginLeft(float m) {
			margin.setLeft(m);
			return this;
		}

		public Constraints margin(float left, float top, float right,
				float bottom) {
			margin.set(left, top, right, bottom);
			return this;
		}

		public Constraints expand(boolean expandX, boolean expandY) {
			this.expandX = expandX;
			this.expandY = expandY;
			return this;
		}

		public Constraints expandX() {
			this.expandX = true;
			return this;
		}

		public Constraints expandY() {
			this.expandY = true;
			return this;
		}

		public Constraints centerX() {
			this.horizontalAlign = Align.center;
			return this;
		}

		public Constraints centerY() {
			this.verticalAlign = Align.center;
			return this;
		}

		public Constraints left() {
			this.horizontalAlign = Align.left;
			return this;
		}

		public Constraints right() {
			this.horizontalAlign = Align.right;
			return this;
		}

		public Constraints bottom() {
			this.verticalAlign = Align.bottom;
			return this;
		}

		public Constraints top() {
			this.verticalAlign = Align.top;
			return this;
		}

		public Constraints getWidht() {
			return this;
		}
	}
}