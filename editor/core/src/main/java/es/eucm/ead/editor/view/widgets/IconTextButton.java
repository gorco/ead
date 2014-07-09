package es.eucm.ead.editor.view.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class IconTextButton extends Button{

	private static final float PAD_LARGE = 17f, PAD_SMALL = 10f;

	public enum Position {
		TOP, BOTTOM, RIGHT, LEFT
	}

	private Position pos;
	
	private Image sceneIcon;
	
	private float size;
	
	private Label label;

	public IconTextButton(String name, Skin skin,
			Drawable icon, Position pos) {
		this(name, skin, icon, pos, 0, 0, 0);
	}
	
	public IconTextButton(String name, Skin skin,
			Drawable icon, Position pos, float pad, float size) {
		this(name, skin, icon, pos, pad, pad, size);
	}
	
	public IconTextButton(String name, Skin skin,
			Drawable icon, Position pos, float lateralPad, float basePad, float size) {
		super(skin);
		this.pos = pos;
		this.size = size;
		
		sceneIcon = new Image(icon);
		sceneIcon.setScaling(Scaling.fit);

		this.label = new Label(name, skin);
		this.label.setWrap(true);

		switch (pos) {
		case TOP:
			pad(PAD_LARGE, PAD_LARGE, PAD_SMALL, PAD_LARGE);
			this.label.setAlignment(Align.center);
			add(this.label).expand().fill();
			row();
			add(sceneIcon).expand().fill().pad(basePad, lateralPad, basePad, lateralPad);
			break;
		case BOTTOM:
			pad(PAD_LARGE, PAD_LARGE, PAD_SMALL, PAD_LARGE);
			this.label.setAlignment(Align.center);
			add(sceneIcon).expand().fill().pad(basePad, lateralPad, basePad, lateralPad);
			row();
			add(this.label).expand().fill();
			break;
		case LEFT:
			pad(PAD_LARGE, PAD_SMALL, PAD_LARGE, PAD_SMALL);
			this.label.setAlignment(Align.right);
			add(this.label).right().expand().fill();
			add(sceneIcon).left().fill().pad(basePad, lateralPad, basePad, lateralPad);
			break;
		case RIGHT:
			add(sceneIcon).right().fill().pad(basePad, lateralPad, basePad, lateralPad);
			pad(PAD_LARGE, PAD_SMALL, PAD_LARGE, PAD_SMALL);
			this.label.setAlignment(Align.left);
			add(this.label).left().expand().fill();
		}
	}
	
	public void setSize(float size){
		this.size = size;
	}
	
	public void setAligmentText(int alig) {
		this.label.setAlignment(alig);
	}

	@Override
	public float getPrefHeight() {
		if(pos == Position.TOP || pos == Position.BOTTOM && size != 0){
			return size;
		}
		return super.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		if(pos == Position.LEFT || pos == Position.RIGHT && size != 0){
			return size;
		}
		return super.getPrefWidth();
	}
	
	public Label getLabel() {
		return this.label;
	}
}
