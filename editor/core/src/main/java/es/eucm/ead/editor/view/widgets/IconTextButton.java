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

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Button with an image and a text. The text button can be below, above or to
 * the sides of the image.
 * 
 */
public class IconTextButton extends Button {

	private static final float PAD_LARGE = 5f, PAD_SMALL = 2f;

	public enum Position {
		TOP, BOTTOM, RIGHT, LEFT
	}

	private Position pos;

	private Cell<Image> iconCell;

	private float size;

	private Cell<Label> labelCell;

	/**
	 * Create a Button with a text <b>name</b>, an image <b>icon</b>. The
	 * <b>pos</b> indicates if the text is below, above or to the sides of the
	 * image.
	 * 
	 * @param name
	 * @param skin
	 * @param icon
	 * @param pos
	 */
	public IconTextButton(String name, Skin skin, Drawable icon, Position pos) {
		this(name, skin, icon, pos, 0, 0, 0);
	}

	/**
	 * Create a Button with a text <b>name</b>, an image <b>icon</b>. The
	 * <b>pos</b> indicates if the text is below, above or to the sides of the
	 * image. The image have the padding <b>pad</b>
	 * 
	 * @param name
	 * @param skin
	 * @param icon
	 * @param pos
	 * @param pad
	 * @param size
	 */
	public IconTextButton(String name, Skin skin, Drawable icon, Position pos,
			float pad, float size) {
		this(name, skin, icon, pos, pad, pad, size);
	}

	/**
	 * Create a Button with a text <b>name</b>, an image <b>icon</b>. The
	 * <b>pos</b> indicates if the text is below, above or to the sides of the
	 * image. The image have the padding <b>lateralPad</b> in sides and
	 * <b>basePad</b> in top and bottom.
	 * 
	 * @param name
	 * @param skin
	 * @param icon
	 * @param pos
	 * @param pad
	 * @param size
	 */
	public IconTextButton(String name, Skin skin, Drawable icon, Position pos,
			float lateralPad, float basePad, float size) {
		super(skin);
		this.pos = pos;
		this.size = size;

		Image sceneIcon = new Image(icon);
		sceneIcon.setScaling(Scaling.fit);

		Label label = new Label(name, skin);

		switch (pos) {
		case TOP:
			pad(PAD_LARGE, PAD_LARGE, PAD_SMALL, PAD_LARGE);
			label.setAlignment(Align.center);
			labelCell = add(label).expand().fill();
			row();
			iconCell = add(sceneIcon).expand().fill()
					.pad(basePad, lateralPad, basePad, lateralPad);
			break;
		case BOTTOM:
			pad(PAD_LARGE, PAD_LARGE, PAD_SMALL, PAD_LARGE);
			label.setAlignment(Align.center);
			iconCell = add(sceneIcon).expand().fill()
					.pad(basePad, lateralPad, basePad, lateralPad);
			row();
			labelCell = add(label).expand().fill();
			break;
		case LEFT:
			pad(PAD_LARGE, PAD_SMALL, PAD_LARGE, PAD_SMALL);
			label.setAlignment(Align.right);
			labelCell = add(label).right().expand().fill();
			iconCell = add(sceneIcon).left().fill()
					.pad(basePad, lateralPad, basePad, lateralPad);
			break;
		case RIGHT:
			iconCell = add(sceneIcon).right().fill()
					.pad(basePad, lateralPad, basePad, lateralPad);
			pad(PAD_LARGE, PAD_SMALL, PAD_LARGE, PAD_SMALL);
			label.setAlignment(Align.left);
			labelCell = add(label).left().expand().fill();
		}
	}

	public void setSize(float size) {
		this.size = size;
	}

	public void setAligmentText(int alig) {
		labelCell.getActor().setAlignment(alig);
	}

	@Override
	public float getPrefHeight() {
		if (pos == Position.TOP || pos == Position.BOTTOM && size != 0) {
			return size;
		}
		return super.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		if (pos == Position.LEFT || pos == Position.RIGHT && size != 0) {
			return size;
		} else if (labelCell != null
				&& labelCell.getActor().getWidth() > super.getPrefWidth()) {
			return labelCell.getActor().getWidth();
		}
		return super.getPrefWidth();
	}

	public Label getLabel() {
		return this.labelCell.getActor();
	}

	public void changeText(String newText) {
		this.labelCell.getActor().setText(newText);
	}

	public Drawable getDrawableImage() {
		return iconCell.getActor().getDrawable();
	}

	public void setPadding(float top, float right, float bottom, float left,
			float center) {
		switch (pos) {
		case TOP:
			labelCell.pad(top, left, 0, right);
			iconCell.pad(center, left, bottom, right);
			break;
		case BOTTOM:
			labelCell.pad(0, left, bottom, right);
			iconCell.pad(top, left, center, right);
			break;
		case LEFT:
			labelCell.pad(top, left, bottom, 0);
			iconCell.pad(top, center, bottom, right);
			break;
		case RIGHT:
			labelCell.pad(top, 0, bottom, right);
			iconCell.pad(top, left, bottom, center);
		}
	}

}
