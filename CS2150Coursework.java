/* CS2150Coursework.java
 * University ID: 180039762 | Name: Inder Panesar
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [T(2,treeValue,-12)] Tree
 *  |   |
 *  |   +-- [Rx(-90)] Trunk
 *  |   |
 *  |   +-- [T(0,2,0)] Leafy head
 *  |
 *  +-- [T(5,treeValue,-12)] Tree
 *  |   |
 *  |   +-- [Rx(-90)] Trunk
 *  |   |
 *  |   +-- [T(0,2,0)] Leafy head
 *  |
 *  +-- [T(5,treeValue,-24)] Tree
 *  |   |
 *  |   +-- [Rx(-90)] Trunk
 *  |   |
 *  |   +-- [T(0,2,0)] Leafy head
 *  |
 *  +-- [T(0.0,-1.0,-5.0) S(40,1,40)] Ground plane
 *  |
 *  +-- [T(0.0,20,-45.0) S(90,1,50) Ry(90)] Wall Plane
 *  |   |
 *  |   +-- [T(0.0,-0.5, 0.0)] Sun
 *  |
 *  +-- [T(deltaX, 2.0, deltaZ)] Diamond Icon 
 *  |   |
 *  | 	+-- [T(0, -2.5, 0)] Player
 *  |       |
 *  |    	+-- [T(0, 0, 0.6)] Gun
 *  |           |
 *  |			+-- [T(-bulletZ, 0, -0.7)] Bullet
 *  |
 *  +-- [T(-5.0f, 1.5f, -20.0f)] Diamond Icon	
 *  |   |
 *  |	+-- [T(0.0f, -2.0f, 0.0f)] Button 
 */
package coursework.InderPanesar;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * This sample shows two different actors, the player and the button. The player is able to shoot
 * the button and the button collides with the bullet shot by the player. The button in this is used 
 * to start Terraforming the world. This world is one that that is heavily polluted with dead grass and red skies.
 * To which the boxes only found one solution to this, as mentioned before Terraforming. They interact with the button
 * and begin the stages, the skys return to normal, tree's begin to regrow and the grass returns to it's natural green state. 
 *
 * CREDITS:
 * Grass Texture: https://www.brusheezy.com/textures/20185-seamless-green-grass-textures
 * Sky Texture: https://texturify.com/stock-photo/sky-10550.html
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * <li>Press W to move forwards
 * <li>Press S to move backwards
 * <li>Press A to move left
 * <li>Press D to move right
 * <li>Press SPACE to move shoot bullet
 * <li>Press R to reset the scene
 * </ul>
 *
 * @author Inder Panesar - (180039762) - Aston University
 */

public class CS2150Coursework extends GraphicsLab
{
    /** The texture for the sky */
    private Texture sky;
    
    /** The texture for the grass */
    private Texture grass;
    
    /** button class, contains collision method. */
    private Button button;
    
    /** display list id for the diamond - player */
    private final int diamondList = 1;
    
    /** display list id for the player cube */
    private final int cubeList = 2;
    
    /** display list id for the button cube */
    private final int buttonList = 3;
    
    /** display list id for the floor and wall in scene */
    private final int floorAndWallList = 4;

    /** display list id for the bullet */
    private final int bulletList = 5;
    
    /** display list id for the gun */
    private final int gunList = 6;

    /** move up is true once W is pressed moves player forward. */
    private boolean moveUp = false;
    
    /** move down is true once S is pressed moves player backwards. */
    private boolean moveDown = false;
    
    /** move left is true once A is pressed moves player left. */
	private boolean moveLeft = false;
	
    /** move right is true once D is pressed moves player right. */
	private boolean moveRight = false;
	
    /** Once true the player will shoot the bullet */
    private boolean shootBullet = false;

    /** The deltaX is the X value of player */
	private float deltaX = 0.0f;
	
	/** The deltaZ is the Z value of player */
    private float deltaZ = -5.0f;  
    
    /** The bulletZ is the Z value of the bullet */
    private float bulletZ = 0.0f;
    
    /** Is the rotation of the diamond */
    private float diamondRotationAngle= 90.0f;
       
    /** This is for when an button has not been hit by a bullet. */
    private float redbutton = 1f;
    
    /** When the button has been hit by the bullet. */
    private float bluebutton = 0f;
    
    /** When the button has been hit by the bullet, the trees will rise and this value will increase it's Y value. */
    private float treeValue = -5f;

    /** If the tree's should rise */
	private boolean treeRise = false;
	
    /** The size of the tree */
	private float treeSize = 0.8f;

    /** Texture for dead grass */
	private Texture diedgrass;

    /** Texture for red sky */
	private Texture redsky;
	
    /** Texture for the normal ground. */
	private Texture ground;

    /** Texture for the normal sky. */
	private Texture wall;

    //Animation scale changed to 0.005 from 0.01;
    public static void main(String args[])
    {   new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission",0.005f);
    }

    protected void initScene() throws Exception
    {
        // load the textures
		try{
			grass = loadTexture("coursework/InderPanesar/textures/GRASS.bmp");
			sky = loadTexture("coursework/InderPanesar/textures/SKY.bmp");
			diedgrass = loadTexture("coursework/InderPanesar/textures/DIEDGRASS.bmp");
			redsky = loadTexture("coursework/InderPanesar/textures/REDSKY.bmp");

		} catch(Exception e){
			grass = loadTexture("textures/GRASS.bmp");
			sky = loadTexture("textures/SKY.bmp");
			diedgrass = loadTexture("textures/DIEDGRASS.bmp");
			redsky = loadTexture("textures/REDSKY.bmp");
		}
		
		wall = redsky;
		ground = diedgrass;
		
        //Ambient light of the scene
		float globalAmbient[] = {0.2f,  0.2f,  0.2f, 1.0f};
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		// Define light properties in RGBA
		float diffuse0[] = {0.8f,  0.8f, 0.8f, 1.0f};
		float ambient0[] = {0.9f,  0.9f, 0.9f, 1.0f};
		//Define light location in xyz
		float position0[] = {0.0f,10.5f,-20.0f, 3.0f};
		
		// Give OpenGL the properties for the first light.
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		// Enable the light we have just implemented
		GL11.glEnable(GL11.GL_LIGHT0);

		// Allow for lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// Normals are re-normalised after transformations.
		GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

		
		
		
        //TODO: Initialise your resources here - might well call other methods you write.
        GL11.glNewList(diamondList,GL11.GL_COMPILE);
        {   drawDiamond();
        }
        GL11.glEndList();
        
        GL11.glNewList(cubeList,GL11.GL_COMPILE);
        {   drawUnitCube();
        }
        GL11.glEndList();

        GL11.glNewList(buttonList,GL11.GL_COMPILE);
        {   drawUnitCube();
        }
        GL11.glEndList();
        
        GL11.glNewList(floorAndWallList,GL11.GL_COMPILE);
        {   drawUnitPlane();
        }
        GL11.glEndList();
        
        GL11.glNewList(gunList,GL11.GL_COMPILE);
        {   drawGun();
        }
        GL11.glEndList();
        
        GL11.glNewList(bulletList,GL11.GL_COMPILE);
        {   drawBullet();
        }
        GL11.glEndList();
        
   }
        

    
    
    protected void checkSceneInput()
    {   
    	/*
    	 * If the player has not shot the bullet the player is able to move.
    	 */
    	if(!shootBullet) {
	        if(Keyboard.isKeyDown(Keyboard.KEY_W) && !moveDown)
	        {   moveUp = true; // If W is pressed move player up
	        }
	        
	        
	        else if(Keyboard.isKeyDown(Keyboard.KEY_S) && !moveUp)
	        {   moveDown = true; // If S is pressed move player down
	        }
	        
	        else if(Keyboard.isKeyDown(Keyboard.KEY_A)  && !moveRight)
	        {   moveLeft = true; // If A is pressed move player left
	        }
	        
	        
	        else if(Keyboard.isKeyDown(Keyboard.KEY_D)  && !moveLeft)
	        {   moveRight = true; // If D is pressed move player right
	        }
	        
	        /*
	         * Stops all the movements of the player
	         */
	        else if(Keyboard.isKeyDown(Keyboard.KEY_F))
	        {   
	        	moveUp = false;
	        	moveDown = false;
	        	moveRight = false;
	        	moveLeft = false;
	        }
    	}

    	/*
    	 * Shoot the bullet, when Space is pressed
    	 */
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        	shootBullet = true;
        }
        
    	/*
    	 * Press R to reset the scene.
    	 */
        else if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
        	resetScene();
        }

    }
    
    protected void updateScene()
    {  
    	/*
    	 * Move player up, by reducing Z value by 1, stop once deltaZ is less than -6.
    	 */
    	if(moveUp) {
    		if(deltaZ > (-6)) {
    			deltaZ -= 1.0f * getAnimationScale(); 
    		}
    		else {
    			moveUp = false;
    		}
    	}
    	
    	/*
    	 * Move player down, by increasing Z value by 1, stop once deltaZ is more than -5.
    	 */
    	if(moveDown) {
    		if(deltaZ < (-5)) {
    			deltaZ += 1.0f * getAnimationScale();
    		}
    		else {
    			moveDown = false;
    		}
    	}
    	
    	/*
    	 * Move player up, by reducing Z value by 1, stop once deltaZ is less than -6.
    	 */
    	
    	if(moveLeft) {
    		if(deltaX > (-5)) {
    			deltaX -= 1.0f * getAnimationScale();
    		}
    		else {
    			moveLeft = false;
    		}
    	}
    	
    	/*
    	 * Move player up, by reducing Z value by 1, stop once deltaZ is less than -6.
    	 */
    	if(moveRight) {
    		if(deltaX < (5)) {
    			deltaX += 1.0f * getAnimationScale();
    		}
    		else {
    			moveRight = false;
    		}
    	}
    	
    	/*
    	 * When space has been pressed, this method will be activated.
    	 * This handles the bullet.
    	 */
    	if(shootBullet) {
    		//Move bullet away from player in Z axis.
    		bulletZ -=  1.0f * getAnimationScale();
    		//Check for collision
    		if(button.checkCollisions(deltaX, bulletZ-5.0f)) {
    			setRedbutton(0f);
    			bluebutton = 1f;
    			treeRise = true;
    			shootBullet = false;
    			bulletZ = 0.0f;
    			deltaZ = -5.0f;
    		}
    		//If Bullet reaches this point return the bullet to original position
    		if(bulletZ < (-26.5)) {
    			shootBullet = false;
    			bulletZ = 0.0f;
    			deltaZ = -5.0f;
    		}
    		
    	}
    	
    	/*
    	 * If the trees have the button has been activated, then transform the world.
    	 */
    	if(treeRise) {
    		//Increase tree y value till it is greater than -1
    		if(treeValue < -1) {
    			treeValue +=  0.05f * getAnimationScale(); 
    			ground = grass;
    			wall = sky;
    		}
    		//Once it has reached the top, the tree will begin to grow in length.
    		else if(treeSize < 1.2) {
    			treeSize += 0.005 * getAnimationScale();
    		}
    	}

    }
    protected void renderScene()
    {
        GL11.glPushMatrix();
        {
            // how shiny are the front faces of the trunk (specular exponent)
            float trunkFrontShininess  = 20.0f;
            // specular reflection of the front faces of the trunk
            float trunkFrontSpecular[] = {0.2f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the trunk
            float trunkFrontDiffuse[]  = {0.38f, 0.29f, 0.07f, 1.0f};
            
            // set the material properties for the trunk using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, trunkFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(trunkFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(trunkFrontDiffuse));
            //GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(trunkFrontDiffuse));


            // position the tree
            GL11.glTranslatef(2.0f, treeValue, -12.0f);
            
            // draw the trunk using a cylinder quadric object. Surround the draw call with a
            // push/pop matrix pair, as the cylinder will originally be aligned with the Z axis
            // and will have to be rotated to align it along the Y axis
            GL11.glPushMatrix();
            {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.25f, 0.25f, 1.5f, 10, 10);
            }
            GL11.glPopMatrix();

            // how shiny are the front faces of the leafy head of the tree (specular exponent)
            float headFrontShininess  = 20.0f;
            // specular reflection of the front faces of the head
            float headFrontSpecular[] = {0.1f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the head
            float headFrontDiffuse[]  = {0.0f, 0.5f, 0.0f, 1.0f};
            
            // set the material properties for the head using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, headFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(headFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(headFrontDiffuse));

            // position and draw the leafy head using a sphere quadric object
            GL11.glTranslatef(0.0f, 2.0f, 0.0f);
            new Sphere().draw(treeSize, 10, 10);

        }
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        {
            // how shiny are the front faces of the trunk (specular exponent)
            float trunkFrontShininess  = 20.0f;
            // specular reflection of the front faces of the trunk
            float trunkFrontSpecular[] = {0.2f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the trunk
            float trunkFrontDiffuse[]  = {0.38f, 0.29f, 0.07f, 1.0f};
            
            // set the material properties for the trunk using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, trunkFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(trunkFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(trunkFrontDiffuse));


            // position the tree
            GL11.glTranslatef(5.0f, treeValue, -12.0f);
            
            // draw the trunk using a cylinder quadric object. Surround the draw call with a
            // push/pop matrix pair, as the cylinder will originally be aligned with the Z axis
            // and will have to be rotated to align it along the Y axis
            GL11.glPushMatrix();
            {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.25f, 0.25f, 1.5f, 10, 10);
            }
            GL11.glPopMatrix();

            // how shiny are the front faces of the leafy head of the tree (specular exponent)
            float headFrontShininess  = 20.0f;
            // specular reflection of the front faces of the head
            float headFrontSpecular[] = {0.1f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the head
            float headFrontDiffuse[]  = {0.0f, 0.5f, 0.0f, 1.0f};
            
            // set the material properties for the head using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, headFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(headFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(headFrontDiffuse));


            // position and draw the leafy head using a sphere quadric object
            GL11.glTranslatef(0.0f, 2.0f, 0.0f);
            new Sphere().draw(treeSize, 10, 10);
        	

        }
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        {
            // how shiny are the front faces of the trunk (specular exponent)
            float trunkFrontShininess  = 20.0f;
            // specular reflection of the front faces of the trunk
            float trunkFrontSpecular[] = {0.2f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the trunk
            float trunkFrontDiffuse[]  = {0.38f, 0.29f, 0.07f, 1.0f};
            
            // set the material properties for the trunk using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, trunkFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(trunkFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(trunkFrontDiffuse));


            // position the tree
            GL11.glTranslatef(5.0f, treeValue, -24.0f);
            
            // draw the trunk using a cylinder quadric object. Surround the draw call with a
            // push/pop matrix pair, as the cylinder will originally be aligned with the Z axis
            // and will have to be rotated to align it along the Y axis
            GL11.glPushMatrix();
            {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.25f, 0.25f, 1.5f, 10, 10);
            }
            GL11.glPopMatrix();

            // how shiny are the front faces of the leafy head of the tree (specular exponent)
            float headFrontShininess  = 20.0f;
            // specular reflection of the front faces of the head
            float headFrontSpecular[] = {0.1f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the head
            float headFrontDiffuse[]  = {0.0f, 0.5f, 0.0f, 1.0f};
            
            // set the material properties for the head using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, headFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(headFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(headFrontDiffuse));


            // position and draw the leafy head using a sphere quadric object
            GL11.glTranslatef(0.0f, 2.0f, 0.0f);
            new Sphere().draw(treeSize, 10, 10);
        	

        }
        GL11.glPopMatrix();
        
    	//GRASS render
    	GL11.glPushMatrix();
        {
        	//Disable the lighting so the lighting will not affect this polygon's texture.
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            
            //Submit the color white so it looks brighter.
            Colour.WHITE.submit();
            //Enable the texture and binds the texture to it.
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,ground.getTextureID());
            
            //Translate the Floor which in this case is the floor
            GL11.glTranslatef(0.0f,-1.0f,-5.0f);
            //Scale the Floor in X, Y, Z respectively
            GL11.glScalef(40.0f, 1.0f, 40.0f);
            
            // draw the plane using its display list
            GL11.glCallList(floorAndWallList);
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix(); 
        
    	//SKY-SUN render
        GL11.glPushMatrix();
        {
        	//Disable the lighting so the lighting will not affect this polygon's texture.
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);

            //Submit the color white so it looks brighter.
            Colour.WHITE.submit();
            //Enable the texture and binds the texture to it.
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,wall.getTextureID());
            
            //Translate the Wall which in this case is the sky
            GL11.glTranslatef(0.0f,20.0f,-45.0f);
            
            //Rotate the polygon so it y axis is now the z axis. A 90 degree anti-clockwise rotation.
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            
            //Scale the wall in the x and y directions.
            GL11.glScalef(90.0f, 1.0f, 50.0f);
            // draw the plane using its display list
            GL11.glCallList(floorAndWallList);
            // disable textures
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();

            //Reset the scale
            GL11.glScalef(1.0f/90.0f, 1.0f, 1.0f/50.0f);
            //Enable lighting again
            GL11.glEnable(GL11.GL_LIGHTING);
            // specular exponent of the front faces of the sun
        	float sunFrontShininess  = 2.0f;
            // specular reflection of the front faces of the sun
            float sunFrontSpecular[] = {0.6f, 0.6f, 0.6f, 1.0f};
            // diffuse reflection of the front faces of the sun
            float sunFrontDiffuse[]  = {0.1f, 0.1f, 0.00f, 1.0f};
            
            
            
            //Translate the sun
            GL11.glTranslatef(0.0f, 5.5f, 8.0f);


            // set the material properties for the sun using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, sunFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(sunFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(sunFrontDiffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(sunFrontDiffuse));

            //Draw a new sphere.
            new Sphere().draw(1.0f, 20, 20);

           
        }
        GL11.glPopMatrix();
        
    	//button render
        GL11.glPushMatrix();
        {    
           //Translation of the diamond	
           GL11.glTranslatef(-5.0f, 1.5f, -20f);
           
           //Specular exponent of the front faces of the diamond.
           float diamondFrontShininess  = 100.0f;
           //Specular reflection of the front faces of the diamond.
           float diamondFrontSpecular[] = {0.8f, 0.8f, 0.8f, 1.0f};
           //Diffuse reflection of the front faces of the diamond.
           float diamondFrontDiffuse[]  = {0.1f, 0.9f, 0.5f, 1.0f};       
          
           //set material properties of the diamond
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, diamondFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(diamondFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diamondFrontDiffuse));
             
           // draw the diamond using its display list
           GL11.glCallList(diamondList);
           GL11.glPopAttrib();
            
           //Translation of the button cube.
           GL11.glTranslatef(0f, -2.0f, 0.0f);
           //Rotating the button Cube
           GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
           
           //Specular exponent of the front faces of the button.
           float buttonFrontShininess  = 10.0f;
           //Specular reflection of the front faces of the button.
           float buttonFrontSpecular[] = {0.2f, 0.2f, 0.2f, 1.0f};
           //Diffuse reflection of the front faces of the button.
           float buttonFrontDiffuse[]  = {0.0f, 0.2f, bluebutton, 1.0f};
           
           //set material properties of the button
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, buttonFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(buttonFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(buttonFrontDiffuse));

           //updating the vertex points in the button class which is used for collision.
           //This allows for the vertex point to be the world coordinates.
           button.setTranslation(-5f, -20f);
           
           // draw the button using its display list
           GL11.glCallList(buttonList);
           GL11.glPopAttrib();
        }
        GL11.glPopMatrix(); 
        
        //Player-Diamond-Gun render
        GL11.glPushMatrix();
        {   
        	
           //Translation of the diamond	
           GL11.glTranslatef(deltaX, 2f, deltaZ);
           
           //Specular exponent of the front faces of the diamond.
           float diamondFrontShininess  = 1000.0f;
           //Specular reflection of the front faces of the diamond.
           float diamondFrontSpecular[] = {0.8f, 0.8f, 0.8f, 1.0f};
           //Diffuse reflection of the front faces of the diamond.
           float diamondFrontDiffuse[]  = {0.2f, 0.9f, 0.2f, 1.0f};            
         
           //set material properties of the diamond
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, diamondFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(diamondFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(diamondFrontDiffuse));
            
           // draw the diamond using its display list
           GL11.glCallList(diamondList);
           GL11.glPopAttrib();

           //Translation of the player cube.
           GL11.glTranslatef(0, -2.5f, 0);
           //Rotating the player Cube
           GL11.glRotatef(diamondRotationAngle, 0.0f, 1.0f, 0.0f);

           //Specular exponent of the front faces of the cube.
           float cubeFrontShininess  = 10.0f;
           //Specular reflection of the front faces of the cube.
           float cubeFrontSpecular[] = {0.2f, 0.2f, 0.2f, 1.0f};
           //Diffuse reflection of the front faces of the cube.
           float cubeFrontDiffuse[]  = {1f, 0.2f, 0.2f, 1.0f};
           
           //set material properties of the player cube
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, cubeFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(cubeFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(cubeFrontDiffuse));
	        
           // draw the player cube using its display list
           GL11.glCallList(cubeList);
           GL11.glPopAttrib();
           
           //Translation of the gun.
           GL11.glTranslatef(0.0f, 0.0f, 0.6f);
           
           //Specular exponent of the front faces of the gun.
           float gunFrontShininess  = 1000.0f;
           //Specular reflection of the front faces of the gun.
           float gunFrontSpecular[] = {0.2f, 0.2f, 0.2f, 1.0f};
           //Diffuse reflection of the front faces of the gun.
           float gunFrontDiffuse[]  = {0f, 0.2f, 0.2f, 1.0f};
           
           //set material properties of the gun
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, gunFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(gunFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(gunFrontDiffuse));
           
	        // draw the gun using its display list
           GL11.glCallList(gunList);
           
           GL11.glPopAttrib();
           
           //Translation of the bullet.
           GL11.glTranslatef(-bulletZ, 0.0f, -0.7f);
           
           //Specular exponent of the front faces of the bullet.
           float bulletFrontShininess  = 50.0f;
           //Specular reflection of the front faces of the bullet.
           float bulletFrontSpecular[] = {0.2f, 0.2f, 0.2f, 1.0f};
           //Diffuse reflection of the front faces of the bullet.
           float bulletFrontDiffuse[]  = {0.5f, 0.5f, 0.1f, 1.0f};
           
           //set material properties of the bullet
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, bulletFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(bulletFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(bulletFrontDiffuse));
           
	        // draw the bullet using its display list
           GL11.glCallList(bulletList);
           
           GL11.glPopAttrib();

        }
        GL11.glPopMatrix(); 
        
        

    }
    
    private void resetScene()
    {
        // reset all attributes that are modified by user controls or animations
        deltaZ =  -5.0f;  
        deltaX =  0.0f;
        diamondRotationAngle= 90.0f;
        treeValue = -5.0f;
        treeRise = false;
        setRedbutton(1f);
        bluebutton = 0f;
        bulletZ = 0.0f;
        shootBullet = false;
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        treeSize = 0.8f;
        wall = redsky;
        ground = diedgrass;
    }
    
    protected void setSceneCamera()
    {
        super.setSceneCamera();
       
        //camera position
        GLU.gluLookAt(0.0f, 2.0f, 8.0f,        
  		      0.0f, 3.0f, 0.0f,   
  		      0.0f, 20.0f, 0.0f);
    }
    

    protected void cleanupScene()
    {
    	
    }
    
    private void drawUnitPlane()
    {
        Vertex v1 = new Vertex(-0.5f, 0.0f,-0.5f); // left,  back
        Vertex v2 = new Vertex( 0.5f, 0.0f,-0.5f); // right, back
        Vertex v3 = new Vertex( 0.5f, 0.0f, 0.5f); // right, front
        Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left,  front
        
        // draw the plane geometry. order the vertices so that the plane faces up
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v3.toVector(),v2.toVector(),v1.toVector()).submit();
            
            GL11.glTexCoord2f(0.0f,0.0f);
            v4.submit();
            
            GL11.glTexCoord2f(1.0f,0.0f);
            v3.submit();
            
            GL11.glTexCoord2f(1.0f,1.0f);
            v2.submit();
            
            GL11.glTexCoord2f(0.0f,1.0f);
            v1.submit();
        }
        GL11.glEnd();
        
        // if the user is viewing an axis, then also draw this plane
        // using lines so that axis aligned planes can still be seen
        if(isViewingAxis())
        {
            // also disable textures when drawing as lines
            // so that the lines can be seen more clearly
            GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            {
                v4.submit();
                v3.submit();
                v2.submit();
                v1.submit();
            }
            GL11.glEnd();
            GL11.glPopAttrib();
        }
    }
    
    
    private void drawUnitCube()
    {
        // the vertices for the cube (note that all sides have a length of 1)
        Vertex v1 = new Vertex(-0.5f, -0.5f,  0.5f);
        Vertex v2 = new Vertex(-0.5f,  0.5f,  0.5f);
        Vertex v3 = new Vertex( 0.5f,  0.5f,  0.5f);
        Vertex v4 = new Vertex( 0.5f, -0.5f,  0.5f);
        Vertex v5 = new Vertex(-0.5f, -0.5f, -0.5f);
        Vertex v6 = new Vertex(-0.5f,  0.5f, -0.5f);
        Vertex v7 = new Vertex( 0.5f,  0.5f, -0.5f);
        Vertex v8 = new Vertex( 0.5f, -0.5f, -0.5f);
        
        button = new Button(v1,v2,v3,v4,v5,v6,v7,v8);

        // draw the near face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v3.toVector(),v2.toVector(),v1.toVector(),v4.toVector()).submit();
            
            v3.submit();
            v2.submit();
            v1.submit();
            v4.submit();
        }
        GL11.glEnd();

        // draw the left face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v2.toVector(),v6.toVector(),v5.toVector(),v1.toVector()).submit();
            
            v2.submit();
            v6.submit();
            v5.submit();
            v1.submit();
        }
        GL11.glEnd();

        // draw the right face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v7.toVector(),v3.toVector(),v4.toVector(),v8.toVector()).submit();
            
            v7.submit();
            v3.submit();
            v4.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the top face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v7.toVector(),v6.toVector(),v2.toVector(),v3.toVector()).submit();
            
            v7.submit();
            v6.submit();
            v2.submit();
            v3.submit();
        }
        GL11.glEnd();

        // draw the bottom face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v1.toVector(),v5.toVector(),v8.toVector()).submit();
            
            v4.submit();
            v1.submit();
            v5.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the far face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v6.toVector(),v7.toVector(),v8.toVector(),v5.toVector()).submit();
            
            v6.submit();
            v7.submit();
            v8.submit();
            v5.submit();
        }
        GL11.glEnd();
    }
   
    
    /*
     * Modified version of the unit cube.
     */
    private void drawBullet()
    {
    	//The alterated vertices for the bullet
        Vertex v1 = new Vertex(	-0.05f,  -0.02f,  0.65f);
        Vertex v2 = new Vertex(	-0.05f,  0.02f,  0.65f);
        Vertex v3 = new Vertex( 0.05f,  0.02f,  0.65f);
        Vertex v4 = new Vertex( 0.05f,  -0.02f,  0.65f);
        Vertex v5 = new Vertex(	-0.05f, -0.02f,  0.55f);
        Vertex v6 = new Vertex(	-0.05f,  0.02f,  0.55f);
        Vertex v7 = new Vertex( 0.05f,  0.02f,  0.55f);
        Vertex v8 = new Vertex( 0.05f,  -0.02f,  0.55f);

        // draw the near face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v3.toVector(),v2.toVector(),v1.toVector(),v4.toVector()).submit();
            
            v3.submit();
            v2.submit();
            v1.submit();
            v4.submit();
        }
        GL11.glEnd();

        // draw the left face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v2.toVector(),v6.toVector(),v5.toVector(),v1.toVector()).submit();
            
            v2.submit();
            v6.submit();
            v5.submit();
            v1.submit();
        }
        GL11.glEnd();

        // draw the right face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v7.toVector(),v3.toVector(),v4.toVector(),v8.toVector()).submit();
            
            v7.submit();
            v3.submit();
            v4.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the top face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v7.toVector(),v6.toVector(),v2.toVector(),v3.toVector()).submit();
            
            v7.submit();
            v6.submit();
            v2.submit();
            v3.submit();
        }
        GL11.glEnd();

        // draw the bottom face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v1.toVector(),v5.toVector(),v8.toVector()).submit();
            
            v4.submit();
            v1.submit();
            v5.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the far face:
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v6.toVector(),v7.toVector(),v8.toVector(),v5.toVector()).submit();
            
            v6.submit();
            v7.submit();
            v8.submit();
            v5.submit();
        }
        GL11.glEnd();
    }
    
	/*
	 * Diamond Polygon
	 */
    private void drawDiamond() {
    	//The vertices for the diamond
    	Vertex v1 = new Vertex(0f, 1f, 0f);
    	Vertex v2 = new Vertex(-0.5f, 0f, 0.5f);
    	Vertex v3 = new Vertex(0.5f, 0f, 0.5f);
    	Vertex v4 = new Vertex(0f, -1f, 0f);
    	Vertex v5 = new Vertex(0.5f, 0f, -0.5f);
    	Vertex v6 = new Vertex(-0.5f, 0f, -0.5f);

    	//Top of Front Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v2.toVector(),v3.toVector()).submit();

            v1.submit();
            v2.submit();
            v3.submit(); 
        }
        GL11.glEnd();
        
        //Bottom of Front Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
        	
            new Normal(v3.toVector(),v2.toVector(),v4.toVector()).submit();
            
            v3.submit();
            v2.submit();
            v4.submit(); 
        }
        GL11.glEnd();
        
        //Top of Right Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v5.toVector(),v3.toVector(),v4.toVector()).submit();
            v5.submit();
            v3.submit();
            v4.submit(); 
        }
        GL11.glEnd();

        //Bottom of Right Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v3.toVector(),v5.toVector()).submit();
            v1.submit();
            v3.submit();
            v5.submit(); 
        }
        GL11.glEnd();
        
        //Top of Left Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v2.toVector(),v6.toVector(),v4.toVector()).submit();
            v2.submit();
            v6.submit();
            v4.submit(); 
        }
        GL11.glEnd();
        
        //Bottom of Left Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v6.toVector(),v2.toVector()).submit();
            v1.submit();
            v6.submit();
            v2.submit(); 
        }
        GL11.glEnd();
        
        //Top of Back Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v5.toVector(),v6.toVector()).submit();
            v1.submit();
            v5.submit();
            v6.submit(); 
        }
        GL11.glEnd();
        
        //Bottom of Back Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v6.toVector(),v5.toVector(),v4.toVector()).submit();
            v6.submit();
            v5.submit();
            v4.submit(); 
        }
        GL11.glEnd();	
    }
    
	/*
	 * Gun Polygon.
	 */
    private void drawGun() {
    	//The vertices for the gun
    	Vertex v1 = new Vertex(0.25f, 0.125f, 0.1f);
    	Vertex v2 = new Vertex(-0.25f, 0.125f, 0.1f);
    	Vertex v3 = new Vertex(-0.25f, -0.125f, 0.1f);
    	Vertex v4 = new Vertex(-0.05f, -0.125f, 0.1f);
    	Vertex v5 = new Vertex(0.25f, 0.0f, 0.1f);
    	Vertex v6 = new Vertex(0.25f, 0.125f, -0.1f);
    	Vertex v7 = new Vertex(-0.25f, 0.125f, -0.1f);
    	Vertex v8 = new Vertex(-0.25f, -0.125f, -0.1f);
    	Vertex v9 = new Vertex(-0.05f, -0.125f, -0.1f);
    	Vertex v10 = new Vertex(0.25f, 0.0f, -0.1f);

    	//Front face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(), v2.toVector(), v3.toVector(), v4.toVector()).submit();
            v1.submit();
            v2.submit();
            v3.submit();
            v4.submit();
            v5.submit();
        }
        GL11.glEnd();
        
        //Back face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v10.toVector(), v9.toVector(), v8.toVector(), v7.toVector()).submit();
            v10.submit();
            v9.submit();
            v8.submit();
            v7.submit();
            v6.submit();
        }
        GL11.glEnd();
        
        //Top face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v6.toVector(), v7.toVector(), v2.toVector(), v1.toVector()).submit();
            v6.submit();
            v7.submit();
            v2.submit();
            v1.submit();
        }
        GL11.glEnd();
        
        //Right Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v6.toVector(), v1.toVector(), v5.toVector(), v10.toVector()).submit();
            v6.submit();
            v1.submit();
            v5.submit();
            v10.submit();
        }
        GL11.glEnd();
        
        //Left Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v8.toVector(), v3.toVector(), v2.toVector(), v7.toVector()).submit();
            v8.submit();
            v3.submit();
            v2.submit();
            v7.submit();
        }
        GL11.glEnd();
        
        //Bottom Left Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v3.toVector(), v8.toVector(), v9.toVector(), v4.toVector()).submit();
            v3.submit();
            v8.submit();
            v9.submit();
            v4.submit();
        }
        GL11.glEnd();
  	
        //Bottom Right Face
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(), v9.toVector(), v10.toVector(), v5.toVector()).submit();
            v4.submit();
            v9.submit();
            v10.submit();
            v5.submit();
        }
        GL11.glEnd();
    }

    
	/**
	 * Get the R value of the red button
	 * @return the value for red button
	 */
	public float getRedbutton() {
		return redbutton;
	}

	/**
	 * Get the R value of the red button
	 * @param redbutton new float value.
	 */
	public void setRedbutton(float redbutton) {
		this.redbutton = redbutton;
	}

}