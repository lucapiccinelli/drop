package com.bnana.drop.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.bnana.drop.Drop;
import com.bnana.drop.tween.SpriteAccessor;

import java.util.Iterator;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;

/**
 * Created by Luca on 8/19/2015.
 */
public class GameScreen implements Screen {
    private final Drop game;
    private Texture dropImg;
    private Texture bucketImg;
    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Rectangle bucket;

    private Array<Rectangle> rainDrops;
    private long lastDropTime;

    private Animation bckAnimation;
    private TextureRegion backImg2;
    private TextureRegion backImg3;
    private TextureRegion backImg1;
    private float stateTime;

    private Sprite grassSprite;
    private Sprite grassSprite2;
    private Sprite grassSprite3;
    private Sprite grassSprite4;
    private Sprite grassSprite5;
    private Sprite grassSprite6;
    private Sprite grassSprite7;
    private Sprite grassSprite8;
    private Sprite grassSprite9;
    private TweenManager tweenManager;

    public GameScreen(Drop game) {
        this.game = game;

        stateTime = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = game.batch;

        backImg1 = new TextureRegion(new Texture(Gdx.files.internal("bck\\background1.png")));
        backImg2 = new TextureRegion(new Texture(Gdx.files.internal("bck\\background2.png")));
        backImg3 = new TextureRegion(new Texture(Gdx.files.internal("bck\\background3.png")));
        dropImg = new Texture(Gdx.files.internal("droplet.png"));
        bucketImg = new Texture(Gdx.files.internal("bucket.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        rainMusic.setLooping(true);

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 32 / 2;
        bucket.y = 15;
        bucket.width = 32;
        bucket.height = 32;

        rainDrops = new Array<Rectangle>();
        spawnRainDrop();

        TextureRegion[] bckFrames = {backImg1, backImg2, backImg3};
        bckAnimation = new Animation(0.1f, bckFrames);

        createGrass();

        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.call(windCallBack).start(tweenManager);
    }

    private final TweenCallback windCallBack = new TweenCallback() {
        @Override
        public void onEvent(int i, BaseTween<?> baseTween) {
            float d = MathUtils.random() * 0.5f + 0.5f;
            float t = -0.5f * grassSprite.getHeight();

            Timeline.createParallel()
                    .push(Tween.to(grassSprite, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).repeatYoyo(1, 0).setCallback(windCallBack))
                    .push(Tween.to(grassSprite2, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite3, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite4, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite5, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite6, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite7, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3 * 2).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite8, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3 * 2).repeatYoyo(1, 0))
                    .push(Tween.to(grassSprite9, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d / 3 * 2).repeatYoyo(1, 0))
                    .start(tweenManager);
        }
    };

    private void createGrass() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("grass.pack"));

        grassSprite = atlas.createSprite("grass1");
        grassSprite.setSize(300, 28);
        grassSprite.setPosition(0, 0);

        grassSprite2 = atlas.createSprite("grass1");
        grassSprite2.setSize(300, 28);
        grassSprite2.setPosition(280, 0);

        grassSprite3 = atlas.createSprite("grass1");
        grassSprite3.setSize(300, 28);
        grassSprite3.setPosition(540, 0);

        grassSprite4 = atlas.createSprite("grass2");
        grassSprite4.setSize(300, 28);
        grassSprite4.setPosition(0, 0);

        grassSprite5 = atlas.createSprite("grass2");
        grassSprite5.setSize(300, 28);
        grassSprite5.setPosition(280, 0);

        grassSprite6 = atlas.createSprite("grass2");
        grassSprite6.setSize(300, 28);
        grassSprite6.setPosition(540, 0);

        grassSprite7 = atlas.createSprite("grass3");
        grassSprite7.setSize(300, 28);
        grassSprite7.setPosition(0, 0);

        grassSprite8 = atlas.createSprite("grass3");
        grassSprite8.setSize(300, 28);
        grassSprite8.setPosition(280, 0);

        grassSprite9 = atlas.createSprite("grass3");
        grassSprite9.setSize(300, 28);
        grassSprite9.setPosition(540, 0);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        tweenManager.update(Gdx.graphics.getDeltaTime());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bckAnimation.getKeyFrame(stateTime, true), 0, 0, 800, 480);
        grassSprite.draw(batch);
        grassSprite2.draw(batch);
        grassSprite3.draw(batch);

        grassSprite4.draw(batch);
        grassSprite5.draw(batch);
        grassSprite6.draw(batch);

        batch.draw(bucketImg, bucket.x, bucket.y, bucket.width, bucket.height);

        grassSprite7.draw(batch);
        grassSprite8.draw(batch);
        grassSprite9.draw(batch);
        stateTime += Gdx.graphics.getDeltaTime();
        for (Rectangle rainDrop : rainDrops) {
            batch.draw(dropImg, rainDrop.x, rainDrop.y, rainDrop.width, rainDrop.height);
        }
        batch.end();

        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 32 / 2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();

        Iterator<Rectangle> iter = rainDrops.iterator();
        while (iter.hasNext()){
            Rectangle rainDrop = iter.next();
            rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (rainDrop.y + 32 < 0) iter.remove();

            if(rainDrop.overlaps(bucket)){
                dropSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void dispose(){
        dropImg.dispose();
        bucketImg.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }

    private void spawnRainDrop(){
        Rectangle rainDrop = new Rectangle(MathUtils.random(0, 800 - 32), 420, 32, 32);
        rainDrops.add(rainDrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        rainMusic.play();
    }

    @Override
    public void resize(int width, int height) {

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
}
