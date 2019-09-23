package obstecales;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ground.GroundBody;
import helpers.GameInfo;

public class Cactuses
{
    private World world;
    private Body body1 , body2 , scoreBody;

    private Sprite cactus1 , cactus2;

    private final float DISTANCE_BETWEEN_CACTUSES =420;

    private Random random = new Random();

    private OrthographicCamera mainCamera;

    public Cactuses(World world , float x)
    {
        this.world = world;
        createCactuses(x , getRandomY());


    }

    void createCactuses(float x , float y)
    {
        cactus1 = new Sprite(new Texture("Cactus/Cacc.png"));
        cactus2 = new Sprite(new Texture("Cactus/Cacc.png"));

        cactus1.setPosition(x , y + DISTANCE_BETWEEN_CACTUSES);
        cactus2.setPosition(x , y - DISTANCE_BETWEEN_CACTUSES);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        // creating body for cactus1
        bodyDef.position.set(cactus1.getX() / GameInfo.PPM ,
                cactus1.getY() / GameInfo.PPM);
        body1 = world.createBody(bodyDef);
        body1.setFixedRotation(true);


        // creating body for cactus2
        bodyDef.position.set(cactus2.getX() / GameInfo.PPM ,
                cactus2.getY() / GameInfo.PPM);
        body2 = world.createBody(bodyDef);
        body2.setFixedRotation(true);



        // creating the body of the score to detect scores
        bodyDef.position.set(cactus1.getX() / GameInfo.PPM,
                y / GameInfo.PPM);
        scoreBody = world.createBody(bodyDef);
        scoreBody.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();





        shape.setAsBox((cactus1.getWidth() / 2f-22) / GameInfo.PPM,
                (cactus1.getHeight() / 2f-7) / GameInfo.PPM);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.CACTUS;


        Fixture fixture1 = body1.createFixture(fixtureDef);
        fixture1.setUserData("CACTUS");

        Fixture fixture2 = body2.createFixture(fixtureDef);
        fixture2.setUserData("CACTUS");
        shape.setAsBox(3 / GameInfo.PPM ,
                (cactus1.getHeight() / 2f) / GameInfo.PPM);
        fixtureDef.shape=shape;
        fixtureDef.filter.categoryBits = GameInfo.SCORE;
        fixtureDef.isSensor=true;


        Fixture fixture3 = scoreBody.createFixture(fixtureDef);
        fixture3.setUserData("SCORE");

        shape.dispose();

    }

    public void drawCactus(SpriteBatch batch)
    {

        batch.draw(cactus1 , cactus1.getX() - cactus1.getWidth() / 2f -3,
            cactus1.getY() - cactus1.getHeight() / 2f );
        batch.draw(cactus2 , cactus2.getX() - cactus2.getWidth() / 2f -3,
                cactus2.getY() - cactus2.getHeight() / 2f);
    }

    public void updateCactuses()
    {
        cactus1.setPosition(body1.getPosition().x * GameInfo.PPM ,
                body1.getPosition().y * GameInfo.PPM);
        cactus2.setPosition(body2.getPosition().x * GameInfo.PPM ,
                body2.getPosition().y  * GameInfo.PPM);
    }

    public void moveCactuses()
    {
        body1.setLinearVelocity(-4f,0);
        body2.setLinearVelocity(-4f,0);
        scoreBody.setLinearVelocity(-4f,0);

        if(cactus1.getX() + (GameInfo.WIDTH / 2f) + 60 < mainCamera.position.x)
        {
            body1.setActive(false);
            body2.setActive(false);
            scoreBody.setActive(false);
        }

    }
    public void stopCactuses() {
        body1.setLinearVelocity(0, 0);
        body2.setLinearVelocity(0, 0);
        scoreBody.setLinearVelocity(0, 0);
    }

    public void setMainCamera(OrthographicCamera mainCamera)
    {
        this.mainCamera = mainCamera;
    }

    float getRandomY()
    {
        float max = GameInfo.HEIGHT / 2f +150;
        float min = GameInfo.HEIGHT / 2f -150;
        return random.nextFloat() * (max - min) + min;
    }
    public void disposeAll()
    {
        cactus1.getTexture().dispose();
        cactus2.getTexture().dispose();

    }














}
