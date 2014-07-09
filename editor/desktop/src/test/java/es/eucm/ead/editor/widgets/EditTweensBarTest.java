package es.eucm.ead.editor.widgets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import es.eucm.ead.editor.editorui.EditorUITest;
import es.eucm.ead.editor.view.widgets.EditTweensBar;
import es.eucm.ead.editor.view.widgets.IconButton;

public class EditTweensBarTest extends EditorUITest {

	public static void main(String args[]) {
		new LwjglApplication(new EditTweensBarTest(), "Test for EditTweensBar",
				1000, 800);
	}
	
	@Override
	protected void builUI(Group root) {
		controller.getApplicationAssets().loadSkin("skins/light/skin");
		Skin skin = controller.getApplicationAssets().getSkin();
		
		IconButton button1 = new IconButton(skin.getDrawable("actor48x48"), 5,
				skin);
		IconButton button2 = new IconButton(skin.getDrawable("controls48x48"), 5,
				skin);
		IconButton button3 = new IconButton(skin.getDrawable("file48x48"), 5, 
				skin);
		IconButton button4 = new IconButton(skin.getDrawable("folder48x48"), 5,
				skin);
		IconButton button5 = new IconButton(skin.getDrawable("image48x48"), 5, 
				skin);
		IconButton button6 = new IconButton(skin.getDrawable("keyboard48x48"), 5,
				skin);
		IconButton button7 = new IconButton(skin.getDrawable("newscene48x48"), 5,
				skin);
		IconButton button8 = new IconButton(skin.getDrawable("actor48x48"), 5,
				skin);
		IconButton button9 = new IconButton(skin.getDrawable("controls48x48"), 5,
				skin);
		IconButton button10 = new IconButton(skin.getDrawable("file48x48"), 5, 
				skin);
		IconButton button11 = new IconButton(skin.getDrawable("folder48x48"), 5,
				skin);
		IconButton button12 = new IconButton(skin.getDrawable("image48x48"), 5, 
				skin);
		IconButton button13 = new IconButton(skin.getDrawable("keyboard48x48"), 5,
				skin);
		IconButton button14 = new IconButton(skin.getDrawable("newscene48x48"), 5,
				skin);
		
		EditTweensBar bar = new EditTweensBar(skin.getDrawable("blank"), skin);
		
		bar.addInstant(button1);
		bar.addInstant(button2);
		bar.addInstant(button3);
		bar.addGradual(button4);
		bar.addGradual(button5);
		bar.addGradual(button6);
		bar.addGradual(button7);
		bar.addGradual(button8);
		bar.addGradual(button9);
		bar.addGradual(button10);
		bar.addGradual(button11);
		bar.addGradual(button12);
		bar.addGradual(button13);
		bar.addGradual(button14);
		
		bar.setFillParent(true);
		root.addActor(bar);
	}

}
