package hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import scenes.MainMenu;

public class UIHud {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private ImageButton retryBtn,quitBtn,changeCharBtn;
    Preferences prefs = Gdx.app.getPreferences("Data");



    private Label scoreLabel,changeCharLabel,endScoreLabel,highScoreLabel;
    private int score;

    public UIHud(GameMain game)
    {
        this.game=game;
        gameViewport = new FitViewport(GameInfo.WIDTH , GameInfo.HEIGHT ,
                new OrthographicCamera());
        stage = new Stage(gameViewport , game.getBatch());



        createLabel();
        createEndScoresLabel();



        stage.addActor(scoreLabel);


    }

    void createLabel()
    {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("Fonts/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size=70;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;

        BitmapFont font = generator.generateFont(parameter);
        scoreLabel = new Label(java.lang.String.valueOf(score),
                new Label.LabelStyle(font , Color.WHITE));


            scoreLabel.setPosition(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f +300 ,Align.center);





    }

    void createEndScoresLabel()
    {

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
        endScoreLabel = new Label(java.lang.String.valueOf(score),
                new Label.LabelStyle(font , Color.WHITE));
        endScoreLabel.setPosition(GameInfo.WIDTH/2f+129, GameInfo.HEIGHT/2f+25 ,Align.center);

        highScoreLabel = new Label(java.lang.String.valueOf(prefs.getInteger("Score")),
                new Label.LabelStyle(font , Color.WHITE));
        highScoreLabel.setPosition(GameInfo.WIDTH/2f+135, GameInfo.HEIGHT/2f-45 ,Align.center);





    }



    public void createButtons()
    {
        retryBtn = new ImageButton(new SpriteDrawable(
            new Sprite(new Texture("Buttons/Retry.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(
                new Sprite(new Texture("Buttons/Quit.png"))));

        retryBtn.setPosition(GameInfo.WIDTH / 2f -(retryBtn.getWidth()/2f) - 90f ,
            GameInfo.HEIGHT / 2f -193f);
        quitBtn.setPosition(GameInfo.WIDTH / 2f -(quitBtn.getWidth()/2f) + 90f ,
                GameInfo.HEIGHT / 2f -200f);

        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {

                game.setScreen(new Gameplay(game));
                stage.dispose();
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new MainMenu(game));
                stage.dispose();

            }
        });

        stage.addActor(retryBtn);
        stage.addActor(quitBtn);
    }



    public void incrementScore()
    {
        score++;
        scoreLabel.setText(java.lang.String.valueOf(score));
    }

    public void showScore()
    {
        scoreLabel.setText(java.lang.String.valueOf(score));
        stage.addActor(scoreLabel);
    }
    public void showEndScore()
    {
        endScoreLabel.setText(java.lang.String.valueOf(score));
        if(score>prefs.getInteger("Score"))
        {
            highScoreLabel.setPosition(GameInfo.WIDTH/2f+60, GameInfo.HEIGHT/2f-45 ,Align.center);
            highScoreLabel.setText("new "+score);

        }
        else {
            highScoreLabel.setText(java.lang.String.valueOf(prefs.getInteger("Score")));
        }
        stage.addActor(endScoreLabel);
        stage.addActor(highScoreLabel);
    }

    public void changeChar()
    {
        if(changeCharBtn != null)
        {
            changeCharBtn.remove();
        }

        changeCharBtn = new ImageButton(new SpriteDrawable(
                new Sprite(new Texture("Character/"+ GameManager.getInstance().getCharacter()+"/Ch1.png"))));
        changeCharBtn.setPosition(GameInfo.WIDTH / 2f -115, GameInfo.HEIGHT / 2f -10, Align.center);


        stage.addActor(changeCharBtn);
        changeCharBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().incrementIndex();
                changeChar();

            }
        });

    }

    public void createChangeCharLabel()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("Fonts/Bitwise.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size=17;
        //parameter.shadowColor = Color.BLACK;
        //parameter.shadowOffsetX = 3;
        //parameter.shadowOffsetY = 3;
        parameter.borderColor = Color.valueOf("#e4f7b2");
        parameter.borderWidth = 1;

        BitmapFont font = generator.generateFont(parameter);

        changeCharLabel = new Label("Tap to Change",
                new Label.LabelStyle(font , Color.valueOf("#f2855a")));
        changeCharLabel.setPosition(GameInfo.WIDTH/2f-100 , GameInfo.HEIGHT/2f+60, Align.center);
        stage.addActor(changeCharLabel);
    }

    public int getScore() {
        return this.score;
    }

    public Stage getStage() {
        return this.stage;
    }
}
