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
package es.eucm.ead.editor.view.widgets.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;

import es.eucm.ead.editor.control.Preferences;
import es.eucm.ead.editor.view.widgets.IconButton;
import es.eucm.ead.editor.view.widgets.draw.SlideColorPicker.SlideColorPickerStyle;

/**
 * A panel with a {@link SlideColorPicker} that remembers the recent colors that
 * have been picked.
 * 
 * @author Rotaru Dan Cristian
 * 
 */
public class ColorPickerPanel extends Table {

	private static final int DEFAULT_ROWS = 2;

	private static final ClickListener colorClicked = new ClickListener() {

		public void clicked(InputEvent event, float x, float y) {
			Actor listenerActor = event.getTarget();
			ColorPickerPanel colorPicker = ((ColorPickerPanel) listenerActor
					.getUserObject());
			Color color = listenerActor.getColor();
			colorPicker.picker.updateSlidersTo(color);
			ColorClickedEvent colorClicked = Pools
					.obtain(ColorClickedEvent.class);
			colorClicked.color = color;
			colorPicker.fire(colorClicked);
			Pools.free(colorClicked);
		};
	};

	private Skin skin;

	private Table colors;

	protected SlideColorPicker picker;

	private ColorPickerPanelStyle style;

	private Preferences prefs;

	public ColorPickerPanel(Skin skin, Preferences prefs) {
		this(skin, skin.get(ColorPickerPanelStyle.class), prefs);
	}

	public ColorPickerPanel(Skin skin, String styleName, Preferences prefs) {
		this(skin, skin.get(styleName, ColorPickerPanelStyle.class), prefs);
	}

	public ColorPickerPanel(Skin skin,
			ColorPickerPanelStyle colorPickerPanelStyle, Preferences prefs) {
		setBackground(colorPickerPanelStyle.background);
		this.prefs = prefs;
		this.skin = skin;
		this.style = colorPickerPanelStyle;

		picker = new SlideColorPicker(colorPickerPanelStyle);
		colors = new Table();
		for (int i = 0; i < DEFAULT_ROWS; ++i) {
			addColorRow();
		}
		colors.addListener(colorClicked);

		add(colors);
		row();
		add(picker).fill().expand();
	}

	private void addColorRow() {
		String recentColorDrawable = style.recentColorIcon;
		String buttonStyleName = style.recentColorStyle;

		float columns = style.colorsPerRow;

		for (int i = 0; i < columns; ++i) {
			IconButton image = new IconButton(recentColorDrawable, skin,
					buttonStyleName) {
				@Override
				public void setChecked(boolean isChecked) {
				}

				@Override
				public void setColor(float r, float g, float b, float a) {
					super.setColor(r, g, b, a);
					getIcon().setColor(r, g, b, a);
				}

				public void setColor(Color color) {
					super.setColor(color);
					getIcon().setColor(color);
				};
			};
			image.setUserObject(this);

			colors.add(image);
		}
		colors.row();

		colors.addListener(colorClicked);
	}

	public void setPickedColor(Color color) {
		picker.setPickedColor(color);
	}

	public void completeRowsIfPossible(WidgetGroup reference) {
		reference.layout();
		IconButton image = (IconButton) colors.getChildren().first();

		int rowsToAdd = Math.min(
				(int) Math.floor((Gdx.graphics.getHeight() - reference
						.getPrefHeight()) / image.getPrefHeight()), style.rows
						- colors.getRows());

		for (int i = 0; i < rowsToAdd; i++) {
			addColorRow();
		}

		SnapshotArray<Actor> children = colors.getChildren();
		for (int i = 0, n = children.size; i < n; ++i) {
			Actor actor = children.get(i);

			int intCol = prefs.getInteger(Preferences.PREF_COLOR + i, -1);
			if (intCol == -1) {
				float[] rgb = picker.HSBtoRGB(i / (float) n, 1, 1);
				actor.setColor(rgb[0], rgb[1], rgb[2], 1f);
			} else {
				Color.rgba8888ToColor(actor.getColor(), intCol);
				actor.setColor(actor.getColor());
			}
		}
	}

	private void savePrefColors() {
		SnapshotArray<Actor> children = colors.getChildren();
		for (int i = 0, n = children.size; i < n; ++i) {
			Actor actor = children.get(i);

			prefs.putInteger(Preferences.PREF_COLOR + i,
					Color.rgba8888(actor.getColor()));
		}
		prefs.flush();
	}

	public void initResources() {
		picker.initialize();
	}

	private Runnable releaseResources = new Runnable() {

		@Override
		public void run() {
			picker.release();
		}
	};

	public Runnable getReleaseResources() {
		return releaseResources;
	}

	public void setUpPickedColor() {
		if (!hasPickedColor()) {
			Array<Cell> colorCells = this.colors.getCells();
			for (int i = colorCells.size - 2; i >= 0; --i) {
				Actor actor = colorCells.get(i).getActor();
				colorCells.get(i + 1).getActor().setColor(actor.getColor());
			}
			colors.getCells().first().getActor()
					.setColor(picker.getPickedColor());
		}
		savePrefColors();
	}

	private boolean hasPickedColor() {
		for (Cell cell : colors.getCells()) {
			Actor actor = cell.getActor();
			if (actor.getColor().equals(picker.getPickedColor())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Base class to listen to {@link ColorClickedEvent}s produced by
	 * {@link ColorPickerPanel}.
	 */
	public static class ColorClickedListener implements EventListener {

		@Override
		public boolean handle(Event event) {
			if (event instanceof ColorClickedEvent) {
				colorClicked((ColorClickedEvent) event);
			}
			return true;
		}

		/**
		 * The color has been clicked.
		 */
		public void colorClicked(ColorClickedEvent event) {

		}
	}

	/**
	 * Fired when one of the recent colors has been clicked.
	 */
	public static class ColorClickedEvent extends Event {

		private Color color;

		public Color getColor() {
			return color;
		}

		@Override
		public void reset() {
			super.reset();
			this.color = null;
		}
	}

	/**
	 * The style for a {@link ColorPickerPanel}.
	 * 
	 * @author Rotaru Dan Cristian
	 */
	static public class ColorPickerPanelStyle extends SlideColorPickerStyle {

		public String recentColorIcon;

		public String recentColorStyle;

		public int colorsPerRow, rows;

		/** Optional */
		public Drawable background;

		public ColorPickerPanelStyle() {
		}

		public ColorPickerPanelStyle(ColorPickerPanelStyle style) {
			super(style);
			this.recentColorIcon = style.recentColorIcon;
		}

		public ColorPickerPanelStyle(SlideColorPickerStyle style,
				String recentColorIcon) {
			super(style);
			this.recentColorIcon = recentColorIcon;
		}
	}
}
