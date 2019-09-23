package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xsquaredsoft.thefinaljump.GameMain;

import java.util.Random;

import Player.Player;
import ground.GroundBody;
import helpers.GameInfo;
import hud.MainMenuButtons;
import hud.UIHud;
import obstecales.Cactuses;

/**
 * Created by MSI GT72 DRAGON on 12/13/2016.
 */

public class Gameplay implements Screen, ContactListener
{
    private GameMain game;

    private World world;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera debugCamera;
    //to see bodydef shape
    private Box2DDebugRenderer debugRenderer;
    private UIHud hud;
    private boolean firstTouch;
    private Sprite touchToStart;
    private Sprite scorePanel;

    private Array<Sprite> backgrounds = new Array<Sprite>();
    private Array<Sprite> grounds = new Array<Sprite>();
    //test
    private String[] bgs = {"BG1","BG2","BG3","BG4"};
    private int t;
    private Random random = new Random();
    Preferences prefs = Gdx.app.getPreferences("Data");
    int highscore =prefs.getInteger("Score");


    private Player player;
    private GroundBody groundBody;

    private Array<Cactuses> cactusesArray = new Array<Cactuses>();
    private final float DISTANCE_BETWEEN_CACTUSES = 40;

    private Sound scoreSound,playerDieSound,playerJumpSound;




    public Gameplay(GameMain game)
    {
        this.game=game;

        mainCamera = new OrthographicCamera( GameInfo.WIDTH , GameInfo.HEIGHT );
        mainCamera.position.set( GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f , 0 );


        gameViewport = new StretchViewport( GameInfo.WIDTH , GameInfo.HEIGHT , mainCamera );

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false , GameInfo.WIDTH / GameInfo.PPM ,
                                GameInfo.HEIGHT / GameInfo.PPM);
        debugCamera.position.set(GameInfo.WIDTH / 2f , GameInfo.HEIGHT/ 2f , 0);
        //to see bodydef shape
        debugRenderer = new Box2DDebugRenderer();
        hud = new UIHud(game);
        getRandomT();
        createBackgrounds();
        createGrounds();
        createScorePanel();

        world = new World(new Vector2(0,-20f) ,true );
        world.setContactListener(this);

        player = new Player(world , GameInfo.WIDTH/2f - 70 , GameInfo.HEIGHT/2f +50);
        groundBody = new GroundBody(world , grounds.get(0));

        scoreSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Score.mp3"));
        playerJumpSound= Gdx.audio.newSound(Gdx.files.internal("Sounds/Jump.mp3"));
        playerDieSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Dead.mp3"));





    }

    int getRandomT()
    {
        int max = 3;
        int min =0;
        t =random.nextInt((max - min) + 1) + min;

        return t;

    }


    void createTouchToStart()
    {
        touchToStart = new Sprite(new Texture("Buttons/Touch To Start.png"));
        touchToStart.setPosition(GameInfo.WIDTH / 2f -50, GameInfo.HEIGHT / 2f-30);

    }
     void drawTouchToStart(SpriteBatch batch) {

         batch.draw(touchToStart, touchToStart.getX() - touchToStart.getWidth() / 2f,
                 touchToStart.getY() - touchToStart.getHeight() / 2f);
     }

    void createScorePanel()
    {
        scorePanel = new Sprite(new Texture("Buttons/EndScore.png"));
        scorePanel.setPosition(GameInfo.WIDTH / 2f , GameInfo.HEIGHT / 2f);

    }
    void drawScorePanel(SpriteBatch batch) {

        batch.draw(scorePanel, scorePanel.getX() - scorePanel.getWidth() / 2f,
                scorePanel.getY() - scorePanel.getHeight() / 2f);
    }

    void checkForFirstTouch()
    {
        if(!firstTouch)
        {

            createTouchToStart();

           if(Gdx.input.justTouched())
           {
               firstTouch=true;
               player.activatePlayer();
               createAllCactuses();




           }
        }
    }
    void createAllCactuses()
    {

        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                //use custom code
                createCactuses();
            }
        });
        SequenceAction sa = new SequenceAction();
        // for every X seconds add another cactus
        sa.addAction(Actions.delay(0.86f));//1.1
        sa.addAction(run);

        //run this sequence in an infinate loop
        hud.getStage().addAction(Actions.forever(sa));

    }

    void update(float dt)
    {
        checkForFirstTouch();
       if(player.getAlive())
       {
           moveBackgrounds();
           moveGrounds();
           playerJump();
           updateCactuses();
           moveCactuses();
           ACHIEVEMENTS();




       }

    }
    void playerJump()
    {
        if(Gdx.input.justTouched())
        {

            player.playerJump();
            playerJumpSound.play();
            if(player.getY()>GameInfo.HEIGHT/2f+350 )
            {
                player.playerStopJump();
            }
        }
    }



    void createBackgrounds()
    {

        for(int i = 0 ; i < 3 ; i++)
        {
            Sprite bg = new Sprite(new Texture("Backgrounds/"+bgs[t]+".jpg"));
            bg.setPosition( i * bg.getWidth() , 0 );
            backgrounds.add(bg);

        }


    }

    void createGrounds()
    {
        for(int i = 0 ; i < 3 ; i++)
        {
            Sprite gr = new Sprite(new Texture("Backgrounds/Ground.png"));
            gr.setPosition( i * gr.getWidth() , -gr.getHeight()/2 -55 );
            grounds.add(gr);

        }

    }

    void drawBcakgrounds(SpriteBatch batch)
    {

        for(Sprite s : backgrounds )
        {
            batch.draw( s , s.getX() , s.getY() );
        }

    }

    void drawGrounds(SpriteBatch batch)
    {

        for(Sprite gr : grounds )
        {
            batch.draw( gr , gr.getX() , gr.getY() );
        }


    }


    void moveBackgrounds()
    {
        for(Sprite bg : backgrounds)
        {
            float x1 = bg.getX()-5.5f;//4
            bg.setPosition(x1 , bg.getY());

            if(bg.getX() + GameInfo.WIDTH + (bg.getWidth() / 2f ) < mainCamera.position.x)
            {
                float x2 = bg.getX() + bg.getWidth() * backgrounds.size;
                bg.setPosition(x2 , bg.getY());
            }
        }
    }

    void moveGrounds()
    {
        for(Sprite gr : grounds)
        {
            float x1 = gr.getX() - 4f;//4
            gr.setPosition(x1 , gr.getY());

            if(gr.getX() + GameInfo.WIDTH + (gr.getWidth() / 2f ) < mainCamera.position.x)
            {
                float x2 = gr.getX() + gr.getWidth() * grounds.size;
                gr.setPosition(x2 , gr.getY());
            }
        }
    }

    void createCactuses()
    {
        Cactuses c = new Cactuses(world ,  GameInfo.WIDTH+DISTANCE_BETWEEN_CACTUSES);
        c.setMainCamera(mainCamera);
        cactusesArray.add(c);
    }

    void drawCactuses(SpriteBatch batch)
    {
        for(Cactuses cactus: cactusesArray)
        {
            cactus.drawCactus(batch);
        }
    }

    void updateCactuses()
    {
        for(Cactuses cactus: cactusesArray)
        {
            cactus.updateCactuses();
        }
    }

    void moveCactuses()
    {
        for(Cactuses cactus: cactusesArray)
        {
            cactus.moveCactuses();
        }
    }

    void stopCactuses()
    {
        for(Cactuses cactus: cactusesArray)
        {
            cactus.stopCactuses();
        }
    }

    void playerDied()
    {
        player.setAlive(false);
        player.charDied();
        stopCactuses();
        hud.getStage().clear();
        hud.showEndScore();


        if (highscore < hud.getScore())
        {
            prefs.putInteger("Score",hud.getScore());
            prefs.flush();
            game.playServices.submitScore(hud.getScore());
        }

        hud.createButtons();
        hud.createChangeCharLabel();
        hud.changeChar();
        Gdx.input.setInputProcessor(hud.getStage());


    }

    void ACHIEVEMENTS()
    {
        if(game.playServices.isSignedIn()) {
            if (hud.getScore() == 1) {
                game.playServices.unlockAchievement("CgkI1Ye0wosfEAIQAQ");
            }
            if (hud.getScore() == 50) {
                game.playServices.unlockAchievement("CgkI1Ye0wosfEAIQAg");
            }
            if (hud.getScore() == 100) {
                game.playServices.unlockAchievement("CgkI1Ye0wosfEAIQAw");
            }
            if (hud.getScore() == 300) {
                game.playServices.unlockAchievement("CgkI1Ye0wosfEAIQBA");
            }
            if (hud.getScore() == 500) {
                game.playServices.unlockAchievement("CgkI1Ye0wosfEAIQBQ");
            }
        }

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBcakgrounds(game.getBatch());
        player.drawMain(game.getBatch());
        player.animatePlayer(game.getBatch());
        drawCactuses(game.getBatch());
        drawGrounds(game.getBatch());
        if(!firstTouch)
        drawTouchToStart(game.getBatch());
        if(!player.getAlive()&&firstTouch)
        drawScorePanel(game.getBatch());

        game.getBatch().end();
        //to see bodydef shape
        //debugRenderer.render(world , debugCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        player.updatePlayer();


        world.step(Gdx.graphics.getDeltaTime() , 6 , 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update( width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        world.dispose();
        for(Sprite bg : backgrounds)
        {
            bg.getTexture().dispose();
        }
        for(Sprite ground : grounds)
        {
            ground.getTexture().dispose();
        }
        for(Cactuses cactuses : cactusesArray)
        {
            cactuses.disposeAll();
        }


        playerJumpSound.dispose();
        playerDieSound.dispose();
        scoreSound.dispose();
        game.dispose();


    }

    @Override
    public void beginContact(Contact contact) {
        Fixture body1,body2;
        if(contact.getFixtureA().getUserData()=="PLAYER")
        {
            body1=contact.getFixtureA();
            body2=contact.getFixtureB();
        }
        else
        {
            body1=contact.getFixtureB();
            body2=contact.getFixtureA();
        }

        if (body1.getUserData()=="PLAYER" && body2.getUserData() == "CACTUS")
        {
            if(player.getAlive())
            {
                playerDieSound.play();
                playerDied();
            }
        }
        if (body1.getUserData()=="PLAYER" && body2.getUserData() == "GROUND")
        {
            if(player.getAlive())
            {
                playerDieSound.play();
                playerDied();
            }
        }
        if (body1.getUserData()=="PLAYER" && body2.getUserData() == "SCORE")
        {
            scoreSound.play();
            hud.incrementScore();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
