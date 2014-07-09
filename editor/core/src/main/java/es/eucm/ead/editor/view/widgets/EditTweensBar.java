package es.eucm.ead.editor.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.tablelayout.Cell;

import es.eucm.ead.editor.view.widgets.IconTextButton.Position;
import es.eucm.ead.editor.view.widgets.layouts.LinearLayout;

public class EditTweensBar extends Table{

	private static final float LATERAL_MARGIN = 20, BASE_MARGIN = 10;
	
	private Button instantButton;
	
	private Button gradualButton;
	
	private TypeTweensBar instantTweens;	
	
	private TypeTweensBar gradualsTweens;

	private Cell<TypeTweensBar> right;
	
	public EditTweensBar(Skin skin) {
		this(null, skin);
	}
	
	public EditTweensBar(Drawable background, Skin skin) {
		super(skin);
		setBackground(background);
		
		align(Align.left);
		
		instantTweens = new TypeTweensBar(skin);
		gradualsTweens = new TypeTweensBar(skin);
		
		instantButton = new IconTextButton("INSTANTANEO", skin, skin.getDrawable("camera48x48"), Position.RIGHT, 10, 2, 200);
		instantButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				changeTweensBar(instantTweens);
			}
		});
		
		gradualButton = new IconTextButton("GRADUAL", skin, skin.getDrawable("goal48x48"), Position.RIGHT, 10, 0, 200);
		gradualButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				changeTweensBar(gradualsTweens);
			}
		});
		
		LinearLayout left = new LinearLayout(false);

		left.add(instantButton).margin(LATERAL_MARGIN, BASE_MARGIN, LATERAL_MARGIN, BASE_MARGIN).expand(true, true);
		left.add(new Separator(true, skin));
		left.add(gradualButton).margin(LATERAL_MARGIN, BASE_MARGIN, LATERAL_MARGIN, BASE_MARGIN).expand(true, true);
		
		this.add(left);
		this.add(new Separator(false, skin));
		right = this.add(instantTweens).expand(true, true).align(Align.left);
	}
	
	public void addInstant(Actor actor){
		instantTweens.addButton(actor);
	}
	
	public void addGradual(Actor actor){
		gradualsTweens.addButton(actor);
	}
	
	private void changeTweensBar(TypeTweensBar newBar){
		right.setWidget(null);
		right.setWidget(newBar);
	}
	
	private class TypeTweensBar extends Table{
		
		private static final float SCROLL_MOVING = 115, DEFAULT_PAD = 5;
		private static final String FORWARD = "forward24x24", BACK = "back24x24";
	
		private Button leftArrow;
		
		private Button rightArrow;
		
		private ScrollPane scroll;
		
		private Table inner;
		
		private Skin skin;
		
		public TypeTweensBar(Skin skin) {
			super();

			this.skin = skin;
			inner = new Table();
			scroll = new ScrollPane(inner);
			
			leftArrow = new IconButton(BACK, DEFAULT_PAD, skin){
				@Override
				public float getPrefHeight() {
					if(this.getParent()!=null){
						return this.getParent().getHeight();
					}
					return super.getPrefHeight();
				}
			};
			leftArrow.addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					scroll.setScrollX(scroll.getScrollX()-SCROLL_MOVING);
					return super.touchDown(event, x, y, pointer, button);
				}
			});
			
			rightArrow = new IconButton(FORWARD, DEFAULT_PAD, skin){
				@Override
				public float getPrefHeight() {
					if(this.getParent()!=null){
						return this.getParent().getHeight();
					}
					return super.getPrefHeight();
				}
			};
			rightArrow.addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					scroll.setScrollX(scroll.getScrollX()+SCROLL_MOVING);
					return super.touchDown(event, x, y, pointer, button);
				}
			});
			
			
			this.add(leftArrow);
			this.add(scroll);
			inner.add(new Separator(false, skin));
			this.add(rightArrow);
		}
		
		public void addButton(Actor actor){
			inner.add(actor);
			inner.add(new Separator(false, skin));
		}
				
	}

}
