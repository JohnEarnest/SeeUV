import java.io.*;
import java.nio.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class SeeUV {

	public static void main(String[] args) throws Exception {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());

		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("SeeUV");
		Display.create();
		setup();
		long lastframe = (System.nanoTime() / 1000000);
		while(!Display.isCloseRequested()) {
			long thisframe = (System.nanoTime() / 1000000);
			tick((int)(thisframe - lastframe));
			draw();
			lastframe = thisframe;
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	static float rotation = 0;
	static int textureid  = 0;

	static void setup() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		try {
			FloatBuffer buffer = Model.load("../test/lamp.obj");
			textureid = Texture.load("../test/welp.png");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	static void tick(int delta) {
		rotation += 0.1 * delta;
	}

	static void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureid);
		glEnable(GL_TEXTURE_2D);
		glPushMatrix();
			glTranslatef(320, 240, 0);
			glRotatef(rotation, 0, 0, 1);
			glBegin(GL_QUADS);
				glTexCoord2f(0, 0); glVertex2f(-100, -100);
				glTexCoord2f(0, 1); glVertex2f( 100, -100);
				glTexCoord2f(1, 1); glVertex2f( 100,  100);
				glTexCoord2f(1, 0); glVertex2f(-100,  100);
			glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
}