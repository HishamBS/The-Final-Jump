package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xsquaredsoft.thefinaljump.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.Gameplay;

/**
 * Created by MSI GT72 DRAGON on 12/17/2016.
 */

public class MainMenuButtons
{

    private GameMain game;
    private Stage stage;
    private Viewport viewport;
    private ImageButton playBtn,achBtn,lbBtn;
    private Label  scoreLabel;


    public MainMenuButtons(GameMain game )
    {
        this.game = game;
        viewport = new FitViewport(GameInfo.WIDTH , GameInfo.HEIGHT ,
                new OrthographicCamera());
        stage = new Stage(viewport , game.getBatch());


        createButtons();
        showScore();
        stage.addActor(playBtn);
        stage.addActor(achBtn);
        stage.addActor(lbBtn);




    }

    public void createButtons()
    {
        playBtn = new ImageButton(new SpriteDrawable(
                new Sprite(new Texture("Buttons/Play.png"))));
        achBtn = new ImageButton(new SpriteDrawable(
                new Sprite(new Texture("Buttons/ACH.png"))));
        lbBtn = new ImageButton(new SpriteDrawable(
                new Sprite(new Texture("Buttons/LB.png"))));



        playBtn.setPosition(GameInfo.WIDTH / 2f -10, GameInfo.HEIGHT / 2f -45f , Align.center);
        achBtn.setPosition(GameInfo.WIDTH / 2f+210, GameInfo.HEIGHT / 2f-300, Align.center);
        lbBtn.setPosition(GameInfo.WIDTH /2f+210, GameInfo.HEIGHT /2f-360, Align.center);





        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new Gameplay(game));
                stage.dispose();

            }
        });

        achBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.playServices.showAchievement();

            }
        });

        lbBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.playServices.showScore();

            }
        });


    }



    public void showScore()
    {

        if(scoreLabel!=null)
        {
            return;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("Fonts/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size=30;
//        parameter.shadowColor = Color.BLACK;
//        parameter.shadowOffsetX = 3;
//        parameter.shadowOffsetY = 3;
//        parameter.borderColor = Color.BLACK;
//        parameter.borderWidth = 3;

        BitmapFont font = generator.generateFont(parameter);
        Preferences prefs = Gdx.app.getPreferences("Data");

        scoreLabel = new Label(String.valueOf(prefs.getInteger("Score")),
               new Label.LabelStyle(font , Color.valueOf("#f2b93c")));
        scoreLabel.setPosition(GameInfo.WIDTH/2f-6f , GameInfo.HEIGHT/2f+300f , Align.center);
        stage.addActor(scoreLabel);

    }




    public Stage getStage() {
        return this.stage;
    }
}
