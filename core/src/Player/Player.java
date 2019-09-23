package Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;
import helpers.GameManager;

/**
 * Created by MSI GT72 DRAGON on 12/14/2016.
 */

public class Player extends Sprite
{

    private World world;
    private Body body;
    private boolean isAlive;

    private Texture charDead;

    private TextureAtlas playerAtlas;
    private Animation animation;
    private float elapsedTime;

    public Player (World world , float x , float y )
    {
        super(new Texture("Character/"+ GameManager.getInstance().getCharacter()+"/Ch1.png"));
        charDead = new Texture("Character/"+ GameManager.getInstance().getCharacter()+"/Dead.png");
        this.world = world;
        setPosition(x,y);


        createBody();
        createAnimation();
    }

    void createBody()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX()/GameInfo.PPM , getY()/GameInfo.PPM);


        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / GameInfo.PPM, (getHeight() / 2) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;// the mass

        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        fixtureDef.filter.maskBits= GameInfo.GROUND | GameInfo.CACTUS | GameInfo.SCORE;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("PLAYER");
        shape.dispose();
        body.setActive(false);
    }

    public void activatePlayer()
    {
        isAlive=true;
        body.setActive(true);
    }

    public void  playerJump()
    {

        body.setLinearVelocity(0 , 6f);//5.7
    }

    public void  playerStopJump()
    {

        body.setLinearVelocity(0, -2f);
    }

    public void drawMain(SpriteBatch batch)
    {
        if(!isAlive) {
            batch.draw(this, getX() - getWidth() / 2f, getY() - getHeight() / 2f);
        }
    }

    public void animatePlayer(SpriteBatch batch)
    {
        if(isAlive) {

                elapsedTime += Gdx.graphics.getDeltaTime();
                batch.draw(animation.getKeyFrame(elapsedTime, true), getX() - getWidth() / 2f
                        , getY() - getHeight() / 2f);

        }


    }

    public void updatePlayer()
    {
        setPosition(body.getPosition().x * GameInfo.PPM , body.getPosition().y * GameInfo.PPM);
    }

    void createAnimation()
    {
        playerAtlas = new TextureAtlas("Character/" + GameManager.getInstance().getCharacter()
                +"/"+ GameManager.getInstance().getCharacter()+" Player.atlas");
        animation = new Animation(1f/8f , playerAtlas.getRegions());
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean getAlive()
    {
        return this.isAlive;
    }

    public void charDied()
    {
       this.setTexture(charDead);
    }

}
